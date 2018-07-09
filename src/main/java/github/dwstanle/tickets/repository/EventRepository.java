package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByName(String name);
}
