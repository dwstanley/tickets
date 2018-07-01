package github.dwstanle.tickets.algorithm.impl;

import github.dwstanle.tickets.algorithm.*;
import github.dwstanle.tickets.model.Account;
import github.dwstanle.tickets.model.ReservationRequest;
import github.dwstanle.tickets.model.ReservationResult;
import github.dwstanle.tickets.model.SeatMap;

import java.util.Objects;

import static java.util.Collections.emptySet;
import static java.util.Optional.empty;

public class BasicSeatFinderEngine<T extends BookingMemento> implements SeatFinderEngine<T> {

    private final SeatAssignmentGenerator<T> generator;
    private final SeatAssignmentEvaluator<T> evaluator;
    private final BookingMementoFactory<T> factory;

    public BasicSeatFinderEngine(SeatAssignmentGenerator<T> generator, SeatAssignmentEvaluator<T> evaluator, BookingMementoFactory<T> factory) {
        this.generator = Objects.requireNonNull(generator);
        this.evaluator = Objects.requireNonNull(evaluator);
        this.factory = Objects.requireNonNull(factory);
    }

    @Override
    public ReservationResult findBestAvailable(ReservationRequest request) {
        T currentSateOfVenue = factory.of(request.getEvent().getCurrentSeatMap());
        T bestResult = evaluator.findBest(generator.generateAllAvailable(request, currentSateOfVenue));
        return toReservationResult(bestResult, request.getAccount());
    }

    @Override
    public T createMemento(SeatMap layout) {
        return factory.of(layout);
    }

    private ReservationResult toReservationResult(T bestResult, Account account) {
        return (null == bestResult) ?
                new ReservationResult(empty(), emptySet(), account, false) :
                new ReservationResult(empty(), bestResult.getNewSeats().get(), account, true); // todo review use of empty reservation
    }

}