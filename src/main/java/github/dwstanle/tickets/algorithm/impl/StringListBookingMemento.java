package github.dwstanle.tickets.algorithm.impl;

import github.dwstanle.tickets.algorithm.BookingMemento;
import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.model.SeatStatus;
import github.dwstanle.tickets.model.VenueMemento;

import java.util.*;

import static github.dwstanle.tickets.model.SeatStatus.AVAILABLE;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

public class StringListBookingMemento implements BookingMemento {

    private final List<List<String>> seats;

    public StringListBookingMemento (int numberOfRows, int numberOfCols) {
        this.seats = new ArrayList<>(numberOfCols);
        for (int i = 0; i < numberOfRows; i++) {
            this.seats.add(new ArrayList<>(Collections.nCopies(numberOfCols, AVAILABLE.getCode())));
        }
    }

    @Override
    public VenueMemento asSeatMap() {
        return null;
    }

    @Override
    public void setSeat(Seat seat, SeatStatus status) {
        setSeat(seat.getRow(), seat.getCol(), status.getCode());
    }

    @Override
    public Optional<Set<Seat>> getNewSeats() {
        return Optional.empty();
    }

    @Override
    public SeatStatus getSeatStatus(Seat seat) {
        String statusCode = getSeat(seat.getRow(), seat.getCol());
        return SeatStatus.ofCode(statusCode);
    }

    public String getSeat(int row, int col) {
        return seats.get(row).get(col);
    }

    public void setSeat(int row, int col, String status) {
        seats.get(row).set(col, status);
    }

    @Override
    public String toString() {
        return join("\n", seats.stream().map(s -> join(" ", s)).collect(toList()));
    }

}
