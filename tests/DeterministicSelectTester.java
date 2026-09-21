import main.java.DeterministicSelector;

import java.util.Arrays;
import java.util.Random;

public class DeterministicSelectTester {

    private static final Random RANDOM = new Random(44);

    public static void runTests() {

        System.out.println(
                "Testing Deterministic Select..."
        );

        // 100 random tests
        for (int test = 1; test <= 100; test++) {

            int size = 1 + RANDOM.nextInt(500);

            int[] original =
                    generateRandomArray(size);

            int k = RANDOM.nextInt(size);

            int[] reference =
                    original.clone();

            Arrays.sort(reference);

            int expected = reference[k];

            DeterministicSelector selector =
                    new DeterministicSelector();

            int actual =
                    selector.select(
                            original.clone(),
                            k
                    );

            if (actual != expected) {

                throw new AssertionError(
                        "Deterministic Select failed.\n" +
                                "Test: " + test +
                                "\nSize: " + size +
                                "\nk: " + k +
                                "\nExpected: " + expected +
                                "\nActual: " + actual
                );
            }
        }

        // Edge case
        testCase(new int[]{42}, 0);

        testCase(
                new int[]{5, 5, 5, 5, 5},
                2
        );

        testCase(
                new int[]{9, 8, 7, 6, 5},
                0
        );

        testCase(
                new int[]{9, 8, 7, 6, 5},
                4
        );

        testCase(
                new int[]{-5, 2, -10, 8, 0},
                2
        );

        System.out.println(
                "Deterministic Select: PASSED " +
                        "(100 random tests)"
        );
    }

    private static void testCase(
            int[] original,
            int k) {

        int[] reference =
                original.clone();

        Arrays.sort(reference);

        int expected = reference[k];

        DeterministicSelector selector =
                new DeterministicSelector();

        int actual =
                selector.select(
                        original.clone(),
                        k
                );

        if (actual != expected) {

            throw new AssertionError(
                    "Deterministic Select edge case failed"
            );
        }
    }

    private static int[] generateRandomArray(
            int size) {

        int[] array = new int[size];

        for (int i = 0; i < size; i++) {

            // Small range creates duplicates
            array[i] =
                    RANDOM.nextInt(2001) - 1000;
        }

        return array;
    }

    public static void main(String[] args) {
        runTests();
    }
}