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
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @ManyToOne
    private Venue venue;

    @OneToMany(mappedBy = "event")
    private Set<Reservation> reservations = new HashSet<>();

    //JPA construction only
    private Event() {
    }

    public static Event withId(Venue venue) {
        Long generatedId = UUID.randomUUID().getLeastSignificantBits();
        Event event = new Event("Event: " + generatedId, venue);
        event.setId(generatedId);
        return event;
    }

}