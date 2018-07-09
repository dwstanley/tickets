package github.dwstanle.tickets.service;

import github.dwstanle.tickets.demo.SeatHold;

public interface TicketService {
    int numSeatsAvailable();
    SeatHold findAndHoldSeats(int numSeats, String customerEmail);
    String reserveSeats(int seatHoldId, String customerEmail);
}
