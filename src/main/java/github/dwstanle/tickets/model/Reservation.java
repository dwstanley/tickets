package github.dwstanle.tickets.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
public class Reservation {

//    @Id
//    @GeneratedValue
//    private final Integer id;
    @Builder.Default private Integer id = UUID.randomUUID().hashCode();

    @Builder.Default private long timestamp = System.currentTimeMillis();

//    @JsonIgnore
//    @ManyToOne
    private final Account account;

//    @JsonIgnore
//    @ManyToOne
    private final Event event;
//
//    private SeatAssignment seatAssignment;

//    private final Set<SeatAssignment> seatAssignments;
    private final Set<Seat> seats;

    private final SeatStatus status;



}
