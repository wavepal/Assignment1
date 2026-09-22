import main.java.DeterministicSelector;

import java.util.Arrays;
import java.util.Random;

public class DeterministicSelectTester {

    private static final Random RANDOM = new Random(44);

    public static void runTests() {

        System.out.println("Testing Deterministic Select...");

        int testNumber = 1;

        // 100 random tests
        for (int test = 1; test <= 100; test++) {

            int size = 1 + RANDOM.nextInt(500);

            int[] original = generateRandomArray(size);

            int k = RANDOM.nextInt(size);

            int[] reference = original.clone();

            Arrays.sort(reference);

            int expected = reference[k];

            DeterministicSelector selector = new DeterministicSelector();

            long start = System.nanoTime();

            int actual = selector.select(
                    original.clone(),
                    k
            );

            long end = System.nanoTime();

            double timeMs = (end - start) / 1_000_000.0;

            if (actual != expected) {

                System.out.printf(
                        "Test #%3d | RANDOM | size: %3d | k: %3d | FAILED%n",
                        testNumber,
                        size,
                        k
                );

                throw new AssertionError(
                        "Expected: " + expected +
                                "\nActual: " + actual
                );
            }

            System.out.printf(
                    "Test #%3d | RANDOM | size: %3d | k: %3d | time: %8.3f ms | PASS%n",
                    testNumber,
                    size,
                    k,
                    timeMs
            );

            testNumber++;
        }

        // Edge cases
        testCase(new int[]{42}, 0, testNumber++, "EDGE");

        testCase(
                new int[]{5, 5, 5, 5, 5},
                2,
                testNumber++,
                "EDGE"
        );

        testCase(
                new int[]{9, 8, 7, 6, 5},
                0,
                testNumber++,
                "EDGE"
        );

        testCase(
                new int[]{9, 8, 7, 6, 5},
                4,
                testNumber++,
                "EDGE"
        );

        testCase(
                new int[]{-5, 2, -10, 8, 0},
                2,
                testNumber++,
                "EDGE"
        );

        System.out.println();
        System.out.println("Deterministic Select: ALL TESTS PASSED");
    }

    private static void testCase(
            int[] original,
            int k,
            int testNumber,
            String type) {

        int[] reference = original.clone();

        Arrays.sort(reference);

        int expected = reference[k];

        DeterministicSelector selector = new DeterministicSelector();

        long start = System.nanoTime();

        int actual = selector.select(
                original.clone(),
                k
        );

        long end = System.nanoTime();

        double timeMs = (end - start) / 1_000_000.0;

        if (actual != expected) {

            System.out.printf(
                    "Test #%3d | %-6s | size: %3d | k: %3d | FAILED%n",
                    testNumber,
                    type,
                    original.length,
                    k
            );

            throw new AssertionError(
                    "Expected: " + expected +
                            "\nActual: " + actual
            );
        }

        System.out.printf(
                "Test #%3d | %-6s | size: %3d | k: %3d | time: %8.3f ms | PASS%n",
                testNumber,
                type,
                original.length,
                k,
                timeMs
        );
    }

    private static int[] generateRandomArray(int size) {

        int[] array = new int[size];

        for (int i = 0; i < size; i++) {

            // Small range creates duplicates
            array[i] = RANDOM.nextInt(2001) - 1000;
        }

        return array;
    }

    public static void main(String[] args) {
        runTests();
    }
}