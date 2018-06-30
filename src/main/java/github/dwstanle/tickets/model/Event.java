package github.dwstanle.tickets.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    private String eventName;

    @NonNull
    private Venue venue;

    private Set<Reservation> reservations = new HashSet<>();

    public VenueMemento getCurrentSeatMap() {
        VenueMemento seatMap = VenueMemento.of(venue);
        applyReservations(seatMap);
//        applyHoldings(seatMap);
        return seatMap;
    }

    private void applyReservations(VenueMemento seatMap) {
        reservations.stream()
                .flatMap(reservation -> reservation.getSeatAssignments().stream())
                .forEach(seat -> addReservation(seatMap, seat));
    }

    private void addReservation(VenueMemento seatMap, Seat seat) {
        seatMap.put(seat.getRow(), seat.getCol(), SeatAssignment.RESERVED);
    }
}