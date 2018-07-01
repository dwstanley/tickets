package github.dwstanle.tickets.algorithm;

import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.model.SeatStatus;
import github.dwstanle.tickets.model.VenueMemento;

import java.util.Optional;
import java.util.Set;

public interface BookingMemento {
    VenueMemento asSeatMap();

//    Set<Seat> getNewSeatAssignments();

//    void setSeat(Seat seat, String status);
    void setSeat(Seat seat, SeatStatus status);

    Optional<Set<Seat>> getNewSeats();

    SeatStatus getSeatStatus(Seat seat);

}
