package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.service.ReservationRequest;
import github.dwstanle.tickets.service.ReservationResult;

import java.util.List;

public interface TicketSearchEngine<T extends SeatMap> {
    ReservationResult findBestAvailable(ReservationRequest request);
    T copySeatMap(List<List<String>> origin); // todo consider replacing factory with this method
}
