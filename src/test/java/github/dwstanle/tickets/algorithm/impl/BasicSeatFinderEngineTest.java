package github.dwstanle.tickets.algorithm.impl;

import github.dwstanle.tickets.algorithm.BookingMemento;
import github.dwstanle.tickets.algorithm.BookingMementoFactory;
import github.dwstanle.tickets.algorithm.SeatAssignmentEvaluator;
import github.dwstanle.tickets.algorithm.SeatAssignmentGenerator;
import github.dwstanle.tickets.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singleton;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test class to ensure BasicSeatFinderEngine is wiring the provided generator and evaluator correctly.
 */
public class BasicSeatFinderEngineTest {

    private static class BookingMementoStub implements BookingMemento {

        @Override
        public VenueMemento asSeatMap() {
            return null;
        }

        @Override
        public void setSeat(Seat seat, SeatStatus status) {

        }

        @Override
        public Optional<Set<Seat>> getNewSeats() {
            return Optional.empty();
        }

        @Override
        public SeatStatus getSeatStatus(Seat seat) {
            return null;
        }

    }

    @Mock
    private SeatAssignmentGenerator<BookingMementoStub> generator;

    @Mock
    private SeatAssignmentEvaluator<BookingMementoStub> evaluator;

    @Mock
    private BookingMementoFactory<BookingMementoStub> factory;

    @Mock
    private Event event;

    @Mock
    private Account account;

    private BasicSeatFinderEngine<BookingMementoStub> engine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.engine = new BasicSeatFinderEngine<>(generator, evaluator, factory);
    }

    @Test
    public void findBestAvailable() {

        ReservationRequest request = ReservationRequest.builder()
                .event(event)
                .account(account)
                .numberOfSeats(4).build();

        BookingMementoStub originalBookingMemento = new BookingMementoStub();
        Set<BookingMementoStub> bookingMementos = singleton(new BookingMementoStub());

        when(factory.of(any())).thenReturn(originalBookingMemento);
        when(generator.generateAllAvailable(any(), any())).thenReturn(bookingMementos);
        when(evaluator.findBest(any())).thenReturn(bookingMementos.iterator().next());

        engine.findBestAvailable(request);

        Mockito.verify(generator).generateAllAvailable(request, originalBookingMemento);
        Mockito.verify(evaluator).findBest(bookingMementos);
    }

    @Test
    public void createMemento() {
    }
}