package github.dwstanle.tickets;

import github.dwstanle.tickets.model.Account;
import github.dwstanle.tickets.model.Reservation;
import github.dwstanle.tickets.repository.AccountRepository;
import github.dwstanle.tickets.repository.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class TicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketsApplication.class, args);
	}

//	@Bean
//	CommandLineRunner init(AccountRepository accountRepository,
//						   ReservationRepository reservationRepository) {
//		return (evt) -> Arrays.asList(
//				"jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
//				.forEach(
//						a -> {
//							Account account = accountRepository.save(new Account("test.user.one@email.com"));
//							Account account2 = accountRepository.save(new Account("test.user.two@email.com"));
//							reservationRepository.save(new Reservation(account, null));
////							reservationRepository.save(new Bookmark(account,"http://bookmark.com/2/" + a, "A description"));
//						});
//	}
}
