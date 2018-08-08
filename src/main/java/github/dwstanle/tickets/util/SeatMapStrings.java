package github.dwstanle.tickets.util;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class SeatMapStrings {

    public static String SIMPLE_LAYOUT_STR =
            "S S S S S S S S\n" +
                    "A A A A A A A A\n" +
                    "A A A A A A A A\n" +
                    "A A A A A A A A\n" +
                    "A A A A A A A A\n" +
                    "A A A A A A A A\n" +
                    "A A A A A A A A";

    public static List<List<String>> fromPath(Path path) throws IOException {
        return fromPath(path, " ");
    }

    public static List<List<String>> fromPath(Path path, String delimiter) throws IOException {
        List<List<String>> seatMap;
        try (Stream<String> lines = Files.lines(path)) {
            seatMap = fromLines(lines, delimiter);
        } catch (IOException ioe) {
            throw ioe;
        }
        return seatMap;
    }

    public static List<List<String>> copy(List<List<String>> seatMap) {
        return seatMap.stream().map(ArrayList::new).collect(toList());
    }

    public static List<List<String>> fromLines(Stream<String> lines, String delimiter) {
        return lines.map(s -> asList(s.split(delimiter))).collect(toList());
    }

    public static List<List<String>> fromString(String seatMapStr) {
        return fromLines(Stream.of(seatMapStr.split("\n")), " ");
    }

    public static String toString(List<List<String>> layout) {
        return String.join("\n", layout.stream().map(s -> String.join(" ", s)).collect(toList()));
    }

}
