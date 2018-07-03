package github.dwstanle.tickets.search.impl;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.search.*;
import github.dwstanle.tickets.model.Account;
import github.dwstanle.tickets.service.ReservationRequest;
import github.dwstanle.tickets.service.ReservationResult;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptySet;
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
    public ReservationResult findBestAvailable(ReservationRequest request) {
        T currentSateOfVenue = factory.of(request.getEvent().getVenue().getLayout().getSeats());
        T bestResult = evaluator.findBest(generator.generateAllAvailable(request, currentSateOfVenue));
        return toReservationResult(bestResult, request.getAccount());
    }

//    @Override
    public T copySeatMap(List<List<String>> seatMap) {
        return factory.of(seatMap);
    }

    private ReservationResult toReservationResult(T bestResult, Account account) {
        return (null == bestResult) ?
                new ReservationResult(empty(), emptySet(), account, false) :
                new ReservationResult(empty(), bestResult.getNewSeats().get(), account, true); // todo review use of empty reservation
    }

}