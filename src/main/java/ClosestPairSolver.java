package main.java;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {
//    divide-and-conquer
    private long comparisons;
    private long recursiveCalls;
    private int maxRecursionDepth;

    private Point bestPoint1;
    private Point bestPoint2;
    private double bestDistanceSquared;

    public double solve(Point[] points) {
        comparisons = 0;
        recursiveCalls = 0;
        maxRecursionDepth = 0;

        bestPoint1 = null;
        bestPoint2 = null;
        bestDistanceSquared = Double.POSITIVE_INFINITY;

        if (points == null || points.length < 2) {
            return Double.POSITIVE_INFINITY;
        }

        Point[] sortedByX = points.clone();

        Arrays.sort(
                sortedByX,
                Comparator.comparingDouble(p -> p.x)
        );

        Point[] sortedByY = points.clone();

        Arrays.sort(
                sortedByY,
                Comparator.comparingDouble(p -> p.y)
        );

        closestPair(
                sortedByX,
                sortedByY,
                0,
                sortedByX.length - 1,
                1
        );

        return Math.sqrt(bestDistanceSquared);
    }

    private void closestPair(Point[] pointsByX, Point[] pointsByY, int left, int right, int depth) {

        recursiveCalls++;
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);

        int n = right - left + 1;

        if (n <= 3) {
            bruteForce(pointsByX, left, right);
            return;
        }

        int mid = left + (right - left) / 2;

        double midX = pointsByX[mid].x;

        Point[] leftX = Arrays.copyOfRange(pointsByX, left, mid + 1);

        Point[] rightX = Arrays.copyOfRange(pointsByX, mid + 1, right + 1);

        Point[] leftY = new Point[leftX.length];
        Point[] rightY = new Point[rightX.length];

        int leftCount = 0;
        int rightCount = 0;

        for (Point p : pointsByY) {

            if (p.x < midX ||
                    (p.x == midX && belongsToLeft(p, leftX))) {

                leftY[leftCount++] = p;

            } else {
                rightY[rightCount++] = p;
            }
        }

        closestPair(
                leftX,
                leftY,
                0,
                leftX.length - 1,
                depth + 1
        );

        closestPair(
                rightX,
                rightY,
                0,
                rightX.length - 1,
                depth + 1
        );

        double delta = Math.sqrt(bestDistanceSquared);

        Point[] strip = new Point[pointsByY.length];
        int stripSize = 0;

        for (Point p : pointsByY) {

            if (Math.abs(p.x - midX) < delta) {
                strip[stripSize++] = p;
            }
        }

        // pointsByY already sorted by y
        for (int i = 0; i < stripSize; i++) {

            for (int j = i + 1;
                 j < stripSize &&
                         strip[j].y - strip[i].y < delta;
                 j++) {

                comparisons++;

                updateBestPair(strip[i], strip[j]);

                delta = Math.sqrt(bestDistanceSquared);
            }
        }
    }

    private boolean belongsToLeft(Point p, Point[] leftX) {

        for (Point point : leftX) {
            if (point == p) {
                return true;
            }
        }

        return false;
    }

    private void bruteForce(Point[] points, int left, int right) {

        for (int i = left; i <= right; i++) {

            for (int j = i + 1; j <= right; j++) {

                comparisons++;

                updateBestPair(points[i], points[j]);
            }
        }
    }

    private void updateBestPair(Point a, Point b) {

        double distanceSquared =
                a.distanceSquared(b);

        if (distanceSquared < bestDistanceSquared) {

            bestDistanceSquared = distanceSquared;
            bestPoint1 = a;
            bestPoint2 = b;
        }
    }

//    returns

    public Point getBestPoint1() {
        return bestPoint1;
    }

    public Point getBestPoint2() {
        return bestPoint2;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getRecursiveCalls() {
        return recursiveCalls;
    }

    public int getMaxRecursionDepth() {
        return maxRecursionDepth;
    }
}