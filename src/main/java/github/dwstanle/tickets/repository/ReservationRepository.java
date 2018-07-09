package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByAccount(String email);
    List<Reservation> findByEventId(Long id);
}