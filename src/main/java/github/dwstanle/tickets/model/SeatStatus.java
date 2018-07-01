package github.dwstanle.tickets.model;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum SeatStatus {

    AVAILABLE("A"),
    OBSTACLE("X"),
    STAGE("S"),
    RESERVED("R"),
    HELD("H");

    private static final Map<String, SeatStatus> CODE_MAP = Stream.of(values())
            .collect(toMap(SeatStatus::getCode, seatStatus -> seatStatus));

    public static SeatStatus ofCode (String code) {
        return CODE_MAP.get(code);
    }

    private final String code;

    SeatStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}