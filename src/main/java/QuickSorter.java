import java.util.concurrent.ThreadLocalRandom;

public class QuickSorter {
    private long comparisons;
    private long swaps;
    private long recursiveCalls;
    private int maxRecursionDepth;

    public void sort(int[] array) {
        comparisons = 0;
        swaps = 0;
        recursiveCalls = 0;
        maxRecursionDepth = 0;

        if (array == null || array.length < 2) {
            return;
        }

        quickSort(array, 0, array.length - 1, 1);
    }

    private void quickSort(int[] array, int low, int high, int depth) {
        while (low < high) {
            int pivotIndex = ThreadLocalRandom.current().nextInt(low, high + 1);

            swap(array, pivotIndex, high);

            int partitionIndex = partition(array, low, high);

            int leftSize = partitionIndex - low;
            int rightSize = high - partitionIndex;

            // Recurse in smaller sections
            if (leftSize < rightSize) {

                recursiveCalls++;
                maxRecursionDepth = Math.max(maxRecursionDepth, depth);

                quickSort(array, low, partitionIndex - 1, depth + 1);

                // Iteration over bigger section
                low = partitionIndex + 1;

            } else {

                recursiveCalls++;
                maxRecursionDepth =
                        Math.max(maxRecursionDepth, depth);

                quickSort(array, partitionIndex + 1, high, depth + 1);

                // Iterate over larger partition.
                high = partitionIndex - 1;
            }
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low;

        for (int j = low; j < high; j++) {

            comparisons++;

            if (array[j] <= pivot) {
                swap(array, i, j);
                i++;
            }
        }

        swap(array, i, high);

        return i;
    }

    private void swap(int[] array, int i, int j) {
        if (i == j) {
            return;
        }

        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;

        swaps++;
    }

//    returns
    public long getComparisons() {
        return comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    public long getRecursiveCalls() {
        return recursiveCalls;
    }

    public int getMaxRecursionDepth() {
        return maxRecursionDepth;
    }
}