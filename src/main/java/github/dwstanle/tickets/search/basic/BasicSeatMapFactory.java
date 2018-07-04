package github.dwstanle.tickets.search.basic;

import github.dwstanle.tickets.StringListSeatMap;
import github.dwstanle.tickets.search.SeatMapFactory;

import java.util.List;

public class BasicSeatMapFactory implements SeatMapFactory<StringListSeatMap> {
//    @Override
//    public StringListSeatMap of(List<List<String>> rows) {
//
//        // todo - assumes all rows are the same length
//        // todo - test with different sized layouts, including an empty layout
//
//        StringListSeatMap bookingMemento = new StringListSeatMap(rows.size(), rows.get(0).size());
//
//        List<String> cols;
//        for (int i = 0; i < rows.size(); i++) {
//            cols = rows.get(i);
//            for (int j = 0; j < cols.size(); j++) {
//                bookingMemento.setSeat(new Seat(i, j), SeatStatus.ofCode(cols.get(j)));
//            }
//        }
//        return bookingMemento;
//    }

    @Override
    public StringListSeatMap of(List<List<String>> rows) {
        return new StringListSeatMap(rows);
    }
}
