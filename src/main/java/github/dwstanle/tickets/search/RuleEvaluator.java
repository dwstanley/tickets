package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.model.Seat;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// todo implement
public class RuleEvaluator {
    public RuleEvaluator(int requestedNumberOfSeats, SeatMap origin) {

    }

    public Optional<Set<Seat>> findBest(Set<List<Point>> allSolutions) {
        Set<Seat> result = getFirst(allSolutions).stream()
                .map(point -> new Seat(point.y, point.x))
                .collect(Collectors.toSet());
        return Optional.ofNullable(result);

    }

    public int score(List<Point2D> point2DList) {
        return 0;
    }

    private List<Point> getFirst(Set<List<Point>> allSolutions) {
        return allSolutions.iterator().hasNext() ? allSolutions.iterator().next() : null;
    }

    private Set<Seat> toSeats(List<Point> point2DList) {
        return point2DList.stream()
                .map(point -> new Seat(point.y, point.x))
                .collect(Collectors.toSet());
    }
}