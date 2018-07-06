package github.dwstanle.tickets.service;

import github.dwstanle.tickets.StringListSeatMap;
import github.dwstanle.tickets.exception.IllegalRequestException;
import github.dwstanle.tickets.exception.ReservationNotFoundException;
import github.dwstanle.tickets.model.*;
import github.dwstanle.tickets.repository.ReservationRepository;
import github.dwstanle.tickets.search.TicketSearchEngine;
import github.dwstanle.tickets.service.impl.BasicReservationService;
import org.junit.Before;
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
import static github.dwstanle.tickets.util.SeatMapUtil.SIMPLE_LAYOUT_STR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ReservationServiceTest {

    @TestConfiguration
    static class BasicReservationServiceTestContextConfiguration {

        @Bean
        public ReservationService reservationService() {
            BasicReservationService<StringListSeatMap> reservationService = new BasicReservationService<>();
            reservationService.setSearchEngine(new TicketSearchEngine<StringListSeatMap>() {
                @Override
                public Optional<Set<Seat>> findBestAvailable(ReservationRequest request) {
                    return Optional.of(new HashSet<>(Arrays.asList(new Seat(1, 0), new Seat(1, 1))));
                }

                @Override
                public StringListSeatMap copySeatMap(List<List<String>> origin) {
                    return new StringListSeatMap(origin);
                }
            });
            return reservationService;
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

        requestTemplate = ReservationRequest.builder()
                .account(new Account("test@email.com"))
                .event(new Event(new Venue(SIMPLE_LAYOUT_STR)))
                .build();

        when(reservationRepository.save(reservationArg.capture()))
                .thenAnswer(invocationOnMock -> {
                    reservations.put(reservationArg.getValue().getId(), reservationArg.getValue());
                    return reservationArg.getValue();
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
        assertEquals("test@email.com", result.getAccount().getEmail());
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
        assertEquals("test@email.com", result.getAccount().getEmail());
        assertEquals(2, result.getSeats().size());
        assertTrue(result.getSeats().contains(new Seat(2, 2)));
        assertTrue(result.getSeats().contains(new Seat(2, 3)));
        assertEquals(HELD, result.getStatus());
        assertEquals(result, reservationService.findById(result.getId()).get());

    }

    @Test
    public void whenRequestingTooFew_thenNoReservation() {

        ReservationRequest request = requestTemplate.toBuilder()
                .numberOfSeats(-1)
                .build();

        assertFalse(reservationService.findAndHoldBestAvailable(request).isPresent());

        request = requestTemplate.toBuilder()
                .numberOfSeats(0)
                .build();

        assertFalse(reservationService.findAndHoldBestAvailable(request).isPresent());

    }

    @Test
        public void whenReservationHeld_thenReserve() {
        ReservationRequest request = requestTemplate.toBuilder().numberOfSeats(2).build();
        Reservation held = reservationService.findAndHoldBestAvailable(request).get();
        Reservation reserved = reservationService.reserveSeats(held.getId(), request.getAccount().getEmail()).get();
        assertEquals("test@email.com", reserved.getAccount().getEmail());
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


}