/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
//        In in = new In("distinct.txt");      // input file
//        while (!in.isEmpty()) {
//            queue.enqueue(in.readString());
//        }

        for (int i = 0; i < k; i++) {
            System.out.println(queue.dequeue());

        }
    }
}
