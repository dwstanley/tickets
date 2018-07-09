package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SectionRepository extends JpaRepository<Section, Long> {
}
