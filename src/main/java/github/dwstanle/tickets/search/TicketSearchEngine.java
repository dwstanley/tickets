package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.model.Seat;

import java.util.Optional;
import java.util.Set;

public interface TicketSearchEngine {
    Optional<Set<Seat>> findBestAvailable(int requestedNumberOfSeats, SeatMap seatMap);
    Optional<Set<Seat>> findFirstAvailable(int requestedNumberOfSeats, SeatMap seatMap);
}
