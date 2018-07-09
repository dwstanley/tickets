//package github.dwstanle.tickets.service.impl;
//
//import github.dwstanle.tickets.StringListSeatMap;
//import github.dwstanle.tickets.model.Event;
//import github.dwstanle.tickets.search.RuleEvaluator;
//import github.dwstanle.tickets.search.basic.BasicSeatMapFactory;
//import github.dwstanle.tickets.search.basic.BasicSeatMapGenerator;
//import github.dwstanle.tickets.search.SimpleTicketSearchEngine;
//import github.dwstanle.tickets.service.TicketService;
//
//import java.util.Collection;
//import java.util.Objects;
//import java.util.stream.Stream;
//
//import static java.lang.Math.toIntExact;
//
//public class SingleEventTicketService extends BasicReservationService<StringListSeatMap> implements TicketService {
//
//    private final Event event;
//
//    public SingleEventTicketService(Event event) {
//        super.setSearchEngine(new SimpleTicketSearchEngine<>(
//                        new BasicSeatMapGenerator(),
//                        new RuleEvaluator(),
//                        new BasicSeatMapFactory()));
//
//        this.event = Objects.requireNonNull(event);
//    }
//
//    @Override
//    public int numSeatsAvailable() {
//        return toIntExact(getSeats(event).filter("A"::equals).count());
//    }
//
////    @Override
////    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
////        return null;
////    }
//
//    @Override
//    public String reserveSeats(int seatHoldId, String customerEmail) {
//        return null;
//    }
//
//    private Stream<String> getSeats(Event event) {
//        return event.getVenue().getLayout().getSeats().stream().flatMap(Collection::stream);
//    }
//
//}
