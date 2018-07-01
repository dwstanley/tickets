package github.dwstanle.tickets.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Integer reservationId) {
        super("could not find reservation '" + reservationId + "'.");
    }
}
