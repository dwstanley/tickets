package github.dwstanle.tickets.service.impl;

import github.dwstanle.tickets.algorithm.impl.BasicBookingMemento;
import github.dwstanle.tickets.algorithm.impl.BasicSeatFinderEngine;
import github.dwstanle.tickets.exception.IllegalRequestException;
import github.dwstanle.tickets.exception.ReservationNotFoundException;
import github.dwstanle.tickets.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class to ensure BasicReservationService is wiring the provided generator and evaluator correctly.
 */
public class BasicReservationServiceTest {

    private Event event;
    private Account account;
    private BasicReservationService<BasicBookingMemento> reservationService;

    @Mock
    BasicSeatFinderEngine<BasicBookingMemento> engine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.event = new Event(new Venue(SeatMap.SIMPLE));
//        BasicSeatFinderEngine<BasicBookingMemento> engine = new BasicSeatFinderEngine<>()
        this.reservationService = new BasicReservationService<>(engine);
    }

    @Test
    public void findAndHoldBestAvailable() {

    }

    @Test
    public void createVenueStateFromReservations() {
        reservationService.doReserveSeats(getReservation(0, 0));
        reservationService.doReserveSeats(getReservation(0, 1));
        reservationService.doReserveSeats(getReservation(0, 2));

        // should have first three seats reserved
        BasicBookingMemento venueState = reservationService.createMementoFromReservations(event);
//        vm.toString()

        // should have no seats reserved
        BasicBookingMemento venueState2 = reservationService.createMementoFromReservations(mock(Event.class));
    }

    @Test (expected = ReservationNotFoundException.class)
    public void testReserveNotHeld() {
        reservationService.reserveSeats(0, "test@email.com");
    }

    @Test (expected = IllegalRequestException.class)
    public void testReserveHeldForSomeoneElse() {
        reservationService.reserveSeats(0, "test@email.com");
    }

    public void testReserveHeld() {
        assertEquals("SUCCESS", reservationService.reserveSeats(0, "test@email.com"));
    }

    private Reservation getReservation(int row, int col) {
        return Reservation.builder()
                .id(0)
                .account(account)
                .event(event)
                .status("R")
                .seats(singleton(new Seat(row, col)))
                .build();
    }

}