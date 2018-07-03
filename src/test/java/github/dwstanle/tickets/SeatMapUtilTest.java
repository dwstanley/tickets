package github.dwstanle.tickets;

import github.dwstanle.tickets.util.SeatMapUtil;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static github.dwstanle.tickets.util.SeatMapUtil.SIMPLE_LAYOUT_STR;
import static org.junit.Assert.assertEquals;

public class SeatMapUtilTest {

    private Path testFile;

    @Before
    public void setUp() throws Exception {
        testFile = Paths.get(ClassLoader.getSystemResource("SimpleLayout.input").toURI());
    }

    @Test
    public void fromPath() {
        List<List<String>> layout = SeatMapUtil.fromPath(testFile);
        assertEquals(SIMPLE_LAYOUT_STR, SeatMapUtil.toString(layout));
    }
}