package github.dwstanle.tickets.repository;


import github.dwstanle.tickets.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Collection<Reservation> findByAccountEmail(String email);
}
