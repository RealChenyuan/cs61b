package hw2;

import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double[] fractions;

    private int T;

    private final int seed = 1234;
    private StdRandom random;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        // perform T independent experiments on an N-by-N grid
        if (N <= 0 || T <= 0) throw new IllegalArgumentException();
        random.setSeed(seed);
        for (int i=0; i<T; i++) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                int x = random.uniform(N);
                int y = random.uniform(N);
                p.open(x, y);
            }
            fractions[i] = p.numberOfOpenSites()/(N*N);
        }
        this.T = T;
    }
    public double mean() {
        // sample mean of percolation threshold
        double sum = 0.0;
        for (int i=0; i<T; i++) {
            sum += fractions[i];
        }
        return sum/T;
    }
    public double stddev() {
        // sample standard deviation of percolation threshold
        double mean = mean();
        double SSE = 0.0;
        for (int i=0; i<T; i++) {
            SSE += (fractions[i] - mean)*(fractions[i] - mean);
        }
        return Math.sqrt(SSE/(T-1));
    }
    public double confidenceLow() {
        // low endpoint of 95% confidence interval
        return mean()-1.96*stddev()/Math.sqrt(T);
    }
    public double confidenceHigh() {
        return mean()+1.96*stddev()/Math.sqrt(T);
    }
}
