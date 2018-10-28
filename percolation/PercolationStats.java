import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] opens;
    private static final double CONFIDENCE_95 = 1.96d;
    private double meanVar = -1.0d;
    private double stddevVar = -1.0d;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n:" + n + ", trials:" + trials);
        }


        opens = new double[trials];
        double n2 = n*n;
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            }
            opens[i] = ((double) percolation.numberOfOpenSites()) / n2;
        }
    }

    // sample meanVar of percolation threshold
    public double mean() {
        if (meanVar < 0.0d)
            meanVar = StdStats.mean(opens);
        return meanVar;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stddevVar < 0.0d)
            stddevVar = StdStats.stddev(opens);
        return stddevVar;

    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(opens.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(opens.length));
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("meanVar                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}