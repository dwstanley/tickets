package github.dwstanle.tickets.algorithm.impl;

import github.dwstanle.tickets.algorithm.*;
import github.dwstanle.tickets.model.Account;
import github.dwstanle.tickets.model.SeatAssignmentRequest;
import github.dwstanle.tickets.model.SeatAssignmentResult;
import github.dwstanle.tickets.model.SeatMap;

import java.util.Objects;

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
    public SeatAssignmentResult findBestAvailable(SeatAssignmentRequest request) {
        T currentSateOfVenue = factory.of(request.getEvent().getCurrentSeatMap());
        T bestResult = evaluator.findBest(generator.generateAllAvailable(request, currentSateOfVenue));
        return toSeatAssignmentResult(bestResult, request.getAccount());
    }

    @Override
    public T createMemento(SeatMap layout) {
        return null;
    }

    private SeatAssignmentResult toSeatAssignmentResult(T bestResult, Account account) {
        return (null == bestResult) ?
                new SeatAssignmentResult(empty(), account, false) :
                new SeatAssignmentResult(bestResult.getNewSeats(), account, true);
    }

}