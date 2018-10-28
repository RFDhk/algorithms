
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Object[] items;
    private int size;
    private int last;
    private int removedItemsSize;
    private boolean flag = false;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = new Object[2];

        size = 0;
        last = 0;
        removedItemsSize = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (last == items.length) {
            last = size;
            resize(2 * Math.max(1, size));
        }
        items[last++] = item;
        size++;
    }

    private void resize(int capacity) {
        Object[] copy = new Object[capacity];

        int i = 0;
        for (Object item : items) {
            if (item != null) {
                copy[i++] = item;
            }
        }

        items = copy;
        removedItemsSize = 0;
    }

    private int getRandomPosition() {
        int pos = StdRandom.uniform(items.length);

        if (flag) {
            for (int i = pos; i < items.length; i++) {
                if (items[i] != null) {
                    return i;
                }
            }

            for (int i = 0; i < pos; i++) {
                if (items[i] != null) {
                    return i;
                }
            }
        } else {
            for (int i = pos; i > -1; i--) {
                if (items[i] != null) {
                    return i;
                }
            }

            for (int i = items.length - 1; i > pos; i--) {
                if (items[i] != null) {
                    return i;
                }
            }
        }

        flag = !flag;
        throw new NoSuchElementException("array is empty" + Arrays.toString(items));
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int pos = getRandomPosition();
        Item removedItem = (Item) items[pos];
        items[pos] = null;
        removedItemsSize++;
        size--;

        if (size > 0 && size == items.length / 4) {
            last = size;
            resize(items.length / 2);
        }

        return removedItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (Item) items[getRandomPosition()];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        int[] positions = new int[size];
        int j = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                positions[j++] = i;
            }

        }
        for (int i = 0; i < positions.length; i++) {
            int swapPosition = StdRandom.uniform(positions.length);
            if (swapPosition != i) {
                int swap = positions[i];
                positions[i] = positions[swapPosition];
                positions[swapPosition] = swap;
            }

        }
        return new Iterator<Item>() {
            private int cur = 0;

            @Override
            public boolean hasNext() {
                return cur < positions.length;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (Item) items[positions[cur++]];
            }


            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<String> d = new RandomizedQueue<>();
        d.enqueue("1");
        d.enqueue("2");
        d.enqueue("3");

        d.enqueue("4");
        d.enqueue("5");
        d.enqueue("6");
        d.enqueue("7");

        d.enqueue("8");
        d.enqueue("9");

        d.enqueue("10");
        d.enqueue("11");
        d.enqueue("12");
        d.dequeue();
        d.dequeue();
        d.dequeue();
        d.dequeue();
        d.dequeue();
        d.dequeue();

        Iterator<String> i = d.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }
    }

}