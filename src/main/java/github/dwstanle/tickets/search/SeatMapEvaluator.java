package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;

import java.util.Collection;

public interface SeatMapEvaluator<T extends SeatMap> {
    T findBest(Collection<T> assignmentOptions);
}
