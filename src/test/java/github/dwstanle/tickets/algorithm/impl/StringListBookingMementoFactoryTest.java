package github.dwstanle.tickets.algorithm.impl;

import github.dwstanle.tickets.model.SeatMap;
import org.junit.Before;
import org.junit.Test;

import static github.dwstanle.tickets.model.SeatStatus.HELD;
import static github.dwstanle.tickets.model.SeatStatus.RESERVED;
import static org.junit.Assert.assertEquals;

public class StringListBookingMementoFactoryTest {

    public static String MODIFIED_LAYOUT_STR =
                    "S S S S S S S S\n" +
                    "A H A A A A A A\n" +
                    "A A H A A A A A\n" +
                    "A A A R A A A A\n" +
                    "A A A A H A A A\n" +
                    "A A A A A H A A\n" +
                    "A A A A A A R A";

    private StringListBookingMemento bookingMemento;
    private StringListBookingMementoFactory factory;

    @Before
    public void setUp() {
        factory = new StringListBookingMementoFactory();
    }

    @Test
    public void of() {

        bookingMemento = factory.of(SeatMap.SIMPLE);
        assertEquals(SeatMap.SIMPLE_LAYOUT_STR, bookingMemento.toString());
//        System.err.println(bookingMemento);

        SeatMap modified = SeatMap.SIMPLE;
        modified.getRows().get(1).set(1, HELD.getCode());
        modified.getRows().get(2).set(2, HELD.getCode());
        modified.getRows().get(3).set(3, RESERVED.getCode());
        modified.getRows().get(4).set(4, HELD.getCode());
        modified.getRows().get(5).set(5, HELD.getCode());
        modified.getRows().get(6).set(6, RESERVED.getCode());

        bookingMemento = factory.of(modified);
        assertEquals(MODIFIED_LAYOUT_STR, bookingMemento.toString());
//        System.err.println(bookingMemento);

    }

}