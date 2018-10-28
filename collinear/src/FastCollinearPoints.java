import edu.princeton.cs.algs4.In;

import java.util.Arrays;

public class FastCollinearPoints {

    private final LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] pp) {
        if (pp == null) {
            throw new IllegalArgumentException("null");
        }

        Point[] points = Arrays.copyOf(pp, pp.length);

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("null " + i);
            }
            for (int j = i; j > 0; j--) {
                if (points[j].compareTo(points[j - 1]) < 0) {
                    Point p = points[j - 1];
                    points[j - 1] = points[j];
                    points[j] = p;
                } else if ((points[j].compareTo(points[j - 1]) == 0)) {
                    throw new IllegalArgumentException("duplicate");
                } else {
                    break;
                }
            }
        }


        Point2[] auxSegments = new Point2[points.length * points.length];
        int segmentsCount = 0;
        for (int i = 0; i < points.length - 2; i++) {
            Point center = points[i];
            double[] slopes = new double[points.length - i - 1];
            int[] indexes = new int[points.length - i - 1];
            for (int j = i + 1; j < points.length; j++) {
                slopes[j - i - 1] = center.slopeTo(points[j]);
                indexes[j - i - 1] = j;
            }

            for (int v = 0; v < slopes.length; v++) {
                for (int j = v; j > 0; j--) {
                    if (slopes[j] < slopes[j - 1]) {
                        double d = slopes[j - 1];
                        int index = indexes[j - 1];
                        slopes[j - 1] = slopes[j];
                        indexes[j - 1] = indexes[j];
                        slopes[j] = d;
                        indexes[j] = index;
                    } else {
                        break;
                    }
                }
            }

            int count = 1;
            double curSlope = slopes[0];
            for (int n = 1; n < slopes.length; n++) {
                if (equalD(curSlope, slopes[n])) {
                    count++;
                } else {
                    if (count >= 3) {
                        Point p = points[indexes[n - 1]];
                        for (int j = 0; j < segmentsCount; j++) {
                            Point2 auxSegment = auxSegments[j];
                            if (equalD(auxSegment.getP1().slopeTo(p), curSlope) && auxSegment.getP2().equals(p)) {
                                p = null;
                                break;
                            }
                        }
                        if (p != null) {
                            auxSegments[segmentsCount++] = new Point2(points[i], p);
                        }
                    }
                    count = 1;
                    curSlope = slopes[n];
                }
            }
            if (count >= 3) {
                Point p = points[indexes[slopes.length - 1]];
                for (int j = 0; j < segmentsCount; j++) {
                    Point2 auxSegment = auxSegments[j];
                    if (equalD(auxSegment.getP1().slopeTo(p), curSlope) && auxSegment.getP2().equals(p)) {
                        p = null;
                        break;
                    }
                }
                if (p != null) {
                    int a = indexes[slopes.length - 1];
                    Point2 p2 = new Point2(points[i], points[a]);
                    auxSegments[segmentsCount++] = p2;
                }
            }
        }

        segments = new LineSegment[segmentsCount];
        for (int i = 0; i < segmentsCount; i++) {
            Point2 auxSegment = auxSegments[i];
            segments[i] = new LineSegment(auxSegment.getP1(), auxSegment.getP2());

        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }

    private boolean equalD(double d1, double d2) {
        return d1 == d2 || (Math.abs(d1 - d2) < 1e-12);
    }

    public static void main(String[] args) {
        In in = new In("equidistant.txt");
        int count = in.readInt();
        Point[] points = new Point[count];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
        }

        FastCollinearPoints b = new FastCollinearPoints(points);
        System.out.print(b.numberOfSegments());
    }

    private class Point2 {
        private final Point p1;
        private final Point p2;

        Point2(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        Point getP1() {
            return p1;
        }

        Point getP2() {
            return p2;
        }
    }
}
