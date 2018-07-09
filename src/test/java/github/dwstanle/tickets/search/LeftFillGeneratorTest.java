package github.dwstanle.tickets.search;

import github.dwstanle.tickets.StringListSeatMap;
import github.dwstanle.tickets.util.SeatMapStrings;
import github.dwstanle.tickets.test.Slow;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.awt.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@Category(Slow.class)
public class LeftFillGeneratorTest {

    private boolean printDebug = false;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void solve() {
        doSolve("scenario.1.input", "scenario.1.2.expected", 2, printDebug);
        doSolve("scenario.1.input", "scenario.1.3.expected", 3, printDebug);
        doSolve("scenario.1.input", "scenario.1.4.expected", 4, printDebug);
        doSolve("scenario.2.input", "scenario.2.4.expected", 4, printDebug);
        doSolve("scenario.2.input", "scenario.2.6.expected", 6, printDebug);
        doSolve("scenario.3.input", "scenario.3.6.expected", 6, printDebug);
        doSolve("scenario.3.input", "scenario.3.7.expected", 7, printDebug);
//        doSolve("scenario.4.input", "scenario.3.7.expected", 7, true);
//        doSolve("scenario.5.input", "scenario.3.7.expected", 7, true);
        doSolve("scenario.6.input", "scenario.6.6.expected", 6, printDebug);
    }

    private void doSolve(String inputFile, String expectedFile, int requestedNumberOfSeats, boolean printDebug) {
        StringListSeatMap seatMap = loadSeatMap(inputFile);
        Set<List<Point>> result = new LeftFillGenerator(requestedNumberOfSeats, seatMap).findAllSolutions();
        assertEquals(loadFile(expectedFile), solutionsAsString(seatMap, result));

        if (printDebug) {
            System.err.println("\nSolution found for: " + inputFile + ":\n");
            System.err.println(solutionsAsString(seatMap, result));
        }
    }

    private String solutionsAsString(StringListSeatMap seatMap, Set<List<Point>> resultingSeatRequest) {
        StringBuilder sb = new StringBuilder();
        resultingSeatRequest.forEach(points -> {
            List<List<String>> seatCopy = SeatMapStrings.copy(seatMap.getSeats());
            for (Point point : points) {
                seatCopy.get(point.y).set(point.x, "*");
            }
            sb.append(SeatMapStrings.toString(seatCopy)).append("\n\n");
        });
        return sb.toString().trim();
    }

    private StringListSeatMap loadSeatMap(String fileName) {
        try {
            return new StringListSeatMap(SeatMapStrings.fromPath(path(fileName)));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String loadFile(String filename) {
        try {
            return new String(Files.readAllBytes(path(filename)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Path path(String filename) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource(filename).toURI());
    }

}