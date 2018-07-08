package github.dwstanle.tickets.service;

import github.dwstanle.tickets.model.Account;
import github.dwstanle.tickets.model.Event;
import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.model.Section;
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
//    private final Account account;
    private String account; // email
    private final int numberOfSeats;
    private final double maxPrice;
//    private final Optional<Set<Seat>> requestedSeats;
    @Singular
    private final Set<Seat> requestedSeats;
    private final Section requestedSection;

}
