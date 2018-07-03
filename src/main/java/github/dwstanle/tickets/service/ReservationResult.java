package github.dwstanle.tickets.service;

import github.dwstanle.tickets.model.Account;
import github.dwstanle.tickets.model.Reservation;
import github.dwstanle.tickets.model.Seat;
import lombok.Data;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Data
public class ReservationResult {

//    private final VenueMemento venueEndState;

    private final Optional<Reservation> reservation;

//    private final Optional<Set<Seat>> seats;
    private final Set<Seat> seats;

    private final Account account;

    private final boolean success;

}
