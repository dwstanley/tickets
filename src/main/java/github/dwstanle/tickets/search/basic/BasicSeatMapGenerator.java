package github.dwstanle.tickets.search.basic;

import github.dwstanle.tickets.StringListSeatMap;
import github.dwstanle.tickets.search.SeatMapGenerator;
import github.dwstanle.tickets.service.ReservationRequest;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BasicSeatMapGenerator implements SeatMapGenerator<StringListSeatMap> {
    @Override
    public Set<StringListSeatMap> generateAllAvailable(ReservationRequest request, StringListSeatMap originalBookingState) {

        // check if request is larger than VenueSeatMap
        // check if request is larger than availableSeats
        // optional check if request is larger than biggest open block
        // for each seat
        //   for each suggestedLayout
        //     attempt to place; break early if invalid
        //     is valid check should first check if block size will fit with given starting point
        //     is valid will then check if each requested seat is open

        Set<StringListSeatMap> resultSet = new HashSet<>();
        SolverInstance solver = new SolverInstance(originalBookingState.getSeats(), request.getNumberOfSeats());
        Set<List<Point>> solutions = solver.solve();

        // todo convert solutions into seatmaps?

        return resultSet;

    }

}