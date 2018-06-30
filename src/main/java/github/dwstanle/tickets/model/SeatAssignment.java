package github.dwstanle.tickets.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatAssignment {

    public static String HELD = "H";
    public static String RESERVED = "R";
    public static String AVAILABLE = "A";

    private int row;
    private int col;

}
