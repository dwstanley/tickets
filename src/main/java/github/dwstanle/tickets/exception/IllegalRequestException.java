package github.dwstanle.tickets.exception;

public class IllegalRequestException extends RuntimeException {
    public IllegalRequestException(String reason) {
        super("could not complete request because '" + reason + "'.");
    }
}
