package github.dwstanle.tickets.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CouldNotFindSeatsException extends RuntimeException {
    public CouldNotFindSeatsException(int requestedNumberOfSeats) {
        super("Could not calculate acceptable seat arrangement for'" + requestedNumberOfSeats
                + "' seats but there still might be enough seats to satisfy your group size. Please manually select desired seats.");
    }
}
