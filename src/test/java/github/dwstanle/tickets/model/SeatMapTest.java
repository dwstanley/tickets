package github.dwstanle.tickets.model;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static github.dwstanle.tickets.model.SeatMap.SIMPLE_LAYOUT_STR;
import static github.dwstanle.tickets.model.SeatStatus.HELD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SeatMapTest {

    private Path testFile;

    @Before
    public void setUp() throws Exception {
        testFile = Paths.get(ClassLoader.getSystemResource("SimpleLayout.csv").toURI());
    }

    @Test
    public void seatMap() {
    }

    @Test
    public void fromString() {
        SeatMap layout = SeatMap.fromString(SIMPLE_LAYOUT_STR);
        assertEquals(layout.toString(), SIMPLE_LAYOUT_STR);
    }

    @Test
    public void fromPath() {
        SeatMap layout = SeatMap.fromPath(testFile);
        assertEquals(layout.toString(), SIMPLE_LAYOUT_STR);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void createAndAdd() {
        SeatMap layout = SeatMap.fromString(SIMPLE_LAYOUT_STR);
        layout.getRows().get(0).add(HELD.getCode());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void createAndEditEntireRow() {
        SeatMap layout = SeatMap.fromString(SIMPLE_LAYOUT_STR);
        layout.getRows().set(0, new ArrayList<>());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void createAndEdit() {
        SeatMap layout = SeatMap.fromString(SIMPLE_LAYOUT_STR);
        layout.getRows().get(0).set(1, HELD.getCode());
    }

    @Test
    public void copy() {
        SeatMap layout = SeatMap.fromString(SIMPLE_LAYOUT_STR);
        SeatMap layoutCopy = SeatMap.fromSeatMap(layout);
        assertEquals(layout.toString(), layoutCopy.toString());
    }

    @Test
    public void copyAndEdit() {
        SeatMap layout = SeatMap.fromString(SIMPLE_LAYOUT_STR);
        SeatMap layoutCopy = SeatMap.fromSeatMap(layout);
        assertEquals(layout.toString(), layoutCopy.toString());
        layout.getRows().get(0).set(0, HELD.getCode());
        assertNotEquals(layout.toString(), layoutCopy.toString());
    }

    private void print(String label, SeatMap seatMap) {
        System.err.println("\n" + label);
        System.err.println(seatMap);
    }
}