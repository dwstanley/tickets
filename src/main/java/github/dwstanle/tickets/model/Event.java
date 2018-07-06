package github.dwstanle.tickets.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
//@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue
//    @Builder.Default
//    private Long id = UUID.randomUUID().getLeastSignificantBits();
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

    public static Event withId(Venue venue) {
        Event event = new Event(venue);
        event.setId(UUID.randomUUID().getLeastSignificantBits());
        return event;
    }

}