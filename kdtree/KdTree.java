import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;

public class KdTree {

    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        root = insert0(root, p, 0);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains0(root, p);
    }

    // draw all points to standard draw
    public void draw() {
        draw0(root);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        LinkedList<Point2D> list = new LinkedList<>();
        range0(root, rect, list);
        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }
        return nearest0(root, p, root.getValue());
    }


    private Node insert0(Node node, Point2D point2D, int level) {
        if (node == null) {
            size++;
            return new Node(point2D, level);
        }

        int cmp = compareKey(node.getLevel(), node.getValue(), point2D);

        if (cmp < 0) {
            node.setLeft(insert0(node.getLeft(), point2D, level + 1));
        } else if (cmp > 0) {
            node.setRight(insert0(node.getRight(), point2D, level + 1));
        } else {
            node.setValue(point2D);
        }

        return node;
    }

    private boolean contains0(Node node, Point2D point2D) {
        if (node == null) {
            return false;
        }

        int cmp = compareKey(node.getLevel(), node.getValue(), point2D);

        if (cmp < 0) {
            return contains0(node.getLeft(), point2D);
        } else if (cmp > 0) {
            return contains0(node.getRight(), point2D);
        } else {
            return true;
        }
    }

    private void draw0(Node node) {
        if (node == null) {
            return;
        }

        node.getValue().draw();
        draw0(node.getLeft());
        draw0(node.getRight());
    }

    private void range0(Node node, RectHV rectHV, LinkedList<Point2D> list) {
        if (node == null) {
            return;
        }

        Point2D p = node.getValue();

        if (p.x() >= rectHV.xmin() && p.x() <= rectHV.xmax()
                && p.y() >= rectHV.ymin() && p.y() <= rectHV.ymax()) {
            list.add(p);
            range0(node.getLeft(), rectHV, list);
            range0(node.getRight(), rectHV, list);
        } else if (compareKey(node.getLevel(), node.getValue(), rectHV.xmin(), rectHV.ymin()) < 0) {
            range0(node.getLeft(), rectHV, list);
        } else if (compareKey(node.getLevel(), node.getValue(), rectHV.xmax(), rectHV.ymax()) > 0) {
            range0(node.getRight(), rectHV, list);
        } else {
            range0(node.getLeft(), rectHV, list);
            range0(node.getRight(), rectHV, list);
        }
    }

    private Point2D nearest0(Node node, Point2D queryP, Point2D nearestP) {
        if (node == null) {
            return nearestP;
        }

        if (queryP.distanceSquaredTo(nearestP) > queryP.distanceSquaredTo(node.getValue())) {
            nearestP = node.getValue();
        }

        int cmp = compareKey(node.getLevel(), node.getValue(), queryP);

        if (cmp < 0) {
            nearestP = nearest0(node.getLeft(), queryP, nearestP);
        } else if (cmp > 0) {
            nearestP = nearest0(node.getRight(), queryP, nearestP);
        } else {
            return node.getValue();
        }


        Point2D ortP;
        if (node.getLevel() % 2 != 0) {
            ortP = new Point2D(queryP.x(), node.getValue().y());
        } else {
            ortP = new Point2D(node.getValue().x(), queryP.y());
        }

        if (queryP.distanceSquaredTo(nearestP) > queryP.distanceSquaredTo(ortP)) {
            if (cmp < 0) {
                nearestP = nearest0(node.getRight(), queryP, nearestP);
            } else {
                nearestP = nearest0(node.getLeft(), queryP, nearestP);
            }
        }

        return nearestP;
    }

    private int compareKey(int level, Point2D p1, Point2D p2) {
        return compareKey(level, p1, p2.x(), p2.y());
    }

    private int compareKey(int level, Point2D p1, double x2, double y2) {
        int cmp;
        if ((level % 2) != 0) {
            cmp = Double.compare(p1.y(), y2);
            if (cmp == 0) {
                cmp = Double.compare(p1.x(), x2);
            }
        } else {
            cmp = Double.compare(p1.x(), x2);
            if (cmp == 0) {
                cmp = Double.compare(p1.y(), y2);
            }
        }

        return cmp;
    }

    private class Node {
        private final int level;
        private Point2D value;
        private Node left;
        private Node right;

        public Node(Point2D value, int level) {
            this.value = value;
            this.level = level;
        }

        public Point2D getValue() {
            return value;
        }

        public int getLevel() {
            return level;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public void setValue(Point2D value) {
            this.value = value;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }
}
