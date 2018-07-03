//package github.dwstanle.tickets;
//
//import github.dwstanle.tickets.model.Seat;
//
//import java.util.*;
//
//import static github.dwstanle.tickets.SeatStatus.AVAILABLE;
//import static github.dwstanle.tickets.util.SeatMapUtil.copy;
//import static java.lang.String.join;
//import static java.util.stream.Collectors.toList;
//
//public class ArraySeatMap implements SeatMap {
//
//    private final String[][] seats;
//
////    public ArraySeatMap(int numberOfRows, int numberOfCols) {
////        this.seats = new ArrayList<>(numberOfCols);
////        for (int i = 0; i < numberOfRows; i++) {
////            this.seats.add(new ArrayList<>(Collections.nCopies(numberOfCols, AVAILABLE.getCode())));
////        }
////    }
//
//    // assumes array is correctly sized
//    public ArraySeatMap(String[][] seats) {
//        this.seats = new String[seats.length][];
//        for (int i = 0; i < seats.length; i++) {
//            System.arraycopy(seats[i], 0, this.seats[i], 0, seats.length);
//        }
//    }
//
//    public ArraySeatMap(List<List<String>> seats) {
//        this.seats = new String[seats.length][];
//        for (int i = 0; i < seats.length; i++) {
//            System.arraycopy(seats[i], 0, this.seats[i], 0, seats.length);
//        }
//    }
//
//    @Override
//    public void setSeat(Seat seat, SeatStatus status) {
//        setSeat(seat.getRow(), seat.getCol(), status.getCode());
//    }
//
//    @Override
//    public Optional<Set<Seat>> getNewSeats() {
//        return Optional.empty();
//    }
//
//    @Override
//    public SeatStatus getSeatStatus(Seat seat) {
//        String statusCode = getSeat(seat.getRow(), seat.getCol());
//        return SeatStatus.ofCode(statusCode);
//    }
//
//    public String getSeat(int row, int col) {
////        return seats.get(row).get(col);
//        return null;
//    }
//
//    public void setSeat(int row, int col, String status) {
////        seats.get(row).set(col, status);
//    }
//
//    @Override
//    public String toString() {
//        return null;
////        return join("\n", seats.stream().map(s -> join(" ", s)).collect(toList()));
//    }
//
////    public List<List<String>> getSeats() {
////        return seats;
////    }
//}
