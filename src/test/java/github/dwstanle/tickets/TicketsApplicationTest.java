package github.dwstanle.tickets;

import github.dwstanle.tickets.model.*;
import github.dwstanle.tickets.repository.AccountRepository;
import github.dwstanle.tickets.repository.EventRepository;
import github.dwstanle.tickets.repository.ReservationRepository;
import github.dwstanle.tickets.search.SimpleTicketSearchEngine;
import github.dwstanle.tickets.search.TicketSearchEngine;
import github.dwstanle.tickets.service.BasicReservationService;
import github.dwstanle.tickets.service.ReservationRequest;
import github.dwstanle.tickets.service.ReservationService;
import github.dwstanle.tickets.test.Slow;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static github.dwstanle.tickets.util.SeatMapStrings.SIMPLE_LAYOUT_STR;
import static org.junit.Assert.assertFalse;

/**
 * Integration test.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Category(Slow.class)
public class TicketsApplicationTest {

    @ComponentScan("github.dwstanle.tickets.service")
    static class BasicReservationServiceTestContextConfiguration {
        @Bean
        public ReservationService reservationService() {
            return new BasicReservationService();
        }
        @Bean
        public TicketSearchEngine ticketSearchEngine() {
            return new SimpleTicketSearchEngine();
        }
    }

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private EventRepository eventRepository;

    private ReservationRequest requestTemplate;

    @Before
    public void setUp() throws Exception {

        Section section = entityManager.persist(new Section(SIMPLE_LAYOUT_STR));
        Venue venue = entityManager.persist(Venue.builder().section(section).build());
        section.setVenue(venue);
        entityManager.persist(section);

        Event event = entityManager.persist(new Event("Test Event", venue));
        Account account = entityManager.persist(new Account("foo@gmail.com"));

        requestTemplate = ReservationRequest.builder()
                .account(account.getEmail())
                .event(event)
                .build();
    }

    @Test
    public void whenBackToBackReservations_thenDifferentSeatsFound() {

        ReservationRequest request = requestTemplate.toBuilder()
                .numberOfSeats(2)
                .build();

        Reservation result = reservationService.findAndHoldBestAvailable(request).get();

        result = reservationService.findAndHoldBestAvailable(request).get();
        result = reservationService.findAndHoldBestAvailable(request).get();
        result = reservationService.findAndHoldBestAvailable(request).get();

        // verify all reservations held
    }

    @Test
    public void whenTimeout_thenSameSeatsFound() {

        ReservationRequest request = requestTemplate.toBuilder()
                .numberOfSeats(2)
                .build();

        Reservation result = reservationService.findAndHoldBestAvailable(request).get();
//  thread.sleep for      BasicReservationService.HOLD_EXPIRATION_IN_MINUTES
        result = reservationService.findAndHoldBestAvailable(request).get();

//        verify only one reservation held

    }

    @Test
    public void whenSuccessfulHold_thenReservation() {

        ReservationRequest request = requestTemplate.toBuilder()
                .numberOfSeats(2)
                .build();

        Reservation result = reservationService.findAndHoldBestAvailable(request).get();
//        BasicReservationService.HOLD_EXPIRATION_IN_MINUTES
        result = reservationService.findAndHoldBestAvailable(request).get();

//        verify only one reservation held

    }

    @Test
    public void whenRequestTooLarge_thenNoResultFound() {
        ReservationRequest request = requestTemplate.toBuilder().numberOfSeats(500).build();
        assertFalse(reservationService.findAndHoldBestAvailable(request).isPresent());
    }


}