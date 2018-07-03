package github.dwstanle.tickets.model;

import lombok.Data;

@Data
public class Seat {
    private final String section;
    private final int row;
    private final int col;

    public Seat(int row, int col) {
        this.row = requirePositive(row, "row");
        this.col = requirePositive(col, "col");
        this.section = "";
    }

    private int requirePositive(int property, String propertyName) {
        if (property < 0) {
            throw new IllegalArgumentException(propertyName + " must be positive.");
        }
        return property;
    }
}
