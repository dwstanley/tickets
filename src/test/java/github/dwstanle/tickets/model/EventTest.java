package github.dwstanle.tickets.model;

import org.junit.Before;

import static github.dwstanle.tickets.util.SeatMapStrings.SIMPLE_LAYOUT_STR;
import static java.util.Collections.singleton;

public class EventTest {

    private Seat seat;
    private Reservation reservation;
    private Event event;

    @Before
    public void setUp() throws Exception {
        seat = new Seat(0, 0);
        reservation = Reservation.builder()
                .account("test@email.com")
                .seats(singleton(seat))
                .build();
        event = new Event("Event Test", Venue.builder().section(new Section(SIMPLE_LAYOUT_STR)).build());
    }

}