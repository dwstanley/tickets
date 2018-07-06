package github.dwstanle.tickets;

import github.dwstanle.tickets.model.Seat;

import java.util.Optional;
import java.util.Set;

public interface SeatMap {

    void setSeat(Seat seat, SeatStatus status);

    Optional<Set<Seat>> getNewSeats();


    SeatStatus getSeatStatus(Seat seat);

    int getNumberOfRows();

    int getNumberOfCols();

    int numberOfSeatsAvailable();


    <T extends SeatMap> T copy();
}
