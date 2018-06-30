package github.dwstanle.tickets.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String reservationId) {
        super("could not find reservation '" + reservationId + "'.");
    }
}
