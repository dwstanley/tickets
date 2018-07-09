package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.SeatStatus;
import github.dwstanle.tickets.StringListSeatMap;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

import static github.dwstanle.tickets.SeatStatus.isAvailable;
import static java.util.Optional.empty;
import static java.util.Optional.of;

class LeftFillGenerator {

    private final Set<List<Point>> resultSet = new LinkedHashSet<>();
    private final int requestedNumberOfSeats;
    private final List<List<String>> seats;
    private final int rowSize;
    private final int colSize;

    public LeftFillGenerator(int requestedNumberOfSeats, SeatMap seatMap) {

        this.seats = new StringListSeatMap(seatMap).getSeats();
        this.requestedNumberOfSeats = requestedNumberOfSeats;
        this.rowSize = seats.size();
        this.colSize = seats.get(0).size();

//        if (rowSize * colSize > 1000000) {
        if (rowSize * colSize > 250000) {
            throw new UnsupportedOperationException("Solver does not support venues with over a 250,000 seats.");
        }
    }

    public Set<List<Point>> findAllSolutions() {
        if (requestedNumberOfSeats > 0 && isNumSeatsAvailable(requestedNumberOfSeats)) {
            for (int row = 0; row < rowSize; row++) {
                for (int col = 0; col < colSize; col++) {
                    resultSet.addAll(findSolutions(new Point(col, row)));
                }
            }
        }
        return resultSet;
    }

    private Set<List<Point>> findSolutions(Point seat) {
//        System.out.println("\nfinding solutions for: " + seat);
        Set<List<Point>> result = new HashSet<>();
        // represents the bounded area available to fill with seats starting from the provided seat
        for(Rectangle fillBounds : calcFillBounds(seat, getMinPerRowAmt())) {
            if (getMinPerRowAmt() <= fillBounds.width && requestedNumberOfSeats <= fillBounds.width * fillBounds.height) {
//            System.out.println("filling solutions for: " + seat);
//            System.out.println("\nfill bounds are: " + fillBounds);
//            System.out.println(leftFill(fillBounds, getMinPerRowAmt()));
                leftFill(fillBounds, getMinPerRowAmt()).ifPresent(result::add);
            }
        }
        return result;
    }

    private Optional<List<Point>> leftFill(Rectangle bounds, int minPerRow) {
        List<Point> seatsFilled = new ArrayList<>();
        Map<Integer, Integer> rowFilledCount = new HashMap<>();

        outer:
        for (int row = bounds.y; row < bounds.y + bounds.height; row++) {
            int filledThisRow = 0;
            for (int col = bounds.x; col < bounds.x + bounds.width; col++) {
                if (seatsFilled.size() == requestedNumberOfSeats) {
                    break outer; // exit if we already have the required number of seats
                } else if (isAvailable(seat(row, col))) {
                    seatsFilled.add(new Point(col, row));
                    filledThisRow++;
                    rowFilledCount.put(row, filledThisRow);
                } else {
                    break; // jump to next row, we only want consecutive seats
                }
            }
            if (filledThisRow < minPerRow) {
                break; // if previous row did not fill enough bail out
            }
        }

        // check to make sure the rows we filled had the minPerRow
        long rowsNotFilledToMin = rowFilledCount.values().stream().filter(count -> count < minPerRow).count();
        boolean conditionsMet = (rowsNotFilledToMin == 0) && seatsFilled.size() == requestedNumberOfSeats;

        return conditionsMet ? of(seatsFilled) : empty();
    }

//    private Rectangle calcFillBounds(Point seat) {
//        return new Rectangle(seat, new Dimension(colSize - seat.x, rowSize - seat.y));
//    }

    private List<Rectangle> calcFillBounds(Point seat, int minPerRow) {
        List<Rectangle> boundsList = new ArrayList<>();
        int height = rowSize - seat.y;
        for (int width = colSize - seat.x; width >= minPerRow; width--) {
            boundsList.add(new Rectangle(seat, new Dimension(width, height)));
        }
        return boundsList;
    }

    private int getTotalSeats(List<List<String>> seats) {
        return seats.size() * seats.get(0).size();
    }

    private long getTotalSeatsAvailable(List<List<String>> seats) {
        return seats.stream().flatMap(Collection::stream).filter(SeatStatus::isAvailable).count();
    }

    private boolean isNumSeatsAvailable(int numSeats) {
        return numSeats < getTotalSeats(seats) && numSeats < getTotalSeatsAvailable(seats);
    }

    private String seat(int row, int col) {
        return seats.get(row).get(col);
    }

    private int getMinPerRowAmt() {
        return (requestedNumberOfSeats < 3) ? requestedNumberOfSeats : 3;
    }

    public Set<List<Point>> findAllSolutions(Predicate<List<Point2D>> breakEarlyCondition) {
        // todo
        return null;
    }
}
