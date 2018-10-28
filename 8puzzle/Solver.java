/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public class Solver {

    private class Move implements Comparable<Move> {
        private final Move previous;
        private final Board board;
        private final int moves;

        public Move(Board board) {
            this.board = board;
            previous = null;
            this.moves = 0;
        }

        public Move(Board board, Move previous) {
            this.board = board;
            this.previous = previous;
            this.moves = previous.moves + 1;
        }

        public Move getPrevious() {
            return previous;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        @Override
        public int compareTo(Move that) {
            return (this.board.manhattan() - that.getBoard().manhattan())
                    + (this.moves - that.getMoves());
        }
    }

    private Move mainMove;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        mainMove = new Move(initial);
        Move twinMove = new Move(initial.twin());
        MinPQ<Move> mainPQ = new MinPQ<>();
        MinPQ<Move> twinPQ = new MinPQ<>();
        mainPQ.insert(mainMove);
        twinPQ.insert(twinMove);

        while (!mainMove.getBoard().isGoal() && !twinMove.getBoard().isGoal()) {

            for (Board board : mainMove.getBoard().neighbors()) {
                if (mainMove.getPrevious() == null || !board.equals(mainMove.getPrevious().getBoard())) {
                    mainPQ.insert(new Move(board, mainMove));
                }
            }

            for (Board board : twinMove.getBoard().neighbors()) {
                if (twinMove.getPrevious() == null || !board.equals(twinMove.getPrevious().getBoard())) {
                    twinPQ.insert(new Move(board, twinMove));
                }
            }

            mainMove = mainPQ.delMin();
            twinMove = twinPQ.delMin();
        }


    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return mainMove.getBoard().isGoal();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) {
            return mainMove.getMoves();
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            Stack<Board> stack = new Stack<>();
            Move move = mainMove;
            while (move != null) {
                stack.push(move.getBoard());
                move = move.getPrevious();
            }
            return stack::iterator;
        } else {
            return null;
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
