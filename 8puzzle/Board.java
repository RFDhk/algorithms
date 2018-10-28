/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    private final int[][] blocks;
    private final int n;
    private int manhattanBuf = -1;
    private int hammingBuf = -1;


    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null)
            throw new IllegalArgumentException("null");

        this.n = blocks.length;
        this.blocks = new int[n][n];
        int n2 = n * n;
        for (int i = 0; i < n; i++) {
            if (blocks[i].length != n)
                throw new IllegalArgumentException();
            for (int j = 0; j < n; j++) {
                int k = blocks[i][j];
                if (k < 0 || k > n2) throw new IllegalArgumentException();
                this.blocks[i][j] = k;

            }
        }
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        if (hammingBuf > -1) {
            return hammingBuf;
        }
        int distance = 0;
        for (int i = 0; i < blocks.length; i++) {
            int[] row = blocks[i];
            for (int j = 0; j < row.length; j++) {
                int block = row[j];
                if (block == 0) continue;
                if (block != (i * n + j + 1)) distance++;

            }
        }

        hammingBuf = distance;
        return distance;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (manhattanBuf > -1) {
            return manhattanBuf;
        }

        int distance = 0;
        for (int i = 0; i < blocks.length; i++) {
            int[] row = blocks[i];
            for (int j = 0; j < row.length; j++) {
                int block = row[j];
                if (block == 0) continue;
                int blockI = (block - 1) / n;
                int blockJ = (block - 1) % n;
                distance += Math.abs(blockI - i) + Math.abs(blockJ - j);
            }
        }

        manhattanBuf = distance;
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {

        int[][] copy = new int[n][n];
        for (int i = 0; i < blocks.length; i++) {
            int[] row = blocks[i];
            for (int j = 0; j < row.length; j++) {
                int block = row[j];
                copy[i][j] = block;
            }
        }

        if (copy[0][0] != 0 && copy[0][1] != 0) {
            int swap = copy[0][0];
            copy[0][0] = copy[0][1];
            copy[0][1] = swap;
        } else {
            int swap = copy[n - 1][n - 1];
            copy[n - 1][n - 1] = copy[n - 1][n - 2];
            copy[n - 1][n - 2] = swap;
        }

        return new Board(copy);
    }

    // does this board equal y?


    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        Board board = (Board) that;

        if (n != board.n) return false;
        if (!Arrays.deepEquals(blocks, board.blocks)) return false;

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return CustomIterator::new;
    }

    // string representation of this board (in the output format specified below)
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        Board board = new Board(new int[][]{{1, 0}, {2, 3}});
        board.manhattan();

    }

    private class CustomIterator implements Iterator<Board> {

        private final int emptyI;
        private final int emptyJ;
        private int count = 0;
        private boolean hasNext = false;

        public CustomIterator() {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int block = blocks[i][j];
                    if (block == 0) {
                        emptyI = i;
                        emptyJ = j;
                        return;
                    }
                }
            }

            emptyI = -1;
            emptyJ = -1;
        }

        @Override
        public boolean hasNext() {
            hasNext = false;
            while (count <= 4) {
                switch (++count) {
                    case 1: //
                        if (emptyI - 1 >= 0) {
                            hasNext = true;
                            return true;
                        } else {
                            break;
                        }
                    case 2:
                        if (emptyJ + 1 < n) {
                            hasNext = true;
                            return true;
                        } else {
                            break;
                        }
                    case 3:
                        if (emptyJ - 1 >= 0) {
                            hasNext = true;
                            return true;
                        } else {
                            break;
                        }
                    case 4:
                        if (emptyI + 1 < n) {
                            hasNext = true;
                            return true;
                        } else {
                            break;
                        }
                    default:
                        return false;
                }
            }
            return hasNext;
        }

        @Override
        public Board next() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }

            hasNext = false;
            switch (count) {
                case 1:
                    return copy(emptyI, emptyJ, emptyI - 1, emptyJ);
                case 2:
                    return copy(emptyI, emptyJ, emptyI, emptyJ + 1);
                case 3:
                    return copy(emptyI, emptyJ, emptyI, emptyJ - 1);
                case 4:
                    return copy(emptyI, emptyJ, emptyI + 1, emptyJ);
                default:
                    throw new NoSuchElementException();
            }
        }

        private Board copy(int i1, int j1, int i2, int j2) {
            int[][] copy = new int[n][n];
            for (int i = 0; i < blocks.length; i++) {
                int[] row = blocks[i];
                for (int j = 0; j < row.length; j++) {
                    int block = row[j];
                    copy[i][j] = block;
                }
            }

            int swap = copy[i1][j1];
            copy[i1][j1] = copy[i2][j2];
            copy[i2][j2] = swap;

            return new Board(copy);
        }
    }

}
