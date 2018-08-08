package github.dwstanle.tickets;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Representation of the state of 'seat' within a {@code SeatMap}. Because venues are not always symmetrical some seats
 * are actually a representation of an obstacle and cannot be booked.
 */
public enum SeatStatus {

    AVAILABLE("A"),
    OBSTACLE("X"),
    STAGE("S"),
    RESERVED("R"),
    HELD("H");

    public static boolean isAvailable(String s) {
        return AVAILABLE.getCode().equalsIgnoreCase(s);
    }

    public static SeatStatus ofCode (String code) {
        return CODE_MAP.get(code);
    }

    private static final Map<String, SeatStatus> CODE_MAP = Stream.of(values())
            .collect(toMap(SeatStatus::getCode, seatStatus -> seatStatus));

    private final String code;

    SeatStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}