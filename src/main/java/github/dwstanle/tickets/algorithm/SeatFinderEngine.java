package github.dwstanle.tickets.algorithm;

import github.dwstanle.tickets.model.ReservationRequest;
import github.dwstanle.tickets.model.ReservationResult;
import github.dwstanle.tickets.model.SeatMap;

public interface SeatFinderEngine<T extends BookingMemento> {
    ReservationResult findBestAvailable(ReservationRequest request);

    // todo - feels more like it should be in the factory...
    T createMemento(SeatMap layout);
}
