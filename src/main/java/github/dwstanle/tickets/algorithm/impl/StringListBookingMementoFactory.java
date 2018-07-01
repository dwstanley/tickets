package github.dwstanle.tickets.algorithm.impl;

import github.dwstanle.tickets.algorithm.BookingMementoFactory;
import github.dwstanle.tickets.model.Seat;
import github.dwstanle.tickets.model.SeatMap;
import github.dwstanle.tickets.model.SeatStatus;

import java.util.List;

public class StringListBookingMementoFactory implements BookingMementoFactory<StringListBookingMemento> {
    @Override
    public StringListBookingMemento of(SeatMap layout) {

        // todo - assumes all rows are the same length
        // todo - test with different sized layouts, including an empty layout
        List<List<String>> rows = layout.getRows();

        StringListBookingMemento bookingMemento = new StringListBookingMemento(rows.size(), rows.get(0).size());

        List<String> cols;
        for (int i = 0; i < rows.size(); i++) {
            cols = rows.get(i);
            for (int j = 0; j < cols.size(); j++) {
                bookingMemento.setSeat(new Seat(i, j), SeatStatus.ofCode(cols.get(j)));
            }
        }
        return bookingMemento;
    }
}
