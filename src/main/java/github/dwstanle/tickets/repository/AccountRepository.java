package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
//    List<Account> findByEmail(String email);
}
