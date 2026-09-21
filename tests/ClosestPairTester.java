import main.java.ClosestPairSolver;
import main.java.Point;

import java.util.Random;

public class ClosestPairTester {

    private static final Random RANDOM = new Random(45);

    public static void runTests() {

        System.out.println(
                "Testing Closest Pair..."
        );

        int[] smallSizes = {
                2,
                3,
                5,
                10,
                50,
                100,
                500,
                1000,
                2000
        };

        //For n <= 2000:
        for (int size : smallSizes) {

            for (int test = 0; test < 5; test++) {

                Point[] points =
                        generateRandomPoints(size);

                double expected =
                        bruteForceClosestPair(points);

                ClosestPairSolver solver =
                        new ClosestPairSolver();

                double actual =
                        solver.solve(points);

                assertEqual(
                        expected,
                        actual
                );
            }
        }

        // Duplicate points
        Point[] duplicatePoints = {
                new Point(1, 1),
                new Point(5, 5),
                new Point(1, 1),
                new Point(10, 10)
        };

        ClosestPairSolver solver =
                new ClosestPairSolver();

        double result =
                solver.solve(duplicatePoints);

        if (result != 0.0) {
            throw new AssertionError(
                    "Duplicate point test failed"
            );
        }

        // Large dataset
        Point[] largePoints =
                generateRandomPoints(100_000);

        long start = System.nanoTime();

        double largeResult =
                solver.solve(largePoints);

        long end = System.nanoTime();

        if (!Double.isFinite(largeResult)) {
            throw new AssertionError(
                    "Closest Pair failed on large dataset"
            );
        }

        System.out.println(
                "Closest Pair: PASSED"
        );

        System.out.println(
                "Large test: 100000 points, " +
                        (end - start) +
                        " ns"
        );
    }

    private static double bruteForceClosestPair(
            Point[] points) {

        if (points.length < 2) {
            return Double.POSITIVE_INFINITY;
        }

        double best =
                Double.POSITIVE_INFINITY;

        for (int i = 0; i < points.length; i++) {

            for (int j = i + 1;
                 j < points.length;
                 j++) {

                double distanceSquared =
                        points[i].distanceSquared(
                                points[j]
                        );

                if (distanceSquared < best) {
                    best = distanceSquared;
                }
            }
        }

        return Math.sqrt(best);
    }

    private static void assertEqual(
            double expected,
            double actual) {

        double epsilon = 1e-9;

        if (Math.abs(expected - actual) > epsilon) {

            throw new AssertionError(
                    "Closest Pair failed.\n" +
                            "Expected: " + expected +
                            "\nActual: " + actual
            );
        }
    }

    private static Point[] generateRandomPoints(
            int size) {

        Point[] points = new Point[size];

        for (int i = 0; i < size; i++) {

            double x =
                    RANDOM.nextDouble() * 2000 - 1000;

            double y =
                    RANDOM.nextDouble() * 2000 - 1000;

            points[i] =
                    new Point(x, y);
        }

        return points;
    }

    public static void main(String[] args) {
        runTests();
    }
}