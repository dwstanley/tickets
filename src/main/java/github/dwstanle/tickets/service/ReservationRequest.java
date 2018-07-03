package github.dwstanle.tickets.service;

import github.dwstanle.tickets.model.Account;
import github.dwstanle.tickets.model.Event;
import github.dwstanle.tickets.model.Seat;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Optional;
import java.util.Set;

@Data
@Builder(toBuilder=true)
public class ReservationRequest {

//    @NonNull
    private final Event event;
    private final Account account;
    private final int numberOfSeats;
//    private final Optional<Set<Seat>> requestedSeats;
    @Singular
    private final Set<Seat> requestedSeats;

}
