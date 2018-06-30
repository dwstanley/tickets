//package github.dwstanle.tickets.service;
//
//import github.dwstanle.tickets.exception.IllegalRequestException;
//import github.dwstanle.tickets.exception.ReservationNotFoundException;
//import github.dwstanle.tickets.model.Reservation;
//import github.dwstanle.tickets.model.SeatAssignmentRequest;
//import github.dwstanle.tickets.service.impl.BasicReservationService;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//public class ReservationServiceTest {
//
//    private ReservationService reservationService;
//    private SeatAssignmentRequest simpleRequest;
//
//
//    @Before
//    public void setUp() throws Exception {
//        reservationService = new BasicReservationService<>();
//    }
//
//    public void testHoldLength() {
////        Optional<SeatAssignment> seatAssignment = reservationService.findBest(simpleRequest);
////        Thread.sleep(TimeUnit.MINUTES);
////
////        // if timeout
////        //   if the seat is still available assign it
////        //   if the seat is no longer available through error message (maybe auto recalculate new seat)
////        reservationService.reserve(seatAssignment);
//
//    }
//
//    public void testFindAndHold() {
//
//    }
//
//    @Test (expected = ReservationNotFoundException.class)
//    public void testReserveNotHeld() {
//        reservationService.reserveSeats(0, "test@email.com");
//    }
//
//    @Test (expected = IllegalRequestException.class)
//    public void testReserveHeldForSomeoneElse() {
//        reservationService.reserveSeats(0, "test@email.com");
//    }
//
//    public void testReserveHeld() {
//        assertEquals("SUCCESS", reservationService.reserveSeats(0, "test@email.com"));
//    }
//
//}