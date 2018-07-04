package github.dwstanle.tickets.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    private String eventName;

    @NonNull
    @ManyToOne
    private Venue venue;

    @OneToMany(mappedBy = "event")
    private Set<Reservation> reservations = new HashSet<>();

    //JPA construction only
    private Event() {
    }

}