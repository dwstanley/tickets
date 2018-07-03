package github.dwstanle.tickets.service;

public interface ReservationService {

    ReservationResult findAndHoldBestAvailable(ReservationRequest request);

    String reserveSeats(int reservationId, String accountId);

    ReservationResult holdSeats(ReservationRequest request);

    void cancelReservation(int reservationId);

}
