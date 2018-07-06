package github.dwstanle.tickets.service.impl;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.exception.IllegalRequestException;
import github.dwstanle.tickets.exception.ReservationNotFoundException;
import github.dwstanle.tickets.model.Reservation;
import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.model.Section;
import github.dwstanle.tickets.repository.ReservationRepository;
import github.dwstanle.tickets.search.TicketSearchEngine;
import github.dwstanle.tickets.service.ReservationRequest;
import github.dwstanle.tickets.service.ReservationService;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
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
import static github.dwstanle.tickets.service.impl.SeatMaps.*;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.stream.Collectors.partitioningBy;

@Service
public class BasicReservationService implements ReservationService {

    // todo - add to readme comment about why locks and blocking methods were used instead of async and futures

    // todo - make this configurable
    public static final int HOLD_EXPIRATION_IN_MINUTES = 1;

    @Getter
    private static class SectionInfo {
        @NonNull
        private final Section section;
        private final SeatMap seatMap;
        private int numAvailable;
        private double price = 0;

        SectionInfo(Section section) {
            this.section = Objects.requireNonNull(section);
            this.seatMap = section.getCopyOfLayout();
            this.numAvailable = seatMap.numberOfSeatsAvailable();
        }

    }

    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentMap<Long, Lock> eventLock = new ConcurrentHashMap<>();

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TicketSearchEngine engine;

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
                    reservationHeld = holdSeats(request.toBuilder().requestedSeats(bestSeatsAvailable.get()).build());
                    break;
                }
            }
//        } finally {
//            eventLock.unlock();
//        }

        return reservationHeld;
    }

    @Override
    public Optional<Reservation> reserveSeats(long reservationId, String accountId) {

        Reservation holdReservation = reservationRepository.findById(reservationId).orElse(null);

        if (null == holdReservation) {
            throw new ReservationNotFoundException(reservationId);
        }

        if (null == accountId || !accountId.equalsIgnoreCase(holdReservation.getAccount().getEmail())) {
            throw new IllegalRequestException("reservation does not belong to requesting user, " + accountId + ".");
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

                result = Optional.of(reservation);

                // probably overkill, reservations are cancelled automatically when identified in createSeatMapFromReservations
                scheduledExecutor.schedule(() -> cancelReservation(reservation.getId()), HOLD_EXPIRATION_IN_MINUTES, MINUTES);

//            } finally {
//                eventLock.unlock();
//            }

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

    public void setSearchEngine(TicketSearchEngine engine) {
        this.engine = engine;
    }

    private boolean isExpired(Reservation reservation) {
        boolean isExpired = false;
        if (HELD == reservation.getStatus()) {
            long elapsedTime = System.currentTimeMillis() - reservation.getTimestamp();
            isExpired = MINUTES.toMillis(HOLD_EXPIRATION_IN_MINUTES) < elapsedTime;
        }
        return isExpired;
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
                .findByEventId(eventId)
                .collect(partitioningBy(this::isExpired));

        reservationExpiredMap.get(true).forEach(reservation -> cancelReservation(reservation.getId()));
        reservationExpiredMap.get(false).forEach(reservation -> addToSeatMap(seatMap, reservation));

        return seatMap;
    }

    private List<SectionInfo> findSectionsToSearch(ReservationRequest request) {
        return request.getEvent().getVenue().getSections().stream()
                .map(SectionInfo::new)
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
