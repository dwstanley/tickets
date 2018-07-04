//package github.dwstanle.tickets.repository.basic;
//
//import github.dwstanle.tickets.model.Event;
//import github.dwstanle.tickets.model.Reservation;
//import github.dwstanle.tickets.SeatStatus;
//import github.dwstanle.tickets.repository.HashMapReservationRepository;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.when;
//
//public class HashMapReservationRepositoryTest {
//
//    private HashMapReservationRepository reservationRepository;
//    private Reservation reservation;
//
//    @Mock
//    private Event event;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        when(event.getId()).thenReturn(0L);
//        this.reservation = Reservation.builder().build();
//        this.reservationRepository = new HashMapReservationRepository();
//    }
//
//    @Test
//    public void findById() {
//        reservationRepository.save(reservation);
//        assertEquals(reservation, reservationRepository.findById(reservation.getId()));
//    }
//
//    @Test
//    public void findByIdUnknown() {
//        assertNull(reservationRepository.findById(reservation.getId()));
//    }
//
//    @Test
//    public void save() {
//        reservationRepository.save(null);
//        reservationRepository.save(reservation);
//        assertEquals(reservation, reservationRepository.findById(reservation.getId()));
//
//        Reservation update = reservation.toBuilder().status(SeatStatus.HELD).build();
//        reservationRepository.save(update);
//        assertEquals(update, reservationRepository.findById(reservation.getId()));
//        assertEquals(update, reservationRepository.findById(update.getId()));
//    }
//
//    @Test
//    public void delete() {
//        reservationRepository.deleteById(reservation.getId());
//        reservationRepository.save(reservation);
//        reservationRepository.deleteById(reservation.getId());
//        assertNull(reservationRepository.findById(reservation.getId()));
//    }
//
//    @Test
//    public void findByEventId() {
//        reservationRepository.save(Reservation.builder().event(event).build());
//        reservationRepository.save(Reservation.builder().event(event).build());
//        reservationRepository.save(Reservation.builder().event(event).build());
//        assertEquals(3, reservationRepository.findByEventId(0L).count());
//    }
//
//    @Test
//    public void findByEventIdNull() {
//        reservationRepository.save(Reservation.builder().event(event).build());
//        reservationRepository.save(Reservation.builder().event(event).build());
//        assertEquals(0, reservationRepository.findByEventId(null).count());
//    }
//
//    @Test
//    public void findByEventIdUnknown() {
//        reservationRepository.save(Reservation.builder().event(event).build());
//        reservationRepository.save(Reservation.builder().event(event).build());
//        assertEquals(0, reservationRepository.findByEventId(700L).count());
//    }
//}