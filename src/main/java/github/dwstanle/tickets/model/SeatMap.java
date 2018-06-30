package github.dwstanle.tickets.model;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SeatMap {

    public static final String SIMPLE_LAYOUT_STR = "S,S,S,S,S,S,S,S\nA,A,A,A,A,A,A,A\nA,A,A,A,A,A,A,A\nA,A,A,A,A,A,A,A\nA,A,A,A,A,A,A,A\nA,A,A,A,A,A,A,A\nA,A,A,A,A,A,A,A";

    public static final SeatMap SIMPLE = fromString(SIMPLE_LAYOUT_STR);

    public static SeatMap fromPath(Path path) {
        List<List<String>> seatMap;
        try (Stream<String> lines = Files.lines(path)) {
            seatMap = lines.map(SeatMap::splitCsvString).collect(toList());
        } catch (IOException ioe) {
            seatMap = new ArrayList<>();
            ioe.printStackTrace();
        }
        return new SeatMap(seatMap);
    }

    public static SeatMap fromString(String layoutString) {
        Stream<String> rows = Stream.of(layoutString.split("\n"));
        List<List<String>> seatMap = rows.map(SeatMap::splitCsvString).collect(toList());
        return new SeatMap(seatMap);
    }

    private static List<String> splitCsvString(String csvStr) {
        return Arrays.asList(csvStr.split(","));
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
