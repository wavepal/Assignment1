import main.java.ClosestPairSolver;
import main.java.Point;

import java.util.Random;

public class ClosestPairTester {

    private static final Random RANDOM = new Random(45);

    public static void runTests() {

        System.out.println("Testing Closest Pair...");

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

        int testNumber = 1;

        // Tests for n <= 2000
        for (int size : smallSizes) {

            for (int test = 0; test < 5; test++) {

                Point[] points = generateRandomPoints(size);

                double expected =
                        bruteForceClosestPair(points);

                ClosestPairSolver solver =
                        new ClosestPairSolver();

                long start = System.nanoTime();

                double actual =
                        solver.solve(points);

                long end = System.nanoTime();

                double timeMs =
                        (end - start) / 1_000_000.0;

                assertEqual(
                        expected,
                        actual,
                        testNumber,
                        size,
                        "RANDOM"
                );

                System.out.printf(
                        "Test #%3d | %-6s | size: %5d | time: %8.3f ms | PASS%n",
                        testNumber,
                        "RANDOM",
                        size,
                        timeMs
                );

                testNumber++;
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

        long duplicateStart = System.nanoTime();

        double result =
                solver.solve(duplicatePoints);

        long duplicateEnd = System.nanoTime();

        double duplicateTime =
                (duplicateEnd - duplicateStart) / 1_000_000.0;

        if (result != 0.0) {
            System.out.printf(
                    "Test #%3d | %-6s | size: %5d | FAILED%n",
                    testNumber,
                    "DUPLICATE",
                    duplicatePoints.length
            );

            throw new AssertionError(
                    "Duplicate point test failed"
            );
        }

        System.out.printf(
                "Test #%3d | %-9s | size: %5d | time: %8.3f ms | PASS%n",
                testNumber,
                "DUPLICATE",
                duplicatePoints.length,
                duplicateTime
        );

        testNumber++;

        // Large dataset
        Point[] largePoints =
                generateRandomPoints(100_000);

        long start = System.nanoTime();

        double largeResult =
                solver.solve(largePoints);

        long end = System.nanoTime();

        double largeTime =
                (end - start) / 1_000_000.0;

        if (!Double.isFinite(largeResult)) {
            System.out.printf(
                    "Test #%3d | %-6s | size: %5d | FAILED%n",
                    testNumber,
                    "LARGE",
                    largePoints.length
            );

            throw new AssertionError(
                    "Closest Pair failed on large dataset"
            );
        }

        System.out.printf(
                "Test #%3d | %-6s | size: %5d | time: %8.3f ms | PASS%n",
                testNumber,
                "LARGE",
                largePoints.length,
                largeTime
        );

        System.out.println();
        System.out.println("Closest Pair: ALL TESTS PASSED");
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
            double actual,
            int testNumber,
            int size,
            String type) {

        double epsilon = 1e-9;

        if (Math.abs(expected - actual) > epsilon) {

            System.out.printf(
                    "Test #%3d | %-6s | size: %5d | FAILED%n",
                    testNumber,
                    type,
                    size
            );

            throw new AssertionError(
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