package github.dwstanle.tickets.algorithm;

import github.dwstanle.tickets.algorithm.BookingMemento;

import java.util.Collection;

public interface SeatAssignmentEvaluator<T extends BookingMemento> {
    T findBest(Collection<T> assignmentOptions);
//    SeatAssignmentResult<T> findBest(Collection<SeatAssignmentResult<T>> assignmentOptions);
}
