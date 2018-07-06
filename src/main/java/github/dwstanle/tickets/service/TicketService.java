package github.dwstanle.tickets.service;

public interface TicketService {

    public static class SeatHold {

    }

    int numSeatsAvailable();
    SeatHold findAndHoldSeats(int numSeats, String customerEmail);
    String reserveSeats(int seatHoldId, String customerEmail);
}
