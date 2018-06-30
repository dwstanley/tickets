package github.dwstanle.tickets.algorithm;

import github.dwstanle.tickets.model.SeatAssignmentRequest;
import github.dwstanle.tickets.model.SeatAssignmentResult;
import github.dwstanle.tickets.model.SeatMap;

public interface SeatFinderEngine<T extends BookingMemento> {
    SeatAssignmentResult findBestAvailable(SeatAssignmentRequest request);

    // todo - feels more like it should be in the factory...
    T createMemento(SeatMap layout);
}
