package github.dwstanle.tickets.service;

public interface TicketService {
    int numSeatsAvailable();
//    SeatHold findAndHoldSeats(int numSeats, String customerEmail);
    String reserveSeats(int seatHoldId, String customerEmail);
}
