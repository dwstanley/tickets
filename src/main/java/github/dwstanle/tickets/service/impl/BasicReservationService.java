package github.dwstanle.tickets.service.impl;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.SeatStatus;
import github.dwstanle.tickets.exception.IllegalRequestException;
import github.dwstanle.tickets.exception.ReservationNotFoundException;
import github.dwstanle.tickets.model.Event;
import github.dwstanle.tickets.model.Reservation;
import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.repository.ReservationRepository;
import github.dwstanle.tickets.search.TicketSearchEngine;
import github.dwstanle.tickets.service.ReservationRequest;
import github.dwstanle.tickets.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static github.dwstanle.tickets.SeatStatus.*;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.stream.Collectors.partitioningBy;

@Service
public class BasicReservationService<T extends SeatMap> implements ReservationService {

    // todo - all public methods should be put on single thread executor to avoid collisions with reservation timeout operations
    //        or all reservation and search testing needs to be thread safe :(
    //        scheduledExecutor.submit()

    // todo - make this configurable
    public static final int HOLD_EXPIRATION_IN_MINUTES = 1;

    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private ReservationRepository reservationRepository;

    private TicketSearchEngine<T> engine;

//    public BasicReservationService(TicketSearchEngine<T> engine) {
//        this.engine = Objects.requireNonNull(engine);
//    }

    @Override
    public Optional<Reservation> findAndHoldBestAvailable(ReservationRequest request) {
        // this implementation ignores when requested seats are already present in ReservationRequest
        Optional<Set<Seat>> bestSeatsAvailable = engine.findBestAvailable(request);
        Optional<Reservation> reservationHeld = Optional.empty();
        if (bestSeatsAvailable.isPresent()) {
            reservationHeld = holdSeats(request.toBuilder().requestedSeats(bestSeatsAvailable.get()).build());
        }
        return reservationHeld;
    }

    @Override
    public String reserveSeats(long reservationId, String accountId) {

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

        Reservation reserve = holdReservation.toBuilder().status(RESERVED).build();

        reservationRepository.save(reserve);

        try {
            createMementoFromReservations(holdReservation.getEvent());
        } catch (Exception e) {
            // rollback reservation?
            throw e;
        }

        return "SUCCESS";
    }

    @Override
    public Optional<Reservation> holdSeats(ReservationRequest request) {

        Objects.requireNonNull(request);
        Objects.requireNonNull(request.getAccount());
        Objects.requireNonNull(request.getAccount().getEmail());

        Optional<Reservation> result = Optional.empty();
        Set<Seat> seats = request.getRequestedSeats();

        if (!seats.isEmpty() && areSeatsAvailable(request.getEvent(), seats)) {

            Reservation reservation = reservationRepository.save(Reservation.builder()
                    .seats(seats)
                    .event(request.getEvent())
                    .account(request.getAccount())
                    .status(HELD).build());

            result = Optional.of(reservation);

            // probably overkill, reservations are cancelled automatically when identified in createMementoFromReservations
            scheduledExecutor.schedule(() -> cancelReservation(reservation.getId()), HOLD_EXPIRATION_IN_MINUTES, MINUTES);

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

    protected boolean isExpired(Reservation reservation) {
        boolean isExpired = false;
        if (HELD == reservation.getStatus()) {
            long elapsedTime = System.currentTimeMillis() - reservation.getTimestamp();
            isExpired = MINUTES.toMillis(HOLD_EXPIRATION_IN_MINUTES) < elapsedTime;
        }
        return isExpired;
    }

    protected void doReserveSeats(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    /**
     * Returns true if the seats specified are available for the event.
     */
    protected boolean areSeatsAvailable(Event event, Collection<Seat> seats) {
        boolean available = false;
        if (areSeatsInRange(event, seats)) {
            T venueState = createMementoFromReservations(event);
            long numRequestedSeatsAvail = seats.stream()
                    .filter(seat -> AVAILABLE == venueState.getSeatStatus(seat))
                    .count();
            available = (numRequestedSeatsAvail == seats.size());
        }
        return available;
    }

    private boolean areSeatsInRange(Event event, Collection<Seat> seats) {

        int rowSize = event.getVenue().getLayout().getSeats().size();
        int colSize = event.getVenue().getLayout().getSeats().get(0).size();

        boolean inRange = true;
        for (Seat seat : seats) {
            if (seat.getRow() >= rowSize || seat.getRow() < 0
                    || seat.getCol() >= colSize || seat.getCol() < 0) {
                inRange = false;
                break;
            }
        }

        return inRange;
    }

    /**
     * Instead of storing the current state of venue in two places we rebuild the venue state from the reservations.
     * This could be optimized by caching or saving the venue state if performance becomes an issue.
     */
    // todo note: this method will fail if ANY reservations are invalid, is there a better way to handle them?
    protected T createMementoFromReservations(Event event) {
        T seatMap = engine.copySeatMap(event.getVenue().getLayout().getSeats());

        // split expired and non-expired reservations for this event, expired reservations map to true
        Map<Boolean, List<Reservation>> reservationExpiredMap = reservationRepository
                .findByEventId(event.getId())
                .collect(partitioningBy(this::isExpired));

        reservationExpiredMap.get(true).forEach(reservation -> cancelReservation(reservation.getId()));
        reservationExpiredMap.get(false).forEach(reservation -> addToMemento(seatMap, reservation));

        return seatMap;
    }

    protected void addToMemento(T memento, Reservation reservation) {
        reservation.getSeats().forEach(seat -> addToMemento(memento, seat, reservation.getStatus()));
    }

    protected void addToMemento(T memento, Seat seat, SeatStatus seatStatus) {
        switch (memento.getSeatStatus(seat)) {
            case RESERVED:
            case STAGE:
            case OBSTACLE:
                throw new UnsupportedOperationException("requested seat cannot be reserved or held.");
            default:
                memento.setSeat(seat, seatStatus);
        }
    }

    public void setSearchEngine(TicketSearchEngine<T> engine) {
        this.engine = engine;
    }
}
