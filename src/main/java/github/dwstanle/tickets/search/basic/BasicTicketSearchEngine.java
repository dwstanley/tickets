package github.dwstanle.tickets.search.basic;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.search.SeatMapEvaluator;
import github.dwstanle.tickets.search.SeatMapFactory;
import github.dwstanle.tickets.search.SeatMapGenerator;
import github.dwstanle.tickets.search.TicketSearchEngine;
import github.dwstanle.tickets.service.ReservationRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;

public class BasicTicketSearchEngine<T extends SeatMap> implements TicketSearchEngine<T> {

    private final SeatMapGenerator<T> generator;
    private final SeatMapEvaluator<T> evaluator;
    private final SeatMapFactory<T> factory;

    public BasicTicketSearchEngine(SeatMapGenerator<T> generator, SeatMapEvaluator<T> evaluator, SeatMapFactory<T> factory) {
        this.generator = Objects.requireNonNull(generator);
        this.evaluator = Objects.requireNonNull(evaluator);
        this.factory = Objects.requireNonNull(factory);
    }

    @Override
    public Optional<Set<Seat>> findBestAvailable(ReservationRequest request) {
        T currentSateOfVenue = factory.of(request.getEvent().getVenue().getLayout().getSeats());
        T bestResult = evaluator.findBest(generator.generateAllAvailable(request, currentSateOfVenue));
        return toReservationResult(bestResult);
    }

    @Override
    public T copySeatMap(List<List<String>> seatMap) {
        return factory.of(seatMap);
    }

    // todo - revisit this implementation
    private Optional<Set<Seat>> toReservationResult(T bestResult) {
        return (null == bestResult) ? empty() : bestResult.getNewSeats();
    }

}