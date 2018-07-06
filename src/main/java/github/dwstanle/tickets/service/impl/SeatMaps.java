package github.dwstanle.tickets.service.impl;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.SeatStatus;
import github.dwstanle.tickets.model.Reservation;
import github.dwstanle.tickets.model.Seat;

import java.util.Collection;

import static github.dwstanle.tickets.SeatStatus.AVAILABLE;

public class SeatMaps {

    public static <M extends SeatMap> void addToSeatMap(M seatMap, Reservation reservation) {
        reservation.getSeats().forEach(seat -> addToSeatMap(seatMap, seat, reservation.getStatus()));
    }

    public static <M extends SeatMap> void addToSeatMap(M seatMap, Seat seat, SeatStatus seatStatus) {
        switch (seatMap.getSeatStatus(seat)) {
            case RESERVED:
            case STAGE:
            case OBSTACLE:
                throw new UnsupportedOperationException("requested seat cannot be reserved or held.");
            default:
                seatMap.setSeat(seat, seatStatus);
        }
    }

    public static <M extends SeatMap> boolean areSeatsAvailable(M seatMap, Collection<Seat> seats) {
        long numRequestedSeatsAvail = seats.stream()
                .map(seatMap::getSeatStatus)
                .filter(AVAILABLE::equals)
                .count();
        return numRequestedSeatsAvail == seats.size();
    }

    public static <M extends SeatMap> boolean areSeatsInRange(M seatMap, Collection<Seat> seats) {

        int rowSize = seatMap.getNumberOfRows();
        int colSize = seatMap.getNumberOfCols();

        boolean inRange = true;
        for (Seat seat : seats) {
            if (seat.getRow() >= rowSize || seat.getRow() < 0
                    || seat.getCol() >= colSize || seat.getCol() < 0) {
                inRange = false;
                break;
            }
        }

        return inRange;
    }

}
