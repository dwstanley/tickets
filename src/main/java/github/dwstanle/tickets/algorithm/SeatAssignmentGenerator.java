package github.dwstanle.tickets.algorithm;

import github.dwstanle.tickets.model.ReservationRequest;

import java.util.Set;

public interface SeatAssignmentGenerator<T extends BookingMemento> {
    Set<T> generateAllAvailable(ReservationRequest request, T originalBookingState);
//    Set<ReservationResult<T>> generateAllAvailable(ReservationRequest<T> request, T originalBookingState);

}
