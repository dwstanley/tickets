package github.dwstanle.tickets.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long reservationId) {
        super("could not find reservation '" + reservationId + "'.");
    }
}
