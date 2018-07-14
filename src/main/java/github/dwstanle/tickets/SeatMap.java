package github.dwstanle.tickets;

import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.util.SeatMapStrings;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static github.dwstanle.tickets.util.SeatMapStrings.SIMPLE_LAYOUT_STR;

/**
 * Represents a 2-dimensional collection of seats and their status. Rows of varying length are handled by marking the
 * seat status as 'unavailable'. Other valid seat status are described in {@link SeatStatus}.
 */
public interface SeatMap {

    /**
     * Simple 8x7 SeatMap
     */
    SeatMap SIMPLE = fromString(SIMPLE_LAYOUT_STR);

    /**
     * Creates a new {@code SeatMap} the given {@code Path}. The file is expected to be formatted as a CSV file, comma
     * delimited using the seat status codes described in {@link SeatStatus}.
     *
     * @param path {@code Path} of the CSV file, cannot be null.
     * @return a new {@code SeatMap} if the provided CSV file is correctly formatted; exception otherwise.
     * @throws IOException              if the path cannot be found.
     * @throws IllegalArgumentException if the file is incorrectly formatted.
     */
    static SeatMap fromPath(Path path) throws IOException {
        return new StringListSeatMap(SeatMapStrings.fromPath(path, ","));
    }

    /**
     * Creates a new {@code SeatMap} from the given string. The string is expected to be in the following format and must
     * consist only of status codes from {@link SeatStatus}.
     *
     * <pre>
     * A A A A A A
     * A A A A A A
     * A A A A A A
     * A A A A A A
     * </pre>
     * <p>
     * As a one line string this may look like
     * {@code A A A A A A\nA A A A A A\nA A A A A A\nA A A A A A}
     *
     * @param seatMapStr the string to parse into a {@code SeatMap}, cannot be null.
     * @throws IllegalArgumentException if the string is incorrectly formatted.
     * @returna new {@code SeatMap} if the string is correctly formatted, exception otherwise.
     */
    static SeatMap fromString(String seatMapStr) {
        return new StringListSeatMap(SeatMapStrings.fromString(seatMapStr));
    }

    /**
     * Creates a new {@code SeatMap} from the given {@code SeatMap}. Seat values are copied such that modifications to
     * the new {@link SeatMap} do not affect the original.
     *
     * @param seatMap the {@code SeatMap} to create the new {@code SeatMap} from, cannot be null.
     * @return a new {@code SeatMap} copy of the one specified.
     */
    static SeatMap fromSeatMap(StringListSeatMap seatMap) {
        return new StringListSeatMap(SeatMapStrings.copy(seatMap.getSeats()));
    }

    /**
     * Creates a new {@code SeatMap} from the given 2-dimensional list of strings. Each row in the list is expected to
     * be the same size and and must consist only of status codes from {@link SeatStatus}.
     * <p>
     * An example list is provided below.
     * <pre>
     * [[X,A,A,A]
     *  [X,X,A,A]
     *  [X,X,A,A]]
     * </pre>
     *
     * @param seats 2-dimensional list of seat status, cannot be null.
     * @return a new {@code SeatMap} if the list is correctly formatted, exception otherwise.
     * @throws IllegalArgumentException if the list is incorrectly formatted or contains illegal characters.
     */
    static SeatMap from2DList(List<List<String>> seats) {
        return new StringListSeatMap(SeatMapStrings.copy(seats));
    }

    /**
     * Set the status of the specified seat.
     *
     * @param seat   Seat to store the status of, cannot be null.
     * @param status Status of the seat to store, cannot be null.
     */
    void setSeat(Seat seat, SeatStatus status);

    /**
     * Return the status of the given seat.
     *
     * @param seat Seat to return the status of, cannot be null.
     * @return status of the specified seat.
     * @throws IllegalArgumentException if the seat is not known (outside of the scope of this SeatMap).
     */
    SeatStatus getSeatStatus(Seat seat);

    /**
     * Return the number of rows contained in this SeatMap.
     *
     * @return number of rows, always positive
     */
    int getNumberOfRows();

    /**
     * Return the number of columns contained in this SeatMap.
     *
     * @return number of columns, always positive
     */
    int getNumberOfCols();

    /**
     * Return the current number of seats available in this SeatMap. Available seats are defined as those that are not
     * reserved, held, stage, or obstacle.
     *
     * @return number of seats available, always non-negative.
     */
    int numberOfSeatsAvailable();

    /**
     * Create a new SeatMap and apply the status provided to the seats identified.
     *
     * @param seatsToAdd seats to set the status of in the copy, cannot be null.
     * @param status     status to set, cannot be null.
     * @return a copy of the current SeatMap with the new status applied.
     */
    SeatMap copyWithNewSeats(Set<Seat> seatsToAdd, SeatStatus status);

}