package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    private Account account;
    private Venue venue;
    private Section section;
    private Event event;

    @Before
    public void setUp() {

        this.section = entityManager.persist(new Section("S S S \n S S S"));
        this.account = entityManager.persist(new Account("foo@email.com"));
        this.venue = entityManager.persist(Venue.builder().section(section).build());
        this.event = entityManager.persist(new Event(venue));
        entityManager.persist(Reservation.builder()
                .account(account)
                .event(event)
                .seat(new Seat(0,0))
                .seat(new Seat(0,1))
                .build());
    }

    @Test
    public void findByAccountEmail() {
        Reservation reservation = reservationRepository.findByAccountEmail("foo@email.com").iterator().next();
        assertEquals("foo@email.com", reservation.getAccount().getEmail());
        assertEquals(event, reservation.getEvent());
        assertEquals(2, reservation.getSeats().size());
    }

    @Test
    public void findByEventId() {
        Reservation reservation = reservationRepository.findByEventId(event.getId()).iterator().next();
        assertEquals(event, reservation.getEvent());
    }
}