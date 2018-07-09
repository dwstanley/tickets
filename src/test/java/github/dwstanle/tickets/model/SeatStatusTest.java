package github.dwstanle.tickets.model;

import github.dwstanle.tickets.SeatStatus;
import org.junit.Test;

import static github.dwstanle.tickets.SeatStatus.*;
import static org.junit.Assert.assertEquals;

public class SeatStatusTest {

    @Test
    public void ofCode() {
        assertEquals(HELD, SeatStatus.ofCode("H"));
        assertEquals(RESERVED, SeatStatus.ofCode("R"));
        assertEquals(OBSTACLE, SeatStatus.ofCode("X"));
        assertEquals(AVAILABLE, SeatStatus.ofCode("A"));
        assertEquals(STAGE, SeatStatus.ofCode("S"));
    }

    @Test
    public void getCode() {
        assertEquals("H", HELD.getCode());
        assertEquals("R", RESERVED.getCode());
        assertEquals("X", OBSTACLE.getCode());
        assertEquals("A", AVAILABLE.getCode());
        assertEquals("S", STAGE.getCode());
    }

}