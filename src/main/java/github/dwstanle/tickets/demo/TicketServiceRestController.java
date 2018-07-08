
package github.dwstanle.tickets.demo;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.model.Account;
import github.dwstanle.tickets.model.Event;
import github.dwstanle.tickets.model.Reservation;
import github.dwstanle.tickets.repository.AccountRepository;
import github.dwstanle.tickets.repository.EventRepository;
import github.dwstanle.tickets.repository.ReservationRepository;
import github.dwstanle.tickets.service.ReservationRequest;
import github.dwstanle.tickets.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
class TicketServiceRestController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private Environment env;

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    @RequestMapping(value = "/reservations", method = RequestMethod.GET)
    @ResponseBody
    public List<Reservation> findAllReservations() {
        List<Reservation> reservationList = reservationRepository.findAll();
        return reservationList;
    }

    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    @ResponseBody
    public String showOverview() {

        Event event = eventRepository.findByName("demoEvent");
        int numberOfSeatsAvailable = reservationService.findNumberOfSeatsAvailable(event);

        StringBuilder sb = new StringBuilder();
        sb.append("\n<b>Current seat map for: </b>").append(event.getName());
        sb.append("\n<br><b>Number of seats available: </b>").append(numberOfSeatsAvailable);
        sb.append("<pre>");
        event.getVenue().getSections().forEach(section -> {
            SeatMap seatMap = reservationService.getUpdatedSeatMap(event, section.getId());
            sb.append("\n\nSection: ").append(section.getName()).append("\n").append(seatMap);
        });
        sb.append("\n</pre>");

        return sb.toString();
    }

    @RequestMapping(value = "/demo/numSeatsAvailable", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public int numSeatsAvailable() {
        Event event = eventRepository.findByName("demoEvent");
        return reservationService.findNumberOfSeatsAvailable(event);
    }


    @RequestMapping(value = "/demo/findAndHoldSeats", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public SeatHold findAndHoldSeats(@RequestParam(name="numSeats") Integer numSeats, @RequestParam(name="customerEmail") String customerEmail) {

        Event event = eventRepository.findByName("demoEvent");

//        Account account = accountRepository.findByEmail(customerEmail).orElse(accountRepository.save(new Account(customerEmail)));
        Account account = accountRepository.findByEmail("demo@fakeemail.com");

        ReservationRequest request = ReservationRequest.builder()
                .event(event)
                .numberOfSeats(numSeats)
                .account(account.getEmail())
                .build();

        Reservation reservation = reservationService.findAndHoldBestAvailable(request).orElse(null);

        return new SeatHold(reservation, Integer.valueOf(env.getProperty("reservation.timeoutInSeconds")));
    }

    @RequestMapping(value = "/demo/reserveSeats", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String reserveSeats(@RequestParam(name="seatHoldId") Integer seatHoldId, @RequestParam(name="customerEmail") String customerEmail) {
//        Account account = accountRepository.findByEmail(customerEmail).orElse(accountRepository.save(new Account(customerEmail)));
        Account account = accountRepository.findByEmail("demo@fakeemail.com");
        Optional<Reservation> reservation = reservationService.reserveSeats(seatHoldId, account.getEmail());
        return reservation.isPresent() ? "SUCCESS" : "FAIL";
    }

}