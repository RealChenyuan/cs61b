package hw4.puzzle;

import java.util.HashSet;
import java.util.Set;

public class Board implements WorldState {

    private int N;
    private int[][] initial;

    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j
     */
    public Board(int[][] tiles) {
        N = tiles.length;
        initial = arrayCopy(tiles);
    }


    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     */
    public int tileAt(int i, int j) {
        if (i<0 || i>=N || j<0 || j>=N) throw new IndexOutOfBoundsException();
        return initial[i][j];
    }

    /**
     *
     * @return Returns the board size N
     */
    public int size() {
        return N;
    }

    /**
     *
     * @return Returns the neighbors of the current board
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Set<WorldState> neighbs = new HashSet<>();
        int x = blankPos()[0];
        int y = blankPos()[1];
        int[][] tiles = arrayCopy(initial);
        if (inBounds(x-1)) {
            tiles[x][y] = initial[x-1][y];
            tiles[x-1][y] = 0;
            neighbs.add(new Board(tiles));
            tiles[x-1][y] = initial[x-1][y];
            tiles[x][y] = 0;
        }
        if (inBounds(x+1)) {
            tiles[x][y] = initial[x+1][y];
            tiles[x+1][y] = 0;
            neighbs.add(new Board(tiles));
            tiles[x+1][y] = initial[x+1][y];
            tiles[x][y] = 0;
        }
        if (inBounds(y-1)) {
            tiles[x][y] = initial[x][y-1];
            tiles[x][y-1] = 0;
            neighbs.add(new Board(tiles));
            tiles[x][y-1] = initial[x][y-1];
            tiles[x][y] = 0;
        }
        if (inBounds(y+1)) {
            tiles[x][y] = initial[x][y+1];
            tiles[x][y+1] = 0;
            neighbs.add(new Board(tiles));
            tiles[x][y+1] = initial[x][y+1];
            tiles[x][y] = 0;
        }
        return neighbs;
    }

    private int[][] arrayCopy(int[][] src) {
        int[][] tiles = new int[N][N];
        for (int i=0; i<N; i++) {
            System.arraycopy(src[i], 0, tiles[i], 0, N);
        }
        return tiles;
    }

    private boolean inBounds(int k) {
        return k>=0 && k<N;
    }

    private int[] blankPos() {
        int[] pos = new int[2];
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++){
                if (initial[i][j] == 0) {
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }
        return pos;
    }

    public int hamming() {
        int dis = 0;
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++){
                if (initial[i][j]==0) continue;
                if (initial[i][j] != xyTo1D(i, j)) dis++;
            }
        }
        return dis;
    }

    private int xyTo1D(int i, int j) {
        return i*N + j + 1;
    }

    public int manhattan() {
        int dis = 0;
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++){
                if (initial[i][j]==0) continue;
                int x = toX(initial[i][j]);
                int y = toY(initial[i][j]);
                dis += manhattanDistance(i, j, x, y);
            }
        }
        return dis;
    }

    private int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1-x2) + Math.abs(y1-y2);
    }

    private int toX (int k) {
        return (k-1)/N;
    }

    private int toY (int k) {
        return (k-1)%N;
    }

    /**
     * Estimated distance to goal. This method should
     * simply return the results of manhattan() when submitted to
     * Gradescope.
     */
    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * @return Returns true if this board's tile values are the same
     *         position as y's
     */
    public boolean equals(Object y) {
        if (this == y) return true;

        if (y==null || getClass() != y.getClass()) return false;

        Board b = (Board) y;
        int[][] bInit = b.initial;

        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++){
                if (initial[i][j] != bInit[i][j]) return false;
            }
        }
        return true;
    }


    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    @Override
    public int hashCode() {
        int result = initial != null ? initial.hashCode() : 0;
        return result;
    }

}
