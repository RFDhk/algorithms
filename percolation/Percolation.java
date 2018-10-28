public class Percolation {

    private final boolean[] sites;
    private final int[] sizes;
    private final int[] ids;
    private final int n;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        int size = n * n + 1;

        sites = new boolean[size];
        sizes = new int[size];
        ids = new int[size];
        for (int i = 0; i < size; i++) {
            sites[i] = false; // closed
            sizes[i] = 1;
            ids[i] = i;
        }
        sites[0] = true;
    }

    private int root(int i) {
        while (i != ids[i])
            i = ids[i];
        return i;
    }

    private void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j) return;
        if (sizes[i] < sizes[j]) {
            ids[i] = j;
            sizes[j] += sizes[i];
        } else {
            ids[j] = i;
            sizes[i] += sizes[j];
        }

    }

    private int indexOf(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("row:" + row + ", col:" + col);
        }
        return (row - 1) * n + col;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        int i = indexOf(row, col);
        if (sites[i]) {
            return;
        } else {
            sites[i] = true;
        }

        int top = Math.max(i - n, 0);
        if (sites[top]) {
            union(i, top);
        }

        int bottom = i + n;
        if (bottom < ids.length && sites[bottom]) {
            union(i, bottom);
        }

        int left = i - 1;
        if (left % n != 0 && sites[left]) {
            union(i, i - 1);
        }

        int right = i + 1;
        if (i % n != 0 && sites[right]) {
            union(i, i + 1);
        }

    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        return sites[indexOf(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        return root(0) == root(indexOf(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        int c = 0;
        for (int i = 1; i < sites.length; i++) {
            if (sites[i]) c++;
        }
        return c;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int i = n; i > 0; i--) {
            if (root(0) == root(sites.length - i)) {
                return true;
            }
        }
        return false;
    }
}