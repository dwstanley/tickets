package github.dwstanle.tickets.service.test;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.search.TicketSearchEngine;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class TestTicketSearchEngine implements TicketSearchEngine {
    @Override
    public Optional<Set<Seat>> findBestAvailable(int requestedNumberOfSeats, SeatMap seatMap) {
        return Optional.of(new HashSet<>(Arrays.asList(new Seat(1, 0), new Seat(1, 1))));
    }

    @Override
    public Optional<Set<Seat>> findFirstAvailable(int requestedNumberOfSeats, SeatMap seatMap) {
        return Optional.of(new HashSet<>(Arrays.asList(new Seat(1, 0), new Seat(1, 1))));
    }
}
