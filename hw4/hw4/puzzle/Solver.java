package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Solver {

    private MinPQ<SearchNode> minPQ;
    private int moves;

    /**
     * Constructor which solves the puzzle, computing
     *      everything necessary for moves() and solution() to
     *      not have to solve the problem again. Solves the
     *      puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        SearchNode init = new SearchNode(initial, 0, null);
        minPQ = new MinPQ<>();
        minPQ.insert(init);
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     */
    public int moves() {
        solution();
        return moves;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     */
    public Iterable<WorldState> solution() {
        Stack<WorldState> solutions = new Stack<>();
        SearchNode p = null;
        while (!minPQ.isEmpty()) {
            SearchNode c = minPQ.delMin();
            if (c.world.isGoal()) {
                p = c;
                break;
            };
            for (WorldState n: c.world.neighbors()) {
                if (c.prevNode != null && n.equals(c.prevNode.world)) continue;
                SearchNode node = new SearchNode(n, c.moves+1, c);
                minPQ.insert(node);
            }
        }
        moves = 0;
        while (p.prevNode != null) {
            solutions.push(p.world);
            p = p.prevNode;
            moves++;
        }
        return solutions;
    }

    public class SearchNode implements Comparable<SearchNode>{
        public WorldState world;
        public int moves;
        public SearchNode prevNode;
        private int distanceToGoal;

        private SearchNode(WorldState world, int moves, SearchNode prevNode) {
            this.world = world;
            this.moves = moves;
            this.prevNode = prevNode;
            distanceToGoal = world.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(SearchNode o) {
            int p1 = moves + distanceToGoal;
            int p2 = o.moves + o.distanceToGoal;
            return Integer.compare(p1, p2);
        }
    }
}
