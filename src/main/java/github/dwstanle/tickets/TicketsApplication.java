package github.dwstanle.tickets;

import github.dwstanle.tickets.model.Account;
import github.dwstanle.tickets.model.Event;
import github.dwstanle.tickets.model.Section;
import github.dwstanle.tickets.model.Venue;
import github.dwstanle.tickets.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
				Section section = sectionRepository.save(new Section(loadDemoStr()));
				Venue venue = venueRepository.save(Venue.builder().section(section).build());
				section.setVenue(venue);
				section.setName("A");
				sectionRepository.save(section);
				Event event = eventRepository.save(new Event("demoEvent", venue));
				Account account = accountRepository.save(new Account("demo@fakeemail.com"));
			}
		};
	}

	private String loadDemoStr() throws URISyntaxException {
		Path path = Paths.get(ClassLoader.getSystemResource("DemoLayout.csv").toURI());
		return SeatMap.fromPath(path).toString();
	}
}
