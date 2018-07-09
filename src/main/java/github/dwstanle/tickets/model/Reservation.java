package github.dwstanle.tickets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import github.dwstanle.tickets.SeatStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptySet;

@Entity
@Builder(toBuilder = true)
//@AllArgsConstructor(access = AccessLevel.PACKAGE)
//@NoArgsConstructor(access = AccessLevel.PACKAGE)
//@Setter(value = AccessLevel.PACKAGE)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    @Builder.Default private long timestamp = System.currentTimeMillis();

    @JsonIgnore
//    @ManyToOne
//    private Account account;
    private String account; // email

    @JsonIgnore
    @ManyToOne
    private Event event;

    @Singular
    @Embedded
    @ElementCollection
    private Set<Seat> seats;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

//    public Set<Seat> getSeats() {
//        return (null == seats) ? emptySet() : seats;
//    }
}
