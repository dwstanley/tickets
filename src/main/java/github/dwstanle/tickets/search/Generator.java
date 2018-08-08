package github.dwstanle.tickets.search;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface Generator {

    /**
     * Find all solutions to the request specified on construction.
     * <p>
     * The returned solution is represented as a list of {@code Point} objects which represent the location of the
     * selected seats.
     *
     * @return The set of solutions found; or empty set if none are found.
     */
    Set<java.util.List<Point>> findAllSolutions();

    /**
     * Find all solutions to the request specified on construction with the additional option to break out of the search
     * algorithm if the provided condition is met.
     *
     * @param breakEarlyCondition Condition to exit the search engine, cannot be null
     * @return All calculated solutions up to the point where the break early condition was met.
     * @throws UnsupportedOperationException This feature has not yet been implemented
     */
    Set<java.util.List<Point>> findAllSolutions(Predicate<List<Point>> breakEarlyCondition);
}