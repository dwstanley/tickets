package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
