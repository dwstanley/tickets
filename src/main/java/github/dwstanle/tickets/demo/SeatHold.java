package github.dwstanle.tickets.demo;

import github.dwstanle.tickets.SeatStatus;
import github.dwstanle.tickets.model.Reservation;
import github.dwstanle.tickets.model.Seat;
import lombok.Getter;

import java.time.Duration;
import java.util.Set;

import static java.time.temporal.ChronoUnit.SECONDS;

@Getter
public class SeatHold {

    private final Long reservationId;
    private final String accountEmail;
    private final Set<Seat> seats;
    private final SeatStatus status;
    private final long expiration;

    public SeatHold(Reservation reservation, int expirationInSecs) {
        this.reservationId = reservation.getId();
        this.accountEmail = reservation.getAccount();
        this.seats = reservation.getSeats();
        this.status = reservation.getStatus();
        this.expiration = reservation.getTimestamp() + Duration.of(expirationInSecs, SECONDS).toMillis();
    }

}
