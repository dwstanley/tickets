package github.dwstanle.tickets;

import github.dwstanle.tickets.model.*;
import github.dwstanle.tickets.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static github.dwstanle.tickets.util.SeatMapStrings.SIMPLE_LAYOUT_STR;

@SpringBootApplication
public class TicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketsApplication.class, args);
	}

	@Bean
	CommandLineRunner init(AccountRepository accountRepository,
						   EventRepository eventRepository,
						   ReservationRepository reservationRepository,
						   SectionRepository sectionRepository,
						   VenueRepository venueRepository) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				Section section = sectionRepository.save(new Section(SIMPLE_LAYOUT_STR));
				Venue venue = venueRepository.save(Venue.builder().section(section).build());
				section.setVenue(venue);
				section.setName("A");
				sectionRepository.save(section);
				Event event = eventRepository.save(new Event("demoEvent", venue));
				Account account = accountRepository.save(new Account("demo@fakeemail.com"));
			}
		};
	}
}
