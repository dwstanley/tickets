package github.dwstanle.tickets;

import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.util.SeatMapStrings;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static github.dwstanle.tickets.util.SeatMapStrings.SIMPLE_LAYOUT_STR;

public interface SeatMap {

    SeatMap SIMPLE = fromString(SIMPLE_LAYOUT_STR);

    static SeatMap fromPath(Path path) {
        return new StringListSeatMap(SeatMapStrings.fromPath(path, ","));
    }

    static SeatMap fromString(String seatMapStr) {
        return new StringListSeatMap(SeatMapStrings.fromString(seatMapStr));
    }

    static SeatMap fromSeatMap(StringListSeatMap seatMap) {
        return new StringListSeatMap(SeatMapStrings.copy(seatMap.getSeats()));
    }

    static SeatMap from2DList(List<List<String>> seats) {
        return new StringListSeatMap(SeatMapStrings.copy(seats));
    }

    void setSeat(Seat seat, SeatStatus status);

    SeatStatus getSeatStatus(Seat seat);

    int getNumberOfRows();

    int getNumberOfCols();

    int numberOfSeatsAvailable();

    SeatMap copyWithNewSeats(Set<Seat> seatsToAdd, SeatStatus status);
}
