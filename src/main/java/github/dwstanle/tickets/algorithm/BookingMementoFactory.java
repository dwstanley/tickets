package github.dwstanle.tickets.algorithm;

import github.dwstanle.tickets.model.VenueMemento;

public interface BookingMementoFactory<T extends BookingMemento> {
    T of(VenueMemento seatMap);
}
