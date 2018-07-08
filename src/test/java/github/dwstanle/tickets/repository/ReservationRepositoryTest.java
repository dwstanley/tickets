package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static github.dwstanle.tickets.model.Data.generateId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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
        this.event = entityManager.persist(new Event("Event Test", venue));
        entityManager.persist(Reservation.builder()
                .account(account.getEmail())
                .event(event)
                .seat(new Seat(0, 0))
                .seat(new Seat(0, 1))
                .build());
    }

    @Test
    public void findByAccountEmail() {
        Reservation reservation = reservationRepository.findByAccount("foo@email.com").iterator().next();
        assertEquals("foo@email.com", reservation.getAccount());
        assertEquals(event, reservation.getEvent());
        assertEquals(2, reservation.getSeats().size());
    }

    @Test
    public void whenSearchByEventId_thenReturnReservation() {
        Reservation reservation = reservationRepository.findByEventId(event.getId()).iterator().next();
        assertEquals(event, reservation.getEvent());
    }

    @Test
    public void whenEventIdIsNull_thenNoReservationsFound() {
        assertEquals(0, reservationRepository.findByEventId(null).stream().count());
    }

    @Test
    public void whenEventIdIsUnknown_thenNoReservationsFound() {
        assertEquals(0, reservationRepository.findByEventId(700L).stream().count());
    }

    @Test
    public void whenSave_thenReservationCreated() {
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .account(account.getEmail())
                .event(event)
                .seat(new Seat(0, 0))
                .seat(new Seat(0, 1))
                .build());
        assertNotNull(reservation);
        assertNotNull(reservation.getId());
    }

    @Test
    public void whenAlreadySaved_thenReservationCreated() {
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .id(generateId())
                .account(account.getEmail())
                .event(event)
                .seat(new Seat(0, 0))
                .seat(new Seat(0, 1))
                .build());
        assertNotNull(reservation);
        assertNotNull(reservation.getId());
    }

    @Test
    public void whenDelete_thenReservationIsRemoved() {
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .id(generateId())
                .account(account.getEmail())
                .event(event)
                .seat(new Seat(0, 0))
                .seat(new Seat(0, 1))
                .build());
        reservationRepository.deleteById(reservation.getId());
        assertFalse(reservationRepository.findById(reservation.getId()).isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDeleteUnknown_thenReservationIsRemoved() {
        reservationRepository.deleteById(0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDeleteWithNull_thenIllegalArgumentException() {
        reservationRepository.deleteById(null);
    }
}