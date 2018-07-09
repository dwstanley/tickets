package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface VenueRepository extends JpaRepository<Venue, Long> {
}
