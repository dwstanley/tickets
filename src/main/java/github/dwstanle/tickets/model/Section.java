package github.dwstanle.tickets.model;

import github.dwstanle.tickets.SeatMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Section {
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String layout;

    @ManyToOne
    private Venue venue;

    //JPA construction only
    private Section() {
    }

    // always returns a copy so requesting objects can modify as necessary
    public SeatMap getCopyOfLayout() {
        return SeatMap.fromString(layout);
    }

}
