package github.dwstanle.tickets.search.basic;

import github.dwstanle.tickets.Slow;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static github.dwstanle.tickets.SeatStatus.AVAILABLE;

@Category(Slow.class)
public class SolverInstancePerformanceTest {

    private boolean printDebug = true;

    @Before
    public void setUp() {
    }

    @Test
    public void solveLargeVenue() {
//         todo output memory usage during solve()
//        for (int i = 100; i <= 1000; i = i + 100) {
//            timeSolve(10, i);
//        }
    }

    @Test
    public void solveLargeRequest() {
//         todo output memory usage during solve()
//        for (int i = 1; i <= 50; i++) {
//            timeSolve(i, 300);
//        }
    }

    private Duration timeSolve(int numSeats, int venueSize) {
        List<List<String>> seatMap = randomSeatMap(venueSize, venueSize);
        Instant start = Instant.now();
        new SolverInstance(seatMap, numSeats).solve();
        Duration duration = Duration.between(start, Instant.now());
        if (printDebug) {
            System.err.println(String.format("Finding %d seats for a square venue of %d seats took %d ms.",
                    numSeats, venueSize * venueSize, duration.toMillis()));
        }
        return duration;
    }

    // todo make seat values random
    private List<List<String>> randomSeatMap(int rows, int cols) {
        List<List<String>> seats = new ArrayList<>(cols);
        for (int i = 0; i < rows; i++) {
            seats.add(new ArrayList<>(Collections.nCopies(cols, AVAILABLE.getCode())));
        }
        return seats;
    }


}