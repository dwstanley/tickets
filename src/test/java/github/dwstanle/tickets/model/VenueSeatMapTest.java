//package github.dwstanle.tickets.model;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//
//import static github.dwstanle.tickets.model.VenueSeatMap.SIMPLE_LAYOUT_STR;
//import static github.dwstanle.tickets.SeatStatus.HELD;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//
//public class VenueSeatMapTest {
//
//    private Path testFile;
//
//    @Before
//    public void setUp() throws Exception {
//        testFile = Paths.get(ClassLoader.getSystemResource("SimpleCsvLayout.csv").toURI());
//    }
//
//    @Test
//    public void seatMap() {
//    }
//
//    @Test
//    public void fromString() {
//        VenueSeatMap layout = VenueSeatMap.fromString(SIMPLE_LAYOUT_STR);
//        assertEquals(layout.toString(), SIMPLE_LAYOUT_STR);
//    }
//
//    @Test
//    public void fromPath() {
//        VenueSeatMap layout = VenueSeatMap.fromPath(testFile);
//        assertEquals(layout.toString(), SIMPLE_LAYOUT_STR);
//    }
//
//    @Test (expected = UnsupportedOperationException.class)
//    public void createAndAdd() {
//        VenueSeatMap layout = VenueSeatMap.fromString(SIMPLE_LAYOUT_STR);
//        layout.getSeats().get(0).add(HELD.getCode());
//    }
//
//    @Test (expected = UnsupportedOperationException.class)
//    public void createAndEditEntireRow() {
//        VenueSeatMap layout = VenueSeatMap.fromString(SIMPLE_LAYOUT_STR);
//        layout.getSeats().set(0, new ArrayList<>());
//    }
//
//    @Test (expected = UnsupportedOperationException.class)
//    public void createAndEdit() {
//        VenueSeatMap layout = VenueSeatMap.fromString(SIMPLE_LAYOUT_STR);
//        layout.getSeats().get(0).set(1, HELD.getCode());
//    }
//
//    @Test
//    public void copy() {
//        VenueSeatMap layout = VenueSeatMap.fromString(SIMPLE_LAYOUT_STR);
//        VenueSeatMap layoutCopy = VenueSeatMap.fromSeatMap(layout);
//        assertEquals(layout.toString(), layoutCopy.toString());
//    }
//
//    @Test
//    public void copyAndEdit() {
//        VenueSeatMap layout = VenueSeatMap.fromString(SIMPLE_LAYOUT_STR);
//        VenueSeatMap layoutCopy = VenueSeatMap.fromSeatMap(layout);
//        assertEquals(layout.toString(), layoutCopy.toString());
//        layout.getSeats().get(0).set(0, HELD.getCode());
//        assertNotEquals(layout.toString(), layoutCopy.toString());
//    }
//
//    private void print(String label, VenueSeatMap seatMap) {
//        System.err.println("\n" + label);
//        System.err.println(seatMap);
//    }
//}