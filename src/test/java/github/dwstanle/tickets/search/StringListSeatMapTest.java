package github.dwstanle.tickets.search;

import github.dwstanle.tickets.StringListSeatMap;
import github.dwstanle.tickets.model.Seat;
import org.junit.Before;
import org.junit.Test;

import static github.dwstanle.tickets.SeatStatus.AVAILABLE;
import static github.dwstanle.tickets.SeatStatus.HELD;
import static org.junit.Assert.*;

public class StringListSeatMapTest {

    private int rowCount = 3;
    private int colCount = 6;
    private StringListSeatMap memento;

    @Before
    public void setUp() {
        memento = new StringListSeatMap(rowCount, colCount);
    }

    @Test
    public void stringListBookingMemento() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                assertEquals(AVAILABLE.getCode(), memento.getSeat(i, j));
            }
        }
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void stringListBookingMementoIndexOutOfBoundsRows() {
        memento.getSeat(4, 0);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void stringListBookingMementoIndexOutOfBoundsCols() {
        memento.getSeat(0, 7);
    }

    @Test
    public void asSeatMap() {
    }

    @Test
    public void setSeat() {

        memento.setSeat(new Seat(0, 0), HELD);
        assertEquals("H", memento.getSeat(0, 0));

        memento.setSeat(new Seat(0, 0), AVAILABLE);
        assertEquals("A", memento.getSeat(0, 0));

        memento.setSeat(new Seat(2, 4), HELD);
        assertEquals("H", memento.getSeat(2, 4));

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                if (i != 2 && j != 4) {
                    assertEquals(AVAILABLE.getCode(), memento.getSeat(i, j));
                }
            }
        }

    }

    @Test
    public void setSeatRowCol() {

        memento.setSeat(0, 0, "A");
        assertEquals("A", memento.getSeat(0, 0));

        memento.setSeat(0, 0, "H");
        assertEquals("H", memento.getSeat(0, 0));
    }

    @Test
    public void getNewSeats() {
    }
}