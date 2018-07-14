package github.dwstanle.tickets.search;

import github.dwstanle.tickets.model.Seat;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Evaluator {

    /**
     * Find the best collection of seats from the provided possibilities; if no solutions are given or acceptable an
     * empty optional is returned.
     *
     * @param allSolutions collection of solutions to select the best from, cannot be null
     * @return the best collection of seats from the options provided; if possible
     */
    Optional<Set<Seat>> findBest(Set<List<Point>> allSolutions);
}