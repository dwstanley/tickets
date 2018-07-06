//package github.dwstanle.tickets.search.basic;
//
//import github.dwstanle.tickets.SeatStatus;
//import github.dwstanle.tickets.SeatMap;
//import github.dwstanle.tickets.model.*;
//import github.dwstanle.tickets.service.ReservationRequest;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//import java.util.Set;
//
//import static java.util.Collections.singleton;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
///**
// * Test class to ensure SimpleTicketSearchEngine is wiring the provided generator and evaluator correctly.
// */
//public class SimpleTicketSearchEngineTest {
//
//    private static class SeatMapStub implements SeatMap {
//
//        @Override
//        public void setSeat(Seat seat, SeatStatus status) {
//
//        }
//
//        @Override
//        public Optional<Set<Seat>> getNewSeats() {
//            return Optional.empty();
//        }
//
//        @Override
//        public SeatStatus getSeatStatus(Seat seat) {
//            return null;
//        }
//
//    }
//
//    @Mock
//    private SeatMapGenerator<SeatMapStub> generator;
//
//    @Mock
//    private SeatMapEvaluator<SeatMapStub> evaluator;
//
//    @Mock
//    private SeatMapFactory<SeatMapStub> factory;
//
//    private SimpleTicketSearchEngine<SeatMapStub> engine;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        this.engine = new SimpleTicketSearchEngine<>(generator, evaluator, factory);
//    }
//
//    @Test
//    public void findBestAvailable() {
//
//        ReservationRequest request = ReservationRequest.builder()
//                .event(new Event(new Venue("S S S \n S S S")))
//                .account(new Account("foo@email.com"))
//                .numberOfSeats(4).build();
//
//        SeatMapStub originalBookingMemento = new SeatMapStub();
//        Set<SeatMapStub> bookingMementos = singleton(new SeatMapStub());
//
//        when(factory.of(any())).thenReturn(originalBookingMemento);
//        when(generator.generateAllAvailable(any(), any())).thenReturn(bookingMementos);
//        when(evaluator.findBest(any())).thenReturn(bookingMementos.iterator().next());
//
//        engine.findBestAvailable(request);
//
//        Mockito.verify(generator).generateAllAvailable(request, originalBookingMemento);
//        Mockito.verify(evaluator).findBest(bookingMementos);
//    }
//
//    @Test
//    public void createMemento() {
//    }
//}