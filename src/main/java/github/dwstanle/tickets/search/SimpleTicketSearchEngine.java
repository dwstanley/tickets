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
        LeftFillGenerator generate = new LeftFillGenerator(requestedNumberOfSeats, seatMap);
        SimpleEvaluator evaluate = new SimpleEvaluator(requestedNumberOfSeats, seatMap);
        return evaluate.findBest(generate.findAllSolutions());
    }

    @Override
    public Optional<Set<Seat>> findFirstAvailable(int requestedNumberOfSeats, SeatMap seatMap) {
        LeftFillGenerator generate = new LeftFillGenerator(requestedNumberOfSeats, seatMap);
        RuleEvaluator evaluate = new RuleEvaluator(requestedNumberOfSeats, seatMap);
        return evaluate.findBest(generate.findAllSolutions(seats -> evaluate.score(seats) > 0));
    }

}