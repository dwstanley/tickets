package github.dwstanle.tickets.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Builder
//@Entity
public class Reservation {

//    @Id
//    @GeneratedValue
    private final Integer id;

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

    private final String status;

//    public Reservation(Account account, Collection<SeatAssignment> seatAssignments) {
//        this.account = Objects.requireNonNull(account);
//        this.seatAssignments = new HashSet<>(Objects.requireNonNull(seatAssignments));
//    }

    // JPA only
//    private Reservation() {
//
//    }

//    public Set<SeatAssignment> getSeatAssignments() {
//        return Collections.unmodifiableSet(seatAssignments);
//    }
    public Set<Seat> getSeatAssignments() {
        return Collections.unmodifiableSet(seats);
    }
}
