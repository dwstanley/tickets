package github.dwstanle.tickets.service;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.model.Event;
import github.dwstanle.tickets.model.Reservation;
import github.dwstanle.tickets.model.Section;

import java.util.Optional;

public interface ReservationService {

    Optional<Reservation> findAndHoldBestAvailable(ReservationRequest request);

    Optional<Reservation> reserveSeats(long reservationId, String accountEmail);

    Optional<Reservation> holdSeats(ReservationRequest request);

    void cancelReservation(long reservationId);

    Optional<Reservation> findById(long reservationId);

    int findNumberOfSeatsAvailable(Event event);

    SeatMap getUpdatedSeatMap(Event event, long sectionId);
}
