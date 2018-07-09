package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

@Transactional
public interface EventRepository extends JpaRepository<Event, Long> {
//    @Lock(LockModeType.PESSIMISTIC_READ)
    Event findByName(String name);
}
