package github.dwstanle.tickets.model;

import org.junit.Before;
import org.junit.Test;

import static github.dwstanle.tickets.model.Data.generateId;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReservationTest {

    private Seat seat;
    private Reservation reservation;

    @Before
    public void setUp() {
        seat = new Seat(0, 0);
        reservation = Reservation.builder()
                .id(generateId())
                .account("test@email.com")
                .seats(singleton(seat))
                .build();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableSeatAssignments() {
        reservation.getSeats().clear();
    }

    @Test
    public void testGetSeatAssignments() {
        assertEquals(1, reservation.getSeats().size());
        assertTrue(reservation.getSeats().contains(seat));
    }

    @Test
    public void testIdDoesNotChange() {
        assertEquals(reservation.getId(), reservation.toBuilder().build().getId());
    }
}