package github.dwstanle.tickets.model;

import github.dwstanle.tickets.SeatStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
public class Reservation {

    @Builder.Default private final Integer id = UUID.randomUUID().hashCode();

    @Builder.Default private final long timestamp = System.currentTimeMillis();

    private final Account account;

    private final Event event;

    private final Set<Seat> seats;

    private final SeatStatus status;


}
