package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;


    @Test
    public void findByEmail() {
        entityManager.persist(new Account("foo@email.com"));
        Optional<Account> account = accountRepository.findByEmail("foo@email.com");
        assertEquals("foo@email.com", account.get().getEmail());
        assertNotNull(account.get().getId());
    }

}