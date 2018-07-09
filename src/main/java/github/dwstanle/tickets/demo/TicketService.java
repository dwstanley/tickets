package github.dwstanle.tickets.demo;

public interface TicketService {
    int numSeatsAvailable();
    SeatHold findAndHoldSeats(Integer numSeats, String customerEmail);
    String reserveSeats(Integer seatHoldId, String customerEmail);
}
