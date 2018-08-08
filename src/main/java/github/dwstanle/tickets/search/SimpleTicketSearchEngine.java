package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.model.Seat;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class SimpleTicketSearchEngine implements TicketSearchEngine {

    @Override
    public Optional<Set<Seat>> findBestAvailable(int requestedNumberOfSeats, SeatMap seatMap) {
        Generator generate = new LeftFillGenerator(requestedNumberOfSeats, seatMap);
        Evaluator evaluate = new SimpleEvaluator(requestedNumberOfSeats, seatMap);
        return evaluate.findBest(generate.findAllSolutions());
    }

    @Override
    public Optional<Set<Seat>> findFirstAvailable(int requestedNumberOfSeats, SeatMap seatMap) {
        throw new UnsupportedOperationException("Placeholder. Not yet implemented.");
    }

}