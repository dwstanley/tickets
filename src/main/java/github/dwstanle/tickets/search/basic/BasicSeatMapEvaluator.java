package github.dwstanle.tickets.search.basic;

import github.dwstanle.tickets.StringListSeatMap;
import github.dwstanle.tickets.search.SeatMapEvaluator;

import java.util.Collection;

public class BasicSeatMapEvaluator implements SeatMapEvaluator<StringListSeatMap> {

    @Override
    public StringListSeatMap findBest(Collection<StringListSeatMap> assignmentOptions) {
        // todo implement
        return assignmentOptions.iterator().next();
    }
}