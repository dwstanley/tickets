package github.dwstanle.tickets.algorithm;

import github.dwstanle.tickets.algorithm.BookingMemento;
import github.dwstanle.tickets.model.SeatAssignmentRequest;

import java.util.Set;

public interface SeatAssignmentGenerator<T extends BookingMemento> {
    Set<T> generateAllAvailable(SeatAssignmentRequest request, T originalBookingState);
//    Set<SeatAssignmentResult<T>> generateAllAvailable(SeatAssignmentRequest<T> request, T originalBookingState);

}
