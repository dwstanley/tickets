package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.service.ReservationRequest;

import java.util.Set;

public interface SeatMapGenerator<T extends SeatMap> {
    Set<T> generateAllAvailable(ReservationRequest request, T originalBookingState);
}
