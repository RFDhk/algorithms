/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.Arrays;

public class BruteCollinearPoints {

    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] pp) {
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

        LineSegment[] auxSegments = new LineSegment[points.length];
        int count = 0;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        double slope = points[i].slopeTo(points[j]);

                        if (equalD(slope, points[i].slopeTo(points[k]))
                                && equalD(slope, points[i].slopeTo(points[m]))) {
                            auxSegments[count++] = new LineSegment(points[i], points[m]);
                            break;
                        }
                    }
                }
            }
        }


        segments = new LineSegment[count];
        System.arraycopy(auxSegments, 0, segments, 0, count);


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
        In in = new In("input8.txt");
        int count = in.readInt();
        Point[] points = new Point[count];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
        }

        BruteCollinearPoints b = new BruteCollinearPoints(points);
        System.out.print(b.numberOfSegments());
    }
}
