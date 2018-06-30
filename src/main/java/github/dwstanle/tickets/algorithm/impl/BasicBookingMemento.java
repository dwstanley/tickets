package github.dwstanle.tickets.algorithm.impl;

import github.dwstanle.tickets.algorithm.BookingMemento;
import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.model.VenueMemento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BasicBookingMemento implements BookingMemento {

    private final List<List<String>> seats = new ArrayList<>();

    @Override
    public VenueMemento asSeatMap() {
        return null;
    }

//    @Override
//    public Set<Seat> getNewSeatAssignments() {
//        return null;
//    }

    @Override
    public void setSeat(Seat seat, String status) {
        seats.get(seat.getRow()).set(seat.getCol(), status);
    }

    @Override
    public Optional<Set<Seat>> getNewSeats() {
        return Optional.empty();
    }

}
