package github.dwstanle.tickets.model;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SeatMapTest {

    private Path testFile;

    private static String simpleLayoutString =
            "S S S S S S S S\n" +
            "A A A A A A A A\n" +
            "A A A A A A A A\n" +
            "A A A A A A A A\n" +
            "A A A A A A A A\n" +
            "A A A A A A A A\n" +
            "A A A A A A A A";

    @Before
    public void setUp() throws Exception {
        testFile = Paths.get(ClassLoader.getSystemResource("SimpleLayout.csv").toURI());
    }

    @Test
    public void fromString() {
        SeatMap layout = SeatMap.fromString(SeatMap.SIMPLE_LAYOUT_STR);
        assertEquals(layout.toString(), simpleLayoutString);
    }

    @Test
    public void fromPath() {
        SeatMap layout = SeatMap.fromPath(testFile);
        assertEquals(layout.toString(), simpleLayoutString);
    }
}