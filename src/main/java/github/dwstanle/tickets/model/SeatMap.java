package github.dwstanle.tickets.model;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class SeatMap {

    public static String SIMPLE_LAYOUT_STR =
                    "S S S S S S S S\n" +
                    "A A A A A A A A\n" +
                    "A A A A A A A A\n" +
                    "A A A A A A A A\n" +
                    "A A A A A A A A\n" +
                    "A A A A A A A A\n" +
                    "A A A A A A A A";

    public static final SeatMap SIMPLE = fromString(SIMPLE_LAYOUT_STR);

    public static SeatMap fromPath(Path path) {
        List<List<String>> seatMap;
        try (Stream<String> lines = Files.lines(path)) {
            seatMap = lines.map(s -> asList(s.split(","))).collect(toList());
        } catch (IOException ioe) {
            seatMap = new ArrayList<>();
            ioe.printStackTrace();
        }
        return new SeatMap(seatMap);
    }

    public static SeatMap fromString(String layoutStr) {
        Stream<String> rows = Stream.of(layoutStr.split("\n"));
        List<List<String>> seatMap = rows.map(s -> asList(s.split(" "))).collect(toList());
        return new SeatMap(seatMap);
    }

    public static SeatMap fromSeatMap(SeatMap seatMap) {
        return new SeatMap(seatMap.getRows().stream().map(ArrayList::new).collect(toList()));
    }

    private final List<List<String>> seats;

    private SeatMap(List<List<String>> seats) {
        this.seats = Objects.requireNonNull(seats);
    }

    // todo make seats immutable
    public List<List<String>> getRows() {
        return seats;
    }

    @Override
    public String toString() {
        return String.join("\n", seats.stream().map(s -> String.join(" ", s)).collect(toList()));
    }

}
