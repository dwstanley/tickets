package github.dwstanle.tickets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import github.dwstanle.tickets.SeatStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
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

}
