package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.model.Seat;

import java.util.Optional;
import java.util.Set;

public interface TicketSearchEngine {

    /**
     * Find the best available collection of seats in the given {@code SeatMap} that can fit
     * {@code requestedNumberOfSeats} people.
     *
     * @param requestedNumberOfSeats number of seats to find, must be positive
     * @param seatMap                to search, cannot be null
     * @return collection seats satisfying the request or {@code Optional.empty()} if no solution is found.
     */
    Optional<Set<Seat>> findBestAvailable(int requestedNumberOfSeats, SeatMap seatMap);

    /**
     * Find the first available collection of seats that can seat {@code requestedNumberOfSeats}.
     *
     * @param requestedNumberOfSeats number of seats to find, must be positive
     * @param seatMap                to search, cannot be null
     * @return collection seats satisfying the request or {@code Optional.empty()} if no solution is found.
     */
    Optional<Set<Seat>> findFirstAvailable(int requestedNumberOfSeats, SeatMap seatMap);
}
