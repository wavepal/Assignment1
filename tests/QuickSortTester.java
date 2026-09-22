import main.java.QuickSorter;

import java.util.Arrays;
import java.util.Random;

public class QuickSortTester {

    private static final Random RANDOM = new Random(43);

    public static void runTests() {

        System.out.println("Testing QuickSort...");

        int[][] testCases = {
                {},
                {42},
                {2, 1},
                {1, 2, 3, 4, 5},
                {5, 4, 3, 2, 1},
                {5, 1, 5, 2, 5, 3, 5},
                {-5, 3, -1, 8, 0, -10},
                {7, 7, 7, 7}
        };

        int testNumber = 1;

        // Edge cases
        for (int[] testCase : testCases) {
            test(testCase, testNumber++, "EDGE");
        }

        // Random tests
        for (int i = 0; i < 100; i++) {
            int size = RANDOM.nextInt(500);
            test(generateRandomArray(size), testNumber++, "RANDOM");
        }

        System.out.println();
        System.out.println("QuickSort: ALL TESTS PASSED");
    }

    private static void test(int[] original, int testNumber, String type) {

        int[] expected = original.clone();
        int[] actual = original.clone();

        Arrays.sort(expected);

        QuickSorter sorter = new QuickSorter();

        long start = System.nanoTime();
        sorter.sort(actual);
        long end = System.nanoTime();

        double timeMs = (end - start) / 1_000_000.0;

        if (!Arrays.equals(expected, actual)) {
            System.out.println(
                    "Test #" + testNumber +
                            " [" + type + "] FAILED"
            );

            throw new AssertionError(
                    "Expected: " + Arrays.toString(expected) +
                            "\nActual: " + Arrays.toString(actual)
            );
        }

        System.out.printf(
                "Test #%3d | %-6s | size: %3d | time: %8.3f ms | PASS%n",
                testNumber,
                type,
                original.length,
                timeMs
        );
    }

    private static int[] generateRandomArray(int size) {

        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            array[i] = RANDOM.nextInt(2001) - 1000;
        }

        return array;
    }

    public static void main(String[] args) {
        runTests();
    }
}