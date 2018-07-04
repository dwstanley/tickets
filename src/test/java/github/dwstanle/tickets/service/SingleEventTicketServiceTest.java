package github.dwstanle.tickets.service;

import github.dwstanle.tickets.model.Event;
import github.dwstanle.tickets.model.Venue;
import github.dwstanle.tickets.model.VenueSeatMap;
import github.dwstanle.tickets.service.impl.SingleEventTicketService;
import org.junit.Before;
import org.junit.Test;

import static github.dwstanle.tickets.util.SeatMapUtil.SIMPLE_LAYOUT_STR;
import static org.junit.Assert.assertEquals;

public class SingleEventTicketServiceTest {

    private SingleEventTicketService service;
    private Event event;

    @Before
    public void setUp() throws Exception {
        event = new Event(new Venue(SIMPLE_LAYOUT_STR));
        service = new SingleEventTicketService(event);
    }

    @Test
    public void numSeatsAvailable() {
        assertEquals(48, service.numSeatsAvailable());
    }

    @Test
    public void reserveSeats() {
    }
}