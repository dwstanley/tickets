package github.dwstanle.tickets.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Venue {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String layout;

    //JPA construction only
    private Venue() {
    }

    public VenueSeatMap getLayout() {
        return VenueSeatMap.fromString(layout);
    }

}