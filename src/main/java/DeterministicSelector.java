public class DeterministicSelector {
//    This is "Median-of-Medians" algorithm using groups of 5
//    This method returns k-th element in sorted order (0-based)
//    EXAMPLE:[10, 5, 7, 1, 20]
//    k = 0 → 1
//    k = 2 → 7
//    k = 4 → 20
    // note: this comments is not AI-generated, just explanation and reminder

    private long comparisons;
    private long swaps;
    private long recursiveCalls;
    private int maxRecursionDepth;

    public int select(int[] array, int k) {

        comparisons = 0;
        swaps = 0;
        recursiveCalls = 0;
        maxRecursionDepth = 0;

        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }

        if (k < 0 || k >= array.length) {
            throw new IllegalArgumentException("Invalid k: " + k);
        }

        return select(array, 0, array.length - 1, k, 1);
    }

    private int select(int[] array, int left, int right, int k, int depth) {

        recursiveCalls++;
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);

        if (left == right) {
            return array[left];
        }

        int pivotValue = medianOfMedians(array, left, right);

        int[] equalRange = partition(
                array, left, right, pivotValue
        );

        int equalStart = equalRange[0];
        int equalEnd = equalRange[1];

        if (k < equalStart) {
            return select(array, left, equalStart - 1, k, depth + 1);
        }

        if (k > equalEnd) {
            return select(array, equalEnd + 1, right, k, depth + 1);
        }

        return array[k];
    }

    private int medianOfMedians(int[] array, int left, int right) {
        int n = right - left + 1;

        if (n <= 5) {
            insertionSort(array, left, right);

            return array[left + n / 2];
        }

        int numberOfGroups = (n + 4) / 5;

        for (int i = 0; i < numberOfGroups; i++) {

            int groupLeft = left + i * 5;
            int groupRight = Math.min(groupLeft + 4, right);

            insertionSort(array, groupLeft, groupRight);

            int medianIndex = groupLeft + (groupRight - groupLeft) / 2;

            swap(array, left + i, medianIndex);
        }

        return select(
                array,
                left,
                left + numberOfGroups - 1,
                left + numberOfGroups / 2,
                1
        );
    }

    private int[] partition(int[] array, int left, int right, int pivotValue) {
        int less = left;
        int current = left;
        int greater = right;

        while (current <= greater) {

            comparisons++;

            if (array[current] < pivotValue) {

                swap(array, less, current);
                less++;
                current++;

            } else {

                comparisons++;

                if (array[current] > pivotValue) {

                    swap(array, current, greater);
                    greater--;

                } else {
                    current++;
                }
            }
        }

        return new int[]{less, greater};
    }

    private void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {

            int key = array[i];
            int j = i - 1;

            while (j >= left) {

                comparisons++;

                if (array[j] <= key) {
                    break;
                }

                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = key;
        }
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