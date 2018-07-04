package github.dwstanle.tickets.service;

import github.dwstanle.tickets.StringListSeatMap;
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

import static github.dwstanle.tickets.util.SeatMapUtil.SIMPLE_LAYOUT_STR;
import static org.junit.Assert.assertEquals;
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
                .thenAnswer(invocationOnMock -> Optional.of(reservations.get(argument.getValue())));
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
        assertEquals(result, reservationService.findById(result.getId()).get());
    }

    @Test
    public void whenAvailable_thenHoldRequestedSeats() {

        ReservationRequest request = requestTemplate.toBuilder()
                .requestedSeat(new Seat(1, 0))
                .requestedSeat(new Seat(1, 1))
                .build();

        Reservation result = reservationService.holdSeats(request).get();
        assertEquals("test@email.com", result.getAccount().getEmail());

//        assertEquals(account, reservation.getAccount());
//        assertEquals(event, reservation.getEvent());
//        assertEquals(HELD, reservation.getStatus());
//        assertEquals(2, reservation.getSeats().size());
//
        assertEquals(result, reservationService.findById(result.getId()).get());

    }


}