package github.dwstanle.tickets.search;

import github.dwstanle.tickets.SeatMap;
import github.dwstanle.tickets.SeatStatus;
import github.dwstanle.tickets.StringListSeatMap;
import github.dwstanle.tickets.model.Seat;
import lombok.Getter;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static github.dwstanle.tickets.SeatStatus.AVAILABLE;
import static github.dwstanle.tickets.SeatStatus.HELD;
import static java.util.Optional.empty;

public class SimpleEvaluator {

    private final SeatMap origin;

    private final Properties env;

    public SimpleEvaluator(int requestedNumberOfSeats, SeatMap origin) {
        this.origin = Objects.requireNonNull(origin);
        this.env = loadProperties("application.properties");
    }

    public Optional<Set<Seat>> findBest(Set<List<Point>> allSolutions) {

        // find best options from the view of the venue
        int bestVenueScore = Integer.MIN_VALUE;
        Set<Solution> bestOptions = new LinkedHashSet<>();

        for (List<Point> pointList : allSolutions) {
            Solution solution = new Solution(origin, pointList);
            int venueScore = venueScore(solution);
            if (venueScore == bestVenueScore) {
                bestOptions.add(solution);
            } else if (venueScore > bestVenueScore) {
                bestOptions.clear();
                bestOptions.add(solution);
                bestVenueScore = venueScore;
            }
        }

        // given multiple equivalent options from the venue, find the best option for the customer
        double bestCostumerScore = Double.MIN_VALUE;
        Solution bestOption = null;

        for (Solution solution : bestOptions) {
            double customerScore = customerScore(solution);
            if (customerScore > bestCostumerScore) {
                bestOption = solution;
                bestCostumerScore = customerScore;
            }
        }

        return (null == bestOption) ? empty() : Optional.of(bestOption.getNewSeats());

    }

    // this probably requires market research and might be venue specific, for now assume all venue owners only care
    // about not leaving single seats open because they are difficult to sell
    private int venueScore(Solution solution) {
        List<List<String>> seats = solution.getSeats();
        // count the number of individual seats
        int totalIndividualSeats = 0;
        for (int row = 0; row < seats.size(); row++) {
            for (int col = 0; col < seats.get(row).size(); col++) {
                if (isAvailable(seats, row, col) && !isAvailable(seats, row, col - 1) && !isAvailable(seats, row, col + 1)) {
                    totalIndividualSeats++;
                }
            }
        }

        // a fewer number of seats should have a higher score return return number of seats NOT individual and unbooked
        return (solution.getSeatMap().getNumberOfCols() * solution.getSeatMap().getNumberOfCols()) - totalIndividualSeats;

    }

    // Assumes customers care about two things:
    // 1) being as close to the stage as possible
    // 2) being as close to other members in their groups as possible
    private double customerScore(Solution solution) {

        // initialize scores map with all seats having a score of 0
        Map<Point, Double> scores = solution.getPointList().stream().collect(Collectors.toMap(p -> p, p -> 0.0));

        // for each seat, score based off rules above
        solution.getPointList().forEach(seat -> {

            // distance score is larger the closer to the stage you are. stage is assumed before row 0
            int distanceFromStage = origin.getNumberOfRows() - seat.y;

            // count number of party members one seat away [0-8]
            int numberOfGroupMembersNearby = 0;
            numberOfGroupMembersNearby += scores.containsKey(move(seat, -1, -1)) ? 1 : 0;
            numberOfGroupMembersNearby += scores.containsKey(move(seat, -1, 0)) ? 1 : 0;
            numberOfGroupMembersNearby += scores.containsKey(move(seat, -1, 1)) ? 1 : 0;
            numberOfGroupMembersNearby += scores.containsKey(move(seat, 0, -1)) ? 1 : 0;
            numberOfGroupMembersNearby += scores.containsKey(move(seat, 0, 1)) ? 1 : 0;
            numberOfGroupMembersNearby += scores.containsKey(move(seat, 1, -1)) ? 1 : 0;
            numberOfGroupMembersNearby += scores.containsKey(move(seat, 1, 0)) ? 1 : 0;
            numberOfGroupMembersNearby += scores.containsKey(move(seat, 1, 1)) ? 1 : 0;

            // normalize and weigh the two scores
            double normalizedDistanceFromStage =
                    Integer.valueOf(env.getProperty("evaluator.distanceFromStageWeight")) *
                    normalize(distanceFromStage, 0, origin.getNumberOfRows());

            double normalizedNumberOfGroup = normalize(numberOfGroupMembersNearby, 0, 8);

            scores.put(seat, normalizedDistanceFromStage + normalizedNumberOfGroup);

        });

        // final score is the avg of all seat scores
        return scores.values().stream().mapToDouble(Double::valueOf).average().orElse(Double.MIN_VALUE);

    }

    private boolean isAvailable(List<List<String>> seats, int row, int col) {
        boolean available = false;
        if (row >= 0 && row < seats.size() && col >= 0 && col < seats.get(row).size()) {
            available = AVAILABLE == SeatStatus.ofCode(seats.get(row).get(col));
        }
        return available;
    }

    private Point move(Point point, int x, int y) {
        return new Point(point.x + x, point.y + y);
    }

    private double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    private Properties loadProperties(String resourceName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    private static Set<Seat> toSeats(List<Point> pointList) {
        return pointList.stream()
                .map(point -> new Seat(point.y, point.x))
                .collect(Collectors.toSet());
    }

    @Getter
    private static class Solution {
        private final List<Point> pointList;
        private final SeatMap seatMap;
        private final List<List<String>> seats;
        private final Set<Seat> newSeats;

        public Solution(SeatMap origin, List<Point> pointList) {
            this.pointList = pointList;
            this.newSeats = toSeats(pointList);
            this.seatMap = origin.copyWithNewSeats(newSeats, HELD);
            this.seats = new StringListSeatMap(seatMap).getSeats();
        }
    }

}