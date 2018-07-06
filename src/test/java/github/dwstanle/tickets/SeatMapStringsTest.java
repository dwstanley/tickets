package github.dwstanle.tickets;

import github.dwstanle.tickets.util.SeatMapStrings;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static github.dwstanle.tickets.util.SeatMapStrings.SIMPLE_LAYOUT_STR;
import static org.junit.Assert.assertEquals;

public class SeatMapStringsTest {

    private Path testFile;

    @Before
    public void setUp() throws Exception {
        testFile = Paths.get(ClassLoader.getSystemResource("SimpleLayout.input").toURI());
    }

    @Test
    public void fromPath() {
        List<List<String>> layout = SeatMapStrings.fromPath(testFile);
        assertEquals(SIMPLE_LAYOUT_STR, SeatMapStrings.toString(layout));
    }
}