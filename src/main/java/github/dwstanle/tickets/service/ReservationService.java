package github.dwstanle.tickets.service;

import github.dwstanle.tickets.model.ReservationRequest;
import github.dwstanle.tickets.model.ReservationResult;

public interface ReservationService {

    ReservationResult findAndHoldBestAvailable(ReservationRequest request);

    String reserveSeats(int reservationId, String accountId);

    ReservationResult holdSeats(ReservationRequest request);

    void cancelReservation(int reservationId);

}
