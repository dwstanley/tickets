//package github.dwstanle.tickets.service.basic;
//
//import github.dwstanle.tickets.StringListSeatMap;
//import github.dwstanle.tickets.exception.IllegalRequestException;
//import github.dwstanle.tickets.exception.ReservationNotFoundException;
//import github.dwstanle.tickets.model.*;
//import github.dwstanle.tickets.search.SeatMapEvaluator;
//import github.dwstanle.tickets.search.SeatMapGenerator;
//import github.dwstanle.tickets.search.basic.BasicSeatMapFactory;
//import github.dwstanle.tickets.search.basic.BasicTicketSearchEngine;
//import github.dwstanle.tickets.service.ReservationRequest;
//import github.dwstanle.tickets.service.ReservationResult;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.Instant;
//
//import static github.dwstanle.tickets.SeatStatus.*;
//import static github.dwstanle.tickets.service.basic.BasicReservationService.HOLD_EXPIRATION_IN_MINUTES;
//import static github.dwstanle.tickets.util.SeatMapUtil.SIMPLE_LAYOUT_STR;
//import static java.util.Collections.singleton;
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
///**
// * Test class to ensure BasicReservationService is wiring the provided generator and evaluator correctly.
// */
//@RunWith(SpringRunner.class)
//public class BasicReservationServiceTest {
//
//    public static String AFTER_RESERVATIONS_VENUE =
//            "S S S S S S S S\n" +
//                    "R R R A A A A A\n" +
//                    "A A A A A A A A\n" +
//                    "A A A A A A A A\n" +
//                    "A A A A A A A A\n" +
//                    "A A A A A A A A\n" +
//                    "A A A A A A A A";
//
//    private Event event;
//    private Account account;
//    private ReservationRequest holdRequest;
//
//    private BasicReservationService<StringListSeatMap> reservationService;
//
//    private BasicSeatMapFactory factory;
//    private BasicTicketSearchEngine<StringListSeatMap> engine;
//    private StringListSeatMap memento;
//
//    @Mock
//    private SeatMapGenerator<StringListSeatMap> generator;
//
//    @Mock
//    private SeatMapEvaluator<StringListSeatMap> evaluator;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        this.account = new Account("test@email.com");
//        this.event = new Event(new Venue(SIMPLE_LAYOUT_STR));
//        this.factory = new BasicSeatMapFactory();
//        this.engine = new BasicTicketSearchEngine<>(generator, evaluator, factory);
//        this.reservationService = new BasicReservationService<>();
//        this.reservationService.setSearchEngine(engine);
//        this.memento = engine.copySeatMap(VenueSeatMap.SIMPLE.getSeats());
//        this.holdRequest = ReservationRequest.builder()
//                .account(account)
//                .event(event)
//                .requestedSeat(new Seat(1, 1))
//                .requestedSeat(new Seat(1, 2))
//                .build();
//        when(generator.generateAllAvailable(any(), any())).thenReturn(singleton(memento));
//        when(evaluator.findBest(any())).thenReturn(memento);
//    }
//
//    @Test
//    public void findAndHoldBestAvailable() {
//
//        ReservationRequest request = ReservationRequest.builder()
//                .account(account)
//                .event(event)
//                .numberOfSeats(2)
//                .build();
//
//        ReservationResult result = reservationService.findAndHoldBestAvailable(request);
//        assertEquals(account, result.getAccount());
//        assertTrue(result.isSuccess());
//
//        Reservation reservation = result.getReservation().get();
//        assertEquals(account, reservation.getAccount());
//        assertEquals(event, reservation.getEvent());
//        assertEquals(HELD, reservation.getStatus());
//        assertEquals(2, reservation.getSeats().size());
//
//        assertEquals(reservation, reservationService.findById(reservation.getId()));
//
//
//
//
////        ReservationRequest request2 = ReservationRequest.builder()
////                .account(account)
////                .event(event)
////                .numberOfSeats(-1)
////                .build();
////
////        ReservationRequest request3 = ReservationRequest.builder()
////                .account(account)
////                .event(event)
////                .numberOfSeats(0)
////                .build();
////
////        ReservationRequest request4 = ReservationRequest.builder()
////                .account(account)
////                .event(event)
////                .numberOfSeats(60)
////                .build();
////
////        ReservationRequest request5 = ReservationRequest.builder()
////                .account(account)
////                .event(event)
////                .requestedSeat(new Seat(1, 1))
////                .requestedSeat(new Seat(1, 2))
////                .build();
//
//    }
//
//    private void assertFailure(ReservationResult result) {
//        assertEquals(account, result.getAccount());
//        assertFalse(result.getReservation().isPresent());
//        assertFalse(result.isSuccess());
//    }
//
//    @Test
//    public void findAndHoldBestAvailableTooFew() {
//        ReservationRequest request = holdRequest.toBuilder().numberOfSeats(-1).build();
//        assertFailure(reservationService.findAndHoldBestAvailable(request));
//    }
//
//    @Test
//    public void findAndHoldBestAvailableNone() {
//        ReservationRequest request = holdRequest.toBuilder().numberOfSeats(0).build();
//        assertFailure(reservationService.findAndHoldBestAvailable(request));
//    }
//
//        @Test
//    public void testReserveSeatsHeld() {
//        ReservationResult holdResult = reservationService.holdSeats(holdRequest);
//        Reservation reservation = holdResult.getReservation().get();
//        assertEquals("SUCCESS", reservationService.reserveSeats(reservation.getId(), account.getEmail()));
//    }
//
//    @Test(expected = ReservationNotFoundException.class)
//    public void testReserveNotHeld() {
//        reservationService.reserveSeats(0, "test@email.com");
//    }
//
//    @Test(expected = IllegalRequestException.class)
//    public void testReserveHeldForSomeoneElse() {
//        ReservationResult holdResult = reservationService.holdSeats(holdRequest);
//        Reservation reservation = holdResult.getReservation().get();
//        reservationService.reserveSeats(reservation.getId(), "fake123@email.com");
//    }
//
//    @Test(expected = IllegalRequestException.class)
//    public void testReserveIllegalAccountId() {
//        ReservationResult holdResult = reservationService.holdSeats(holdRequest);
//        Reservation reservation = holdResult.getReservation().get();
//        reservationService.reserveSeats(reservation.getId(), null);
//    }
//
//    @Test
//    public void holdSeats() {
//
//        ReservationResult result = reservationService.holdSeats(holdRequest);
//        assertEquals(account, result.getAccount());
//        assertTrue(result.isSuccess());
//
//        Reservation reservation = result.getReservation().get();
//        assertEquals(account, reservation.getAccount());
//        assertEquals(event, reservation.getEvent());
//        assertEquals(HELD, reservation.getStatus());
//        assertEquals(2, reservation.getSeats().size());
//
//        assertEquals(reservation, reservationService.findById(reservation.getId()));
//
//    }
//
//    @Test
//    public void holdSeatsNoSeatsDefined() {
//        ReservationRequest request = ReservationRequest.builder()
//                .account(account)
//                .event(event)
//                .build();
//        assertFailure(reservationService.holdSeats(request));
//    }
//
//    @Test
//    public void holdSeatsAlreadyHeld() {
//        reservationService.holdSeats(holdRequest);
//        assertFailure(reservationService.holdSeats(holdRequest));
//    }
//
//    @Test
//    public void holdSeatsAlreadyHeldBySomeoneElse() {
//        ReservationRequest duplicateRequest = holdRequest.toBuilder()
//                .account(new Account("fake123@email.com"))
//                .build();
//        reservationService.holdSeats(holdRequest);
//        assertFailure(reservationService.holdSeats(duplicateRequest));
//    }
//
//    @Test
//    public void holdSeatsOutOfRange() {
//
//        ReservationRequest request = holdRequest.toBuilder()
//                .requestedSeat(new Seat(10, 10))
//                .build();
//
//        ReservationResult result = reservationService.holdSeats(request);
//        assertFalse(result.getReservation().isPresent());
//        assertFalse(result.isSuccess());
//
//        assertFalse(reservationService.holdSeats(
//                holdRequest.toBuilder()
//                        .requestedSeat(new Seat(7, 5))
//                        .build()).isSuccess());
//
//        assertFalse(reservationService.holdSeats(
//                holdRequest.toBuilder()
//                        .requestedSeat(new Seat(5, 8))
//                        .build()).isSuccess());
//
//        assertFalse(reservationService.holdSeats(
//                holdRequest.toBuilder()
//                        .requestedSeat(new Seat(0, 0))
//                        .build()).isSuccess());
//
//        assertFalse(reservationService.holdSeats(
//                holdRequest.toBuilder()
//                        .requestedSeat(new Seat(-1, 2))
//                        .build()).isSuccess());
//
//        assertFalse(reservationService.holdSeats(
//                holdRequest.toBuilder()
//                        .requestedSeat(new Seat(2, -1))
//                        .build()).isSuccess());
//
//    }
//
//    @Test
//    public void holdSeatsNotAllowed() {
//        ReservationRequest request = holdRequest.toBuilder()
//                .requestedSeat(new Seat(0, 0))
//                .build();
//        assertFailure(reservationService.holdSeats(request));
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void holdSeatsIllegalAccount() {
//        reservationService.holdSeats(holdRequest.toBuilder().account(null).build());
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void holdSeatsNullRequest() {
//        reservationService.holdSeats(null);
//    }
//
//    @Test
//    public void cancelReservation() {
////        reservationService.cancelReservation(getReservation(0, 0).getId());
////        reservationService.holdSeats();
////        reservationService.reserveSeats(reservation, );
////        reservationService.cancelReservation(reservation.getId());
////        reservationService.findReservation(reservation.getId());
//    }
//
//
//    @Test
//    public void isExpired() {
//        Instant expired = Instant.now().minusSeconds((60 * HOLD_EXPIRATION_IN_MINUTES) + 1);
//        Reservation reservation = Reservation.builder().status(HELD).timestamp(expired.toEpochMilli()).build();
//        assertTrue(reservationService.isExpired(reservation));
//        assertFalse(reservationService.isExpired(Reservation.builder().status(HELD).build()));
//    }
//
////    @Test
////    public void addToMementoReservation() {
////        reservationService.addToMemento(memento, getReservation(new Seat(1, 1), new Seat(2, 2)));
////        assertEquals(RESERVED, memento.getSeatStatus(new Seat(1, 1)));
////        assertEquals(RESERVED, memento.getSeatStatus(new Seat(2, 2)));
////    }
////
////    // todo - do we rollback reservations if later requests fail?
////    @Test(expected = IndexOutOfBoundsException.class)
////    public void addToMementoReservationSecondOutOfBounds() {
////        reservationService.addToMemento(memento, getReservation(new Seat(9, 9), new Seat(10, 10)));
////    }
////
////    @Test
////    public void createSeatMapFromReservations() {
////        reservationService.doReserveSeats(getReservation(1, 0));
////        reservationService.doReserveSeats(getReservation(1, 1));
////        reservationService.doReserveSeats(getReservation(1, 2));
////
////        // should have first three seats reserved
////        StringListSeatMap venueState = reservationService.createSeatMapFromReservations(event);
////        assertEquals(RESERVED, venueState.getSeatStatus(new Seat(1, 0)));
////        assertEquals(RESERVED, venueState.getSeatStatus(new Seat(1, 1)));
////        assertEquals(RESERVED, venueState.getSeatStatus(new Seat(1, 2)));
////        assertEquals(AFTER_RESERVATIONS_VENUE, venueState.toString());
////    }
//
//    @Test
//    public void addToMementoNotHeld() {
//        reservationService.addToMemento(memento, new Seat(1, 1), RESERVED);
//        assertEquals(RESERVED, memento.getSeatStatus(new Seat(1, 1)));
//    }
//
//    @Test
//    public void addToMementoHeld() {
//        memento.setSeat(new Seat(1, 1), HELD);
//        reservationService.addToMemento(memento, new Seat(1, 1), RESERVED);
//        assertEquals(RESERVED, memento.getSeatStatus(new Seat(1, 1)));
//    }
//
//    @Test(expected = UnsupportedOperationException.class)
//    public void addToMementoAlreadyReservedSeat() {
//        memento.setSeat(new Seat(1, 1), RESERVED);
//        reservationService.addToMemento(memento, new Seat(1, 1), RESERVED);
//    }
//
//    @Test(expected = UnsupportedOperationException.class)
//    public void addToMementoStage() {
//        reservationService.addToMemento(memento, new Seat(0, 0), RESERVED);
//    }
//
//    @Test(expected = UnsupportedOperationException.class)
//    public void addToMementoObstacle() {
//        memento.setSeat(new Seat(1, 1), OBSTACLE);
//        reservationService.addToMemento(memento, new Seat(1, 1), RESERVED);
//    }
//
//    @Test(expected = IndexOutOfBoundsException.class)
//    public void addToMementoOutOfBounds() {
//        reservationService.addToMemento(memento, new Seat(1, 10), HELD);
//    }
//
//}