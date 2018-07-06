package github.dwstanle.tickets.repository;

import github.dwstanle.tickets.model.Event;
import github.dwstanle.tickets.model.Section;
import github.dwstanle.tickets.model.Venue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static github.dwstanle.tickets.util.SeatMapStrings.SIMPLE_LAYOUT_STR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventRepository eventRepository;


    @Test
    public void save() {
        Venue venue = entityManager.persist(Venue.builder().section(new Section(SIMPLE_LAYOUT_STR)).build());
        Event event = eventRepository.save(new Event(venue));
        assertNotNull(event);
    }

    @Test
    public void findById() {
        Section section = entityManager.persist(new Section(SIMPLE_LAYOUT_STR));
        Venue venue = entityManager.persist(Venue.builder().section(section).build());
        Event event = eventRepository.save(new Event(venue));
        assertEquals(event, eventRepository.findById(event.getId()).get());
    }


}