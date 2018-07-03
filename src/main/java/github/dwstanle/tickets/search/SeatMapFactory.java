package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;

import java.util.List;

public interface SeatMapFactory<T extends SeatMap> {
    T of(List<List<String>> seatStates);
}
