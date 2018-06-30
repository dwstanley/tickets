//package github.dwstanle.tickets;
//
//import github.dwstanle.tickets.algorithm.SeatAssignmentGenerator;
//import github.dwstanle.tickets.model.SeatAssignment;
//import github.dwstanle.tickets.model.SeatAssignmentRequest;
//import github.dwstanle.tickets.model.SeatAssignmentResult;
//import github.dwstanle.tickets.model.VenueMemento;
//
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//public class BasicSeatAssignmentGenerator implements SeatAssignmentGenerator {
//
//    @Override
//    public Set<SeatAssignmentResult> generateAllAvailable(SeatAssignmentRequest request, VenueMemento venue) {
//        Set<SeatAssignment> assignments = new HashSet<>();
//        // todo - return if request.NumSeats > venue.availSeats
//        for (int row = 0; row <= venue.getRowCount(); row++) {
//            for (int col = 0; col <= venue.getColCount(); col++) {
//                addSeatAssignments(new Seat(venue, row, col), assignments);
//            }
//        }
//        return null;
//    }
//
//    private void addSeatAssignments(Seat seat, Set<SeatAssignment> assignments) {
//        if (seat.isAvailable()) {
//
//        }
//    }
//
//    private class Seat {
//
//        private final VenueMemento venueMemento;
//        private final int row;
//        private final int col;
//
//        public Seat(VenueMemento venueMemento, int row, int col) {
//            this.venueMemento = venueMemento;
//            this.row = row;
//            this.col = col;
//        }
//
//        public Optional<Seat> west() {
//            return venueMemento.get(row, col - 1);
//        }
//
//        public Optional<Seat> east() {
//            return venueMemento.get(row, col + 1);
//        }
//
//        public Optional<Seat> north() {
//            return venueMemento.get(row - 1, col);
//        }
//
//        public Optional<Seat> south() {
//            return venueMemento.get(row + 1, col);
//        }
//    }
//
//    private class SeatBlock {
//        private int numberOfSeats;
//        public Set<Seat> getDesiredSeats(int startingRow, int startingCol) {
//            return null;
//        }
//        private Set<Seat> createNxMSeatBlock(int n, int m) {
//            Set<Seat> seats = new HashSet<>();
//            for (int i = start, )
//        }
//    }
//
//}
