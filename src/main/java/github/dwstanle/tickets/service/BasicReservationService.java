package github.dwstanle.tickets.service;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.exception.IllegalRequestException;
import github.dwstanle.tickets.exception.ReservationNotFoundException;
import github.dwstanle.tickets.model.Event;
import github.dwstanle.tickets.model.Reservation;
import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.model.Section;
import github.dwstanle.tickets.repository.ReservationRepository;
import github.dwstanle.tickets.search.TicketSearchEngine;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static github.dwstanle.tickets.SeatStatus.HELD;
import static github.dwstanle.tickets.SeatStatus.RESERVED;
import static github.dwstanle.tickets.service.util.SeatMaps.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.partitioningBy;

@Service
public class BasicReservationService implements ReservationService {

    // todo - add to readme comment about why locks and blocking methods were used instead of async and futures

    @Getter
    private class SectionInfo {
        @NonNull
        private final Section section;
        private final Long eventId;
        private SeatMap seatMap;
        private Integer numAvailable;
        private double price = 0;

        SectionInfo(Long eventId, Section section) {
            this.section = Objects.requireNonNull(section);
            this.eventId = Objects.requireNonNull(eventId);
        }

        public Integer getNumAvailable() {
            if (null == numAvailable) {
                this.numAvailable = getSeatMap().numberOfSeatsAvailable();
            }
            return this.numAvailable;
        }

        public SeatMap getSeatMap() {
            if (null == this.seatMap) {
                this.seatMap = fillSeatMapWithReservations(eventId, section);
            }
            return this.seatMap;
        }

    }

    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentMap<Long, Lock> eventLock = new ConcurrentHashMap<>();

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TicketSearchEngine engine;

    @Autowired
    private Environment env;

    private Integer reservationTimeoutInSecs;

//    @Autowired
//    public BasicReservationService(ReservationRepository reservationRepository, TicketSearchEngine engine) {
//        this.reservationRepository = Objects.requireNonNull(reservationRepository);
//        this.engine = Objects.requireNonNull(engine);
//    }

    @Override
    public Optional<Reservation> findAndHoldBestAvailable(ReservationRequest request) {

        if (request.getNumberOfSeats() <= 0) {
            throw new IllegalRequestException("reservation request must contain a positive number of seats.");
        }

        // this implementation ignores requested seats provided in ReservationRequest
        Optional<Reservation> reservationHeld = Optional.empty();
//        Lock eventLock = getLock(request.getEvent().getId());
//        try {
//            eventLock.lock();
        for (SectionInfo section : findSectionsToSearch(request)) {
            Optional<Set<Seat>> bestSeatsAvailable = engine.findBestAvailable(request.getNumberOfSeats(), section.seatMap);
            if (bestSeatsAvailable.isPresent()) {
                reservationHeld = holdSeats(request.toBuilder()
                        .requestedSection(section.section)
                        .requestedSeats(bestSeatsAvailable.get())
                        .build());
                break;
            }
        }
//        } finally {
//            eventLock.unlock();
//        }

        return reservationHeld;
    }

    @Override
    public Optional<Reservation> reserveSeats(long reservationId, String accountEmail) {

        Reservation holdReservation = reservationRepository.findById(reservationId).orElse(null);

        if (null == holdReservation) {
            throw new ReservationNotFoundException(reservationId);
        }

        if (null == accountEmail || !accountEmail.equalsIgnoreCase(holdReservation.getAccount())) {
            throw new IllegalRequestException("reservation does not belong to requesting user, " + accountEmail + ".");
        }

        if (isExpired(holdReservation)) {
            throw new IllegalRequestException("reservation holding period has expired.");
            // consider checking if seats are still available and reserving for them anyway
        }

        Reservation reserve = reservationRepository.save(holdReservation.toBuilder().status(RESERVED).build());

        // todo - rerun fillSeatMapFromReservations and verify state of venue is still good, if not do we roll back the reservation?

        return Optional.ofNullable(reserve);
    }

    @Override
    public Optional<Reservation> holdSeats(ReservationRequest request) {

        Long eventId = request.getEvent().getId();
        Section section = request.getRequestedSection();
        Set<Seat> seats = request.getRequestedSeats();

        Optional<Reservation> result = Optional.empty();

        if (!seats.isEmpty() && areRequestedSeatsAvailable(eventId, section, seats)) {

//            Lock eventLock = getLock(eventId);
//            try {
//                eventLock.lock();
            Reservation reservation = reservationRepository.save(Reservation.builder()
                    .seats(seats)
                    .event(request.getEvent())
                    .account(request.getAccount())
                    .status(HELD).build());

            result = Optional.ofNullable(reservation);

            // probably overkill, reservations are cancelled automatically when new searches are performed
            scheduledExecutor.schedule(() -> cancelHold(reservation.getId()), getReservationTimeout(), SECONDS);

//            } finally {
//                eventLock.unlock();
//            }

        }

        return result;
    }

    /**
     * Check if the current reservation is still on hold and if so, cancel it.
     */
    private void cancelHold(long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        boolean isReserved = reservation.isPresent() && (RESERVED == reservation.get().getStatus());
        if (!isReserved) {
            cancelReservation(reservationId);
        }
    }

    @Override
    public void cancelReservation(long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    @Override
    public Optional<Reservation> findById(long reservationId) {
        return reservationRepository.findById(reservationId);
    }


    @Override
    public int findNumberOfSeatsAvailable(Event event) {
        return event.getVenue().getSections().stream()
                .map(section -> new SectionInfo(event.getId(), section))
                .mapToInt(SectionInfo::getNumAvailable)
                .sum();
    }

    @Override
    public SeatMap getUpdatedSeatMap(Event event, long sectionId) {

        // todo throw error if section not found
        Optional<Section> section = event.getVenue().getSections().stream()
                .filter(s -> Objects.equals(sectionId, s.getId()))
                .findFirst();

        SeatMap seatMap = null;
        if (section.isPresent()) {
            seatMap = fillSeatMapWithReservations(event.getId(), section.get());
        }

        return seatMap;
    }

    public void setSearchEngine(TicketSearchEngine engine) {
        this.engine = engine;
    }

    private boolean isExpired(Reservation reservation) {
        boolean isExpired = false;
        if (HELD == reservation.getStatus()) {
            long elapsedTime = System.currentTimeMillis() - reservation.getTimestamp();
            isExpired = SECONDS.toMillis(getReservationTimeout()) < elapsedTime;
        }
        return isExpired;
    }

    private int getReservationTimeout() {
        if (null == reservationTimeoutInSecs) {
            reservationTimeoutInSecs = Integer.valueOf(env.getProperty("reservation.timeoutInSeconds"));
        }
        return reservationTimeoutInSecs;
    }

    /**
     * Returns true if the seats specified are available for the event.
     */
    private boolean areRequestedSeatsAvailable(Long eventId, Section section, Collection<Seat> seats) {
        boolean available = false;
        if (areSeatsInRange(section.getCopyOfLayout(), seats)) {
            SeatMap seatMapAfterReservations = fillSeatMapWithReservations(eventId, section);
            available = areSeatsAvailable(seatMapAfterReservations, seats);
        }
        return available;
    }

    /**
     * Instead of storing the current state of venue in two places we rebuild the venue state from the reservations.
     * This could be optimized by caching or saving the venue state if performance becomes an issue.
     */
    // todo note: this method will fail if ANY reservations are invalid, is there a better way to handle them?
    private SeatMap fillSeatMapWithReservations(Long eventId, Section section) {
        SeatMap seatMap = section.getCopyOfLayout();

        // split expired and non-expired reservations for this event, expired reservations map to true
        Map<Boolean, List<Reservation>> reservationExpiredMap = reservationRepository
                .findByEventId(eventId).stream()
                .collect(partitioningBy(this::isExpired));

        reservationExpiredMap.get(true).forEach(reservation -> cancelReservation(reservation.getId()));
        reservationExpiredMap.get(false).forEach(reservation -> addToSeatMap(seatMap, reservation));

        return seatMap;
    }

    private List<SectionInfo> findSectionsToSearch(ReservationRequest request) {
        return request.getEvent().getVenue().getSections().stream()
//                .map(SectionInfo::new) // todo might be more efficient to unwind this loop so we only build the section infos one at a time
                .map(section -> new SectionInfo(request.getEvent().getId(), section))
                .filter(sectionInfo -> sectionInfo.getNumAvailable() >= request.getNumberOfSeats())
                .filter(sectionInfo -> sectionInfo.getPrice() <= request.getMaxPrice())
                // sort by number of available per section so stadium fills evenly (optional)
                .sorted(Comparator.comparing(SectionInfo::getNumAvailable))
                .collect(Collectors.toList());
    }

    private Lock getLock(Long key) {
        eventLock.putIfAbsent(key, new ReentrantLock());
        return eventLock.get(key);
    }

}
