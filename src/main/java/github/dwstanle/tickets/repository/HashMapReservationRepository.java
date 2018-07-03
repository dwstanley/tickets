package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.exception.ReservationNotFoundException;
import github.dwstanle.tickets.model.Reservation;
import github.dwstanle.tickets.repository.ReservationRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class HashMapReservationRepository implements ReservationRepository {
    private final Map<Integer, Reservation> reservations = new HashMap<>();

    public Reservation findById(int reservationId) throws ReservationNotFoundException {
        return reservations.get(reservationId);
    }

    public void save(Reservation reservation) {
        if (null != reservation && null != reservation.getId()) {
            reservations.put(reservation.getId(), reservation);
        }
    }

    public Stream<Reservation> findByEventId(Long eventId) {
        return reservations.values().stream()
                .filter(reservation -> Objects.equals(eventId, reservation.getEvent().getId()));
    }

    public void deleteById(int reservationId) {
        reservations.remove(reservationId);
    }
}
