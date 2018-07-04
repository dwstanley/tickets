package github.dwstanle.tickets.search.basic;

import github.dwstanle.tickets.StringListSeatMap;
import github.dwstanle.tickets.model.VenueSeatMap;
import org.junit.Before;
import org.junit.Test;

import static github.dwstanle.tickets.SeatStatus.HELD;
import static github.dwstanle.tickets.SeatStatus.RESERVED;
import static github.dwstanle.tickets.util.SeatMapUtil.SIMPLE_LAYOUT_STR;
import static org.junit.Assert.assertEquals;

public class BasicSeatMapFactoryTest {

    public static String MODIFIED_LAYOUT_STR =
                    "S S S S S S S S\n" +
                    "A H A A A A A A\n" +
                    "A A H A A A A A\n" +
                    "A A A R A A A A\n" +
                    "A A A A H A A A\n" +
                    "A A A A A H A A\n" +
                    "A A A A A A R A";

    private StringListSeatMap bookingMemento;
    private BasicSeatMapFactory factory;

    @Before
    public void setUp() {
        factory = new BasicSeatMapFactory();
    }

    @Test
    public void of() {

        bookingMemento = factory.of(VenueSeatMap.SIMPLE.getSeats());
        assertEquals(SIMPLE_LAYOUT_STR, bookingMemento.toString());
//        System.err.println(bookingMemento);

        VenueSeatMap modified = VenueSeatMap.SIMPLE;
        modified.getSeats().get(1).set(1, HELD.getCode());
        modified.getSeats().get(2).set(2, HELD.getCode());
        modified.getSeats().get(3).set(3, RESERVED.getCode());
        modified.getSeats().get(4).set(4, HELD.getCode());
        modified.getSeats().get(5).set(5, HELD.getCode());
        modified.getSeats().get(6).set(6, RESERVED.getCode());

        bookingMemento = factory.of(modified.getSeats());
        assertEquals(MODIFIED_LAYOUT_STR, bookingMemento.toString());
//        System.err.println(bookingMemento);

    }

}