package github.dwstanle.tickets.model;

import github.dwstanle.tickets.StringListSeatMap;
import github.dwstanle.tickets.util.SeatMapUtil;

import java.nio.file.Path;
import java.util.List;

import static github.dwstanle.tickets.util.SeatMapUtil.SIMPLE_LAYOUT_STR;

// immutable representation of StringListSeatMap used to sore the original
// state of a venue, should never contain held(H) or reserved(R) characters
// todo - in constructors make sure only allowable characters are used
public class VenueSeatMap extends StringListSeatMap {

    public static final VenueSeatMap SIMPLE = fromString(SIMPLE_LAYOUT_STR);

    public static VenueSeatMap fromPath(Path path) {
        return new VenueSeatMap(SeatMapUtil.fromPath(path, ","));
    }

    public static VenueSeatMap fromString(String seatMapStr) {
        return new VenueSeatMap(SeatMapUtil.fromString(seatMapStr));
    }

    public static VenueSeatMap fromSeatMap(VenueSeatMap seatMap) {
        return new VenueSeatMap(SeatMapUtil.copy(seatMap.getSeats()));
    }

    public static VenueSeatMap from2DList(List<List<String>> seats) {
        return new VenueSeatMap(SeatMapUtil.copy(seats));
    }

    private VenueSeatMap(List<List<String>> seats) {
        super(seats);
    }

    // todo make seats immutable
    @Override
    public List<List<String>> getSeats() {
        return super.getSeats();
    }

}
