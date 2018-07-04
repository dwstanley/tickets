package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.service.ReservationRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TicketSearchEngine<T extends SeatMap> {
    Optional<Set<Seat>> findBestAvailable(ReservationRequest request);
    T copySeatMap(List<List<String>> origin); // todo consider replacing factory with this method
}
