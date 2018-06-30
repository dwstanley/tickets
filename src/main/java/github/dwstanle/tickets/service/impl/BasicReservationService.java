package github.dwstanle.tickets.service.impl;

import github.dwstanle.tickets.algorithm.BookingMemento;
import github.dwstanle.tickets.algorithm.SeatFinderEngine;
import github.dwstanle.tickets.model.*;
import github.dwstanle.tickets.service.ReservationService;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BasicReservationService<T extends BookingMemento> implements ReservationService {

    // todo replace with ReservationRepository
    private final Map<Integer, Reservation> reservations = new HashMap<>();

    private final SeatFinderEngine<T> engine;

    public BasicReservationService(SeatFinderEngine<T> engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    @Override
    public SeatAssignmentResult findAndHoldBestAvailable(SeatAssignmentRequest request) {
        // this implementation ignores when requested seats are already present
        SeatAssignmentResult result = engine.findBestAvailable(request);
        if (result.isSuccess()) {
            holdSeats(request.toBuilder().requestedSeats(result.getSeats()).build());
        }
        return result;
    }

    @Override
    public String reserveSeats(int reservationId, String accountId) {
        return null;
    }

    @Override
    public SeatAssignmentResult holdSeats(SeatAssignmentRequest request) {
        Objects.requireNonNull(request);

        SeatAssignmentResult result;
        Optional<Set<Seat>> seats = request.getRequestedSeats();

        if (seats.isPresent() && areSeatsAvailable(request.getEvent(), seats.get())) {
            result = new SeatAssignmentResult(seats, request.getAccount(), true);
        } else {
            result = new SeatAssignmentResult(Optional.empty(), request.getAccount(), false);
        }

        return result;
    }

    /**
     * Returns true if the seats specified are available for the event.
     */
    protected boolean areSeatsAvailable(Event event, Collection<Seat> seats) {
        return false;
    }

//    protected VenueMemento createVenueStateFromReservations(Event event) {
//        return null;
//    }

    /**
     * Instead of storing the current state of venue in two places we rebuild the venue state from the reservations.
     * This could be optimized by caching or saving the venue state if performance becomes an issue.
     */
    protected T createMementoFromReservations(Event event) {
        T memento = engine.createMemento(event.getVenue().getLayout());
        reservations.values().stream()
                .filter(reservation -> event.equals(reservation.getEvent()))
                .forEach(reservation -> addToMemento(memento, reservation));
        return memento;
    }

    protected void doReserveSeats(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }

    private void addToMemento(T memento, Reservation reservation) {
        reservation.getSeats().forEach(seat -> memento.setSeat(seat, reservation.getStatus()));
    }


}
