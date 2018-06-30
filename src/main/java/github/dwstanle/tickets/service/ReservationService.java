package github.dwstanle.tickets.service;

import github.dwstanle.tickets.model.SeatAssignmentRequest;
import github.dwstanle.tickets.model.SeatAssignmentResult;

public interface ReservationService {

    SeatAssignmentResult findAndHoldBestAvailable(SeatAssignmentRequest request);

    String reserveSeats(int reservationId, String accountId);

    SeatAssignmentResult holdSeats(SeatAssignmentRequest request);

}
