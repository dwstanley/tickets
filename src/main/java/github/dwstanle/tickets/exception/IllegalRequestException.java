package github.dwstanle.tickets.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class IllegalRequestException extends RuntimeException {
    public IllegalRequestException(String reason) {
        super("could not complete request because '" + reason + "'.");
    }
}
