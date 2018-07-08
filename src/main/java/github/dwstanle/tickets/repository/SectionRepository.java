package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
