package github.dwstanle.tickets.model;

import github.dwstanle.tickets.SeatStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
public class Reservation {

    @Id
    @Builder.Default private final Long id = UUID.randomUUID().getLeastSignificantBits();

    @Builder.Default private final long timestamp = System.currentTimeMillis();

    @ManyToOne
    private final Account account;

    @ManyToOne
    private final Event event;

    @Embedded
    @Singular
    private final Set<Seat> seats;

    @Enumerated(EnumType.STRING)
    private final SeatStatus status;

}
