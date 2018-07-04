package github.dwstanle.tickets.service;

import github.dwstanle.tickets.model.Reservation;

import java.util.Optional;

public interface ReservationService {

    Optional<Reservation> findAndHoldBestAvailable(ReservationRequest request);

    String reserveSeats(long reservationId, String accountId);

    Optional<Reservation> holdSeats(ReservationRequest request);

    void cancelReservation(long reservationId);

    Optional<Reservation> findById(long reservationId);
}
