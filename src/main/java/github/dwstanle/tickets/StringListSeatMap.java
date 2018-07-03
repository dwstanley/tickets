package github.dwstanle.tickets;

import github.dwstanle.tickets.model.Seat;

import java.util.*;

import static github.dwstanle.tickets.SeatStatus.AVAILABLE;
import static github.dwstanle.tickets.util.SeatMapUtil.copy;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

public class StringListSeatMap implements SeatMap {

    private final List<List<String>> seats;

    public StringListSeatMap(int numberOfRows, int numberOfCols) {
        this.seats = new ArrayList<>(numberOfCols);
        for (int i = 0; i < numberOfRows; i++) {
            this.seats.add(new ArrayList<>(Collections.nCopies(numberOfCols, AVAILABLE.getCode())));
        }
    }

    public StringListSeatMap(List<List<String>> seats) {
        Objects.requireNonNull(seats);
        this.seats = copy(seats); // todo, do we want copy here?
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

//    public Optional<Seat> getSeat(int row, int col) {
//        return Optional.ofNullable(seats.get(row).get(col));
//    }

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

    public List<List<String>> getSeats() {
        return seats;
    }
}
