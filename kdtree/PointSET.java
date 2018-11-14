/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("insert null");
        }

        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("contains null");
        }

        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point2D : set) {
            point2D.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("contains null");
        }

        ArrayList<Point2D> list = new ArrayList<>();
        for (Point2D point2D : set) {
            if (point2D.x() >= rect.xmin() && point2D.x() <= rect.xmax()
                    && point2D.y() >= rect.ymin() && point2D.y() <= rect.ymax()) {
                list.add(point2D);
            }
        }

        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("nearest null");
        }

        double nearestD = Double.POSITIVE_INFINITY;
        Point2D nearestP = null;
        for (Point2D point2D : set) {
            double d = p.distanceSquaredTo(point2D);

            if (d < nearestD) {
                nearestD = d;
                nearestP = point2D;
            }
        }

        return nearestP;
    }
}
