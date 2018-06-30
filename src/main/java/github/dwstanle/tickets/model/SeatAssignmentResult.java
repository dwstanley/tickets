package github.dwstanle.tickets.model;

import lombok.Data;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Data
public class SeatAssignmentResult {

//    private final VenueMemento venueEndState;

    private final Optional<Set<Seat>> seats;

    private final Account account;

    private final boolean success;

}
