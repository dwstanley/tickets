package github.dwstanle.tickets.model;

import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.singleton;

public class EventTest {

    private Seat seat;
    private Reservation reservation;
    private Event event;

    @Before
    public void setUp() throws Exception {
        seat = new Seat(0, 0);
        reservation = Reservation.builder()
                .account(new Account("test@email.com"))
                .seats(singleton(seat))
                .build();
        event = new Event(new Venue(SeatMap.SIMPLE));
    }

    @Test
    public void getCurrentSeatMap() {
        VenueMemento seatMap = event.getCurrentSeatMap();
        // todo
        throw new UnsupportedOperationException();
    }
}