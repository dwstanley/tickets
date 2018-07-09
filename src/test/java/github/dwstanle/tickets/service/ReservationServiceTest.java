package github.dwstanle.tickets.service;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.exception.IllegalRequestException;
import github.dwstanle.tickets.exception.ReservationNotFoundException;
import github.dwstanle.tickets.model.*;
import github.dwstanle.tickets.repository.ReservationRepository;
import github.dwstanle.tickets.search.TicketSearchEngine;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static github.dwstanle.tickets.SeatStatus.HELD;
import static github.dwstanle.tickets.SeatStatus.RESERVED;
import static github.dwstanle.tickets.model.Data.generateId;
import static github.dwstanle.tickets.util.SeatMapStrings.SIMPLE_LAYOUT_STR;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ReservationServiceTest {

    @TestConfiguration
    static class BasicReservationServiceTestContextConfiguration {
        @Bean
        public ReservationService reservationService() {
            return new BasicReservationService();
        }

        @Bean
        public TicketSearchEngine ticketSearchEngine() {
            return new TicketSearchEngine() {
                @Override
                public Optional<Set<Seat>> findBestAvailable(int requestedNumberOfSeats, SeatMap seatMap) {
                    return Optional.of(new HashSet<>(Arrays.asList(new Seat(1, 0), new Seat(1, 1))));
                }

                @Override
                public Optional<Set<Seat>> findFirstAvailable(int requestedNumberOfSeats, SeatMap seatMap) {
                    return Optional.of(new HashSet<>(Arrays.asList(new Seat(1, 0), new Seat(1, 1))));
                }
            };
        }
    }

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private ReservationRepository reservationRepository;

    private final ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
    private final ArgumentCaptor<Reservation> reservationArg = ArgumentCaptor.forClass(Reservation.class);

    private final Map<Long, Reservation> reservations = new HashMap<>();

    private ReservationRequest requestTemplate;

    @Before
    public void setUp() {

        Section section = new Section(SIMPLE_LAYOUT_STR);

        requestTemplate = ReservationRequest.builder()
                .requestedSection(section)
                .account("test@email.com")
                .event(Event.withId(Venue.builder().section(section).build()))
                .build();

        when(reservationRepository.save(reservationArg.capture()))
                .thenAnswer(invocationOnMock -> {
                    Reservation toSave = reservationArg.getValue().toBuilder().id(generateId()).build();
                    reservations.put(toSave.getId(), toSave);
                    return toSave;
                });

        when(reservationRepository.findById(argument.capture()))
                .thenAnswer(invocationOnMock -> Optional.ofNullable(reservations.get(argument.getValue())));
    }

    @Test
    public void whenAvailable_thenFindAndHoldBest() {

        ReservationRequest request = requestTemplate.toBuilder()
                .numberOfSeats(2)
                .build();

        Reservation result = reservationService.findAndHoldBestAvailable(request).get();
        assertEquals("test@email.com", result.getAccount());
        assertEquals(2, result.getSeats().size());
        assertTrue(result.getSeats().contains(new Seat(1, 0)));
        assertTrue(result.getSeats().contains(new Seat(1, 1)));
        assertEquals(HELD, result.getStatus());
        assertEquals(result, reservationService.findById(result.getId()).get());
    }

    @Test
    public void whenAvailable_thenHoldRequestedSeats() {

        ReservationRequest request = requestTemplate.toBuilder()
                .requestedSeat(new Seat(2, 2))
                .requestedSeat(new Seat(2, 3))
                .build();

        Reservation result = reservationService.holdSeats(request).get();
        assertEquals("test@email.com", result.getAccount());
        assertEquals(2, result.getSeats().size());
        assertTrue(result.getSeats().contains(new Seat(2, 2)));
        assertTrue(result.getSeats().contains(new Seat(2, 3)));
        assertEquals(HELD, result.getStatus());
        assertEquals(result, reservationService.findById(result.getId()).get());

    }

    @Test(expected = IllegalRequestException.class)
    public void whenRequestingNegativeNumberOfSeats_thenThrowError() {
        reservationService.findAndHoldBestAvailable(requestTemplate.toBuilder().numberOfSeats(-1).build());
    }

    @Test(expected = IllegalRequestException.class)
    public void whenRequestingNoSeats_thenThrowError() {
        reservationService.findAndHoldBestAvailable(requestTemplate.toBuilder().numberOfSeats(0).build());
    }

    @Test
    public void whenRequestingNoSection_thenThrowError() {
        ReservationRequest request = requestTemplate.toBuilder()
                .numberOfSeats(2)
                .requestedSection(null)
                .build();
        reservationService.findAndHoldBestAvailable(request).get();
    }

    @Test
    public void whenReservationHeld_thenReserve() {
        ReservationRequest request = requestTemplate.toBuilder().numberOfSeats(2).build();
        Reservation held = reservationService.findAndHoldBestAvailable(request).get();
        Reservation reserved = reservationService.reserveSeats(held.getId(), request.getAccount()).get();
        assertEquals("test@email.com", reserved.getAccount());
        assertEquals(2, reserved.getSeats().size());
        assertTrue(reserved.getSeats().contains(new Seat(1, 0)));
        assertTrue(reserved.getSeats().contains(new Seat(1, 1)));
        assertEquals(RESERVED, reserved.getStatus());
        assertEquals(reserved, reservationService.findById(reserved.getId()).get());
    }

    @Test(expected = ReservationNotFoundException.class)
    public void whenReservationNotHeld_thenThrowError() {
        reservationService.reserveSeats(0, "test@email.com");
    }

    @Test(expected = IllegalRequestException.class)
    public void whenReservationHeldBySomeoneElse_thenThrowError() {
        ReservationRequest request = requestTemplate.toBuilder().numberOfSeats(2).build();
        Reservation held = reservationService.findAndHoldBestAvailable(request).get();
        reservationService.reserveSeats(held.getId(), "wrongaccount@email.com");
    }

    @Test(expected = IllegalRequestException.class)
    public void whenReservingWithIllegalAccountIdentifier_thenThrowError() {
        ReservationRequest request = requestTemplate.toBuilder().numberOfSeats(2).build();
        Reservation held = reservationService.findAndHoldBestAvailable(request).get();
        reservationService.reserveSeats(held.getId(), null);
    }

    @Test
    public void whenHoldingNoSpecificSeatsDefined_thenNoAction() {
        reservationService.holdSeats(requestTemplate);
    }

    @Ignore("Feature not yet implemented")
    @Test(expected = IllegalRequestException.class)
    public void whenRequestingToHoldSeatsAlreadyHeld_thenThrowError() {
        ReservationRequest request = requestTemplate.toBuilder()
                .requestedSeat(new Seat(2, 2))
                .requestedSeat(new Seat(2, 3))
                .build();

        Optional<Reservation> firstReservation = reservationService.holdSeats(request);
        Optional<Reservation> secondReservation = reservationService.holdSeats(request);

    }

    @Ignore("Feature not yet implemented")
    @Test(expected = IllegalRequestException.class)
    public void whenRequestingToHoldSeatsAlreadyHeldBySomeoneElse_thenThrowError() {
        reservationService.holdSeats(requestTemplate.toBuilder()
                .account("demo@email.com").requestedSeat(new Seat(2, 2)).build());
        reservationService.holdSeats(requestTemplate.toBuilder()
                .account("fake123@email.com").requestedSeat(new Seat(2, 2)).build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenRequestingToHoldSeatsOutOfRange1_thenThrowError() {
        reservationService.holdSeats(requestTemplate.toBuilder().requestedSeat(new Seat(-1, 2)).build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenRequestingToHoldSeatsOutOfRange2_thenThrowError() {
        reservationService.holdSeats(requestTemplate.toBuilder().requestedSeat(new Seat(2, -1)).build());
    }

    @Test
    public void whenRequestingToHoldSeatsContainingObjects_thenThrowError() {
        Optional<Reservation> reservation = reservationService.holdSeats(
                requestTemplate.toBuilder().requestedSeat(new Seat(0, 0)).build());
        assertFalse(reservation.isPresent());
    }

    @Test
    public void whenReservationHeld_thenCancel() {
        // not implemented yet
    }

    @Test
    public void whenReservationReserved_thenCancel() {
        // not implemented yet
    }

    @Test
    public void whenReservationTimeout_thenCancelAutomatically() {
        // not implemented yet
    }

}