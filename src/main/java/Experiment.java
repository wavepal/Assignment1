package main.java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Experiment {

    private static final Random RANDOM = new Random(42);

    private static final int[] SIZES = {
            100,
            1_000,
            10_000,
            50_000,
            100_000
    };

    public static void run() {

        File resultsDirectory = new File("results");

        if (!resultsDirectory.exists()) {
            resultsDirectory.mkdirs();
        }

        File csvFile = new File(
                resultsDirectory,
                "results.csv"
        );

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(csvFile))) {

            writer.println(
                    "algorithm,input_type,size,time_ns," +
                            "max_recursion_depth,comparisons," +
                            "swaps,recursive_calls"
            );

            runSortingExperiments(writer);
            runSelectionExperiments(writer);
            runClosestPairExperiments(writer);

            System.out.println(
                    "Results saved to: " +
                            csvFile.getAbsolutePath()
            );

        } catch (IOException e) {
            System.err.println(
                    "Could not save CSV: " +
                            e.getMessage()
            );
        }
    }

    private static void runSortingExperiments(
            PrintWriter writer) {

        String[] types = {
                "Random",
                "Sorted",
                "Reverse-sorted",
                "Duplicate-heavy"
        };

        for (int size : SIZES) {

            for (String type : types) {

                int[] original =
                        generateArray(size, type);

                runMergeSort(
                        writer,
                        original,
                        type,
                        size
                );

                runQuickSort(
                        writer,
                        original,
                        type,
                        size
                );
            }
        }
    }

    private static void runMergeSort(
            PrintWriter writer,
            int[] original,
            String type,
            int size) {

        int[] array = original.clone();

        MergeSorter sorter = new MergeSorter();

        long start = System.nanoTime();

        sorter.sort(array);

        long end = System.nanoTime();

        checkSorted(array);

        writer.printf(
                "MergeSort,%s,%d,%d,%d,%d,%d,%d%n",
                type,
                size,
                end - start,
                sorter.getMaxRecursionDepth(),
                sorter.getComparisons(),
                0,
                sorter.getRecursiveCalls()
        );
    }

    private static void runQuickSort(
            PrintWriter writer,
            int[] original,
            String type,
            int size) {

        int[] array = original.clone();

        QuickSorter sorter = new QuickSorter();

        long start = System.nanoTime();

        sorter.sort(array);

        long end = System.nanoTime();

        checkSorted(array);

        writer.printf(
                "QuickSort,%s,%d,%d,%d,%d,%d,%d%n",
                type,
                size,
                end - start,
                sorter.getMaxRecursionDepth(),
                sorter.getComparisons(),
                sorter.getSwaps(),
                sorter.getRecursiveCalls()
        );
    }

    private static void runSelectionExperiments(
            PrintWriter writer) {

        // IMPORTANT 1: SIZES
        int[] sizes = {
                100,
                1_000,
                10_000,
                50_000,
                100_000
        };

        String[] types = {
                "Random",
                "Sorted",
                "Reverse-sorted",
                "Duplicate-heavy"
        };

        for (int size : sizes) {

            for (String type : types) {

                int[] original =
                        generateArray(size, type);

                int k = size / 2;

                DeterministicSelector selector =
                        new DeterministicSelector();

                int[] array = original.clone();

                long start = System.nanoTime();

                int result =
                        selector.select(array, k);

                long end = System.nanoTime();

                // Verify result.
                int[] verification =
                        original.clone();

                Arrays.sort(verification);

                if (result != verification[k]) {
                    throw new IllegalStateException(
                            "Deterministic Select failed"
                    );
                }

                writer.printf(
                        "DeterministicSelect,%s,%d,%d,%d,%d,%d,%d%n",
                        type,
                        size,
                        end - start,
                        selector.getMaxRecursionDepth(),
                        selector.getComparisons(),
                        selector.getSwaps(),
                        selector.getRecursiveCalls()
                );
            }
        }
    }

    private static void runClosestPairExperiments(
            PrintWriter writer) {

        // IMPORTANT 2: SIZES
        int[] sizes = {
                100,
                1_000,
                10_000,
                50_000
        };

        String[] types = {
                "Random",
                "Duplicate-heavy"
        };

        for (int size : sizes) {

            for (String type : types) {

                Point[] points =
                        generatePoints(size, type);

                ClosestPairSolver solver =
                        new ClosestPairSolver();

                long start = System.nanoTime();

                double result =
                        solver.solve(points);

                long end = System.nanoTime();

                if (points.length >= 2 &&
                        !Double.isFinite(result)) {

                    throw new IllegalStateException(
                            "Closest Pair failed"
                    );
                }

                writer.printf(
                        "ClosestPair,%s,%d,%d,%d,%d,%d,%d%n",
                        type,
                        size,
                        end - start,
                        solver.getMaxRecursionDepth(),
                        solver.getComparisons(),
                        0,
                        solver.getRecursiveCalls()
                );
            }
        }
    }

    private static int[] generateArray(
            int size,
            String type) {

        int[] array = new int[size];

        switch (type) {
            case "Random":

                for (int i = 0; i < size; i++) {
                    array[i] =
                            RANDOM.nextInt(size * 10 + 1);
                }

                break;

            case "Sorted":

                for (int i = 0; i < size; i++) {
                    array[i] = i;
                }

                break;

            case "Reverse-sorted":

                for (int i = 0; i < size; i++) {
                    array[i] = size - i;
                }

                break;

            case "Duplicate-heavy":

                for (int i = 0; i < size; i++) {
                    array[i] =
                            RANDOM.nextInt(10);
                }

                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown input type: " + type
                );
        }

        return array;
    }

    private static Point[] generatePoints(
            int size,
            String type) {

        Point[] points = new Point[size];

        for (int i = 0; i < size; i++) {

            double x;
            double y;

            if (type.equals("Duplicate-heavy")) {

                x = RANDOM.nextInt(100);
                y = RANDOM.nextInt(100);

            } else {

                x = RANDOM.nextDouble() * size;
                y = RANDOM.nextDouble() * size;
            }

            points[i] = new Point(x, y);
        }

        return points;
    }

    private static void checkSorted(int[] array) {

        for (int i = 1; i < array.length; i++) {

            if (array[i - 1] > array[i]) {
                throw new IllegalStateException(
                        "Array is not sorted"
                );
            }
        }
    }
}