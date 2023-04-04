package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF grid;
    private int[] type;
    private int N;
    private int openSize;

    public Percolation(int N) {
        // create N-by-N grid, with all sites initially blocked
        // blocked: 0; empty open site: 1; full open site: 2
        if (N<0) {
            throw new IllegalArgumentException();
        }
//        grid = new WeightedQuickUnionUF(N*N+1);
//        type = new int[N*N+1];
//        for (int i=0; i<=N*N; i++) {
//            type[i] = 0;
//        }
//        this.N = N;
//        openSize = 0;
//        for(int i=N*(N-1); i<N*N; i++) {
//            grid.union(N*N, i);
//        }
        grid = new WeightedQuickUnionUF(N*N);
        type = new int[N*N];
        for (int i=0; i<N*N; i++) {
            type[i] = 0;
        }
        this.N = N;
        openSize = 0;
    }
    public void open(int row, int col) {
        // open the site (row, col) if it is not open already
        int k = xyToX(row, col);
        if (k < 0 || k >= N*N) {
            throw new IndexOutOfBoundsException();
        }
        if (isOpen(row, col)) return;
        if (k < N) {
            type[k] = 2;
        } else {
            type[k] = 1;
        }
        openSize++;
        if (row-1 >= 0){
            if (isOpen(row-1, col)) changeType(k, k-N);
        }
        if (col-1 >= 0){
            if (isOpen(row, col-1)) changeType(k, k-1);
        }
        if (row+1 < N){
            if (isOpen(row+1, col)) changeType(k, k+N);
        }
        if (col+1 < N){
            if (isOpen(row, col+1)) changeType(k, k+1);
        }
    }

    private void changeType (int k1, int k2) {
        int p1 = grid.find(k1);
        int p2 = grid.find(k2);
        grid.union(k1, k2);
        int p = grid.find(k1);
        if (Math.max(type[p1], type[p2]) == 2) {
            type[p] = 2;
        }
    }

    public boolean isOpen(int row, int col) {
        // is the site (row, col) open?
        int k = xyToX(row, col);
        if (k < 0 || k >= N*N) {
            throw new IndexOutOfBoundsException();
        }
        return type[k] != 0;
    }
    public boolean isFull(int row, int col) {
        // is the site (row, col) full?
        int k = xyToX(row, col);
        if (k < 0 || k >= N*N) {
            throw new IndexOutOfBoundsException();
        }
        return type[grid.find(k)] == 2;
    }
    public int numberOfOpenSites() {
        // number of open sites
        return openSize;
    }
    public boolean percolates() {
        // does the system percolate?
        boolean percolates = false;
        for (int i=0; i<N; i++) {
            percolates = isFull(N - 1, i) || percolates;
        }
        return percolates;
        // return type[grid.find(N*N)] == 2;
    }

    private int xyToX(int x, int y) {
        return x* N + y;
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(10);
        p.open(9,4);
        p.open(8,4);
        p.open(2,9);
    }

}
