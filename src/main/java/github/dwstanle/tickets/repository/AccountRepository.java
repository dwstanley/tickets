package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
}
