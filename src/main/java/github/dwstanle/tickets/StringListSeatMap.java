package github.dwstanle.tickets;

import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.util.SeatMapUtil;

import java.util.*;

import static github.dwstanle.tickets.SeatStatus.AVAILABLE;
import static java.lang.Math.toIntExact;
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
        this.seats = Objects.requireNonNull(seats);
    }

    public StringListSeatMap(SeatMap seatMap) {
        this.seats = new ArrayList<>(seatMap.getNumberOfCols());
        for (int row = 0; row < seatMap.getNumberOfRows(); row++) {
            this.seats.add(new ArrayList<>());
            for (int col = 0; col < seatMap.getNumberOfCols(); col++) {
                this.seats.get(row).add(seatMap.getSeatStatus(new Seat(row, col)).getCode());
            }
        }
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

    @Override
    public int getNumberOfRows() {
        return seats.size();
    }

    @Override
    public int getNumberOfCols() {
        return seats.get(0).size();
    }

    @Override
    public int numberOfSeatsAvailable() {
        return toIntExact(seats.stream()
                .flatMap(Collection::stream)
                .filter(AVAILABLE.getCode()::equalsIgnoreCase)
                .count());
    }

    @Override
    public StringListSeatMap copy() {
        return new StringListSeatMap(SeatMapUtil.copy(seats));
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
