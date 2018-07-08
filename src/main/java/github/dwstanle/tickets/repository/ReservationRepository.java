package github.dwstanle.tickets.repository;


import github.dwstanle.tickets.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
//    Stream<Reservation> findByAccountEmail(String email);
//    Stream<Reservation> findByEventId(Long id);

//    List<Reservation> findByAccountEmail(String email);
    List<Reservation> findByAccount(String email);
    List<Reservation> findByEventId(Long id);
}