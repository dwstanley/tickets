package github.dwstanle.tickets.model;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;
import java.util.Set;

@Data
@Builder(toBuilder=true)
public class SeatAssignmentRequest {

//    @NonNull
    private final Event event;
    private final Account account;
    private final int numberOfSeats;
    private final Optional<Set<Seat>> requestedSeats;

}
