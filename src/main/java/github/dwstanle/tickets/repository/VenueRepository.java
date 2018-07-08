package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
