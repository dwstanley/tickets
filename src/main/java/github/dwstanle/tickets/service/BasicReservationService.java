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

/**
 * Default implementation of ReservationService.
 */
@Service
public class BasicReservationService implements ReservationService {

    static final int DEFAULT_RESERVATION_TIMEOUT_SECS = 60;

    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentMap<Long, Lock> eventLockMap = new ConcurrentHashMap<>();

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TicketSearchEngine engine;

    @Autowired
    private Environment env;

    private Integer reservationTimeoutInSecs;

    @Override
    public Optional<Reservation> findAndHoldBestAvailable(ReservationRequest request) {

        if (request.getNumberOfSeats() <= 0) {
            throw new IllegalRequestException("reservation request must contain a positive number of seats.");
        }

        // this implementation ignores requested seats provided in ReservationRequest
        Optional<Reservation> reservationHeld = Optional.empty();
        Lock eventLock = getLock(request.getEvent().getId());
        try {
            eventLock.lock();
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
        } finally {
            eventLock.unlock();
        }

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

        return Optional.ofNullable(reserve);
    }

    @Override
    public Optional<Reservation> holdSeats(ReservationRequest request) {

        Long eventId = request.getEvent().getId();
        Section section = request.getRequestedSection();
        Set<Seat> seats = request.getRequestedSeats();

        Optional<Reservation> result = Optional.empty();

        if (!seats.isEmpty() && areRequestedSeatsAvailable(eventId, section, seats)) {

            Lock eventLock = getLock(eventId);
            try {
                eventLock.lock();
                Reservation reservation = reservationRepository.save(Reservation.builder()
                        .seats(seats)
                        .event(request.getEvent())
                        .account(request.getAccount())
                        .status(HELD).build());

                result = Optional.ofNullable(reservation);

                // probably overkill, reservations are cancelled automatically when new searches are performed
                scheduledExecutor.schedule(() -> cancelHold(eventId, reservation.getId()), getReservationTimeout(), SECONDS);

            } finally {
                eventLock.unlock();
            }

        }

        return result;
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

        Optional<Section> section = event.getVenue().getSections().stream()
                .filter(s -> Objects.equals(sectionId, s.getId()))
                .findFirst();

        SeatMap seatMap = null;
        if (section.isPresent()) {
            seatMap = fillSeatMapWithReservations(event.getId(), section.get());
        }

        return seatMap;
    }

    /**
     * Check if the current reservation is still on hold and if so, cancel it.
     */
    private void cancelHold(long eventId, long reservationId) {
        Lock eventLock = getLock(eventId);
        try {
            eventLock.lock();
            Optional<Reservation> reservation = reservationRepository.findById(reservationId);
            boolean isReserved = reservation.isPresent() && (RESERVED == reservation.get().getStatus());
            if (!isReserved) {
                cancelReservation(reservationId);
            }
        } finally {
            eventLock.unlock();
        }
    }

    /**
     * Returns whether or not the specified reservation is expired.
     */
    private boolean isExpired(Reservation reservation) {
        boolean isExpired = false;
        if (HELD == reservation.getStatus()) {
            long elapsedTime = System.currentTimeMillis() - reservation.getTimestamp();
            isExpired = SECONDS.toMillis(getReservationTimeout()) < elapsedTime;
        }
        return isExpired;
    }

    /**
     * Lazily obtain reservation timeout from environment properties; or DEFAULT_RESERVATION_TIMEOUT_SECS if not found.
     */
    private int getReservationTimeout() {
        if (null == reservationTimeoutInSecs) {
            try {
                reservationTimeoutInSecs = Integer.valueOf(env.getProperty("reservation.timeoutInSeconds"));
            } catch (NumberFormatException nfe) {
                reservationTimeoutInSecs = DEFAULT_RESERVATION_TIMEOUT_SECS;
            }
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

    /**
     * Identify which sections should be searched based on the given request. Sections with too few open seats or too
     * high of a price will be filtered out.
     */
    private List<SectionInfo> findSectionsToSearch(ReservationRequest request) {
        return request.getEvent().getVenue().getSections().stream()
                // might be more efficient to unwind this loop so we only build the section infos one at a time
                .map(section -> new SectionInfo(request.getEvent().getId(), section))
                .filter(sectionInfo -> sectionInfo.getNumAvailable() >= request.getNumberOfSeats())
                .filter(sectionInfo -> sectionInfo.getPrice() <= request.getMaxPrice())
                // sort by number of available per section so stadium fills evenly (optional)
                .sorted(Comparator.comparing(SectionInfo::getNumAvailable))
                .collect(Collectors.toList());
    }

    /**
     * Obtain lock for given id, if no lock exists a new one is created and returned.
     */
    private Lock getLock(Long eventId) {
        eventLockMap.putIfAbsent(eventId, new ReentrantLock());
        return eventLockMap.get(eventId);
    }

    /**
     * Data class used to encapsulate information related to sections and delay
     * processing events until time of need.
     */
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

}
