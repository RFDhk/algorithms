import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Deque<Item> implements Iterable<Item> {

    private final LinkItem<Item> first;
    private final LinkItem<Item> last;

    // construct an empty deque
    public Deque() {
        first = new LinkItem<>();
        last = new LinkItem<>();
        first.setNext(last);
        last.setPrev(first);
    }

    // is the deque empty?
    public boolean isEmpty() {
        return Objects.equals(first.getNext(), last);
    }

    // return the number of items on the deque
    public int size() {
        if (isEmpty()) {
            return 0;
        }

        int i = 0;
        LinkItem<Item> cur = first.getNext();
        while (cur.getNext() != null) {
            cur = cur.getNext();
            i++;
        }
        return i;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        LinkItem<Item> cur = new LinkItem<>();
        cur.setCur(item);
        cur.setNext(first.getNext());
        cur.setPrev(first);

        if (isEmpty()) {
            last.setPrev(cur);
        }

        first.getNext().setPrev(cur);
        first.setNext(cur);
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        LinkItem<Item> cur = new LinkItem<>();
        cur.setCur(item);
        cur.setPrev(last.getPrev());
        cur.setNext(last);

        if (isEmpty()) {
            first.setNext(cur);
        }

        last.getPrev().setNext(cur);
        last.setPrev(cur);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item removed = first.getNext().getCur();
        first.getNext().getNext().setPrev(first);
        first.setNext(first.getNext().getNext());
        return removed;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item removed = last.getPrev().getCur();
        last.getPrev().getPrev().setNext(last);
        last.setPrev(last.getPrev().getPrev());
        return removed;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private LinkItem<Item> cur = first;
            @Override
            public boolean hasNext() {
                return !cur.getNext().equals(last);
            }
            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                cur = cur.getNext();
                return cur.getCur();
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> d = new Deque<>();
        d.addFirst("1");
        d.addLast("2");
        d.addLast("2");
        d.addFirst("1");

        Iterator<String> i = d.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }

        d.removeFirst();
        d.removeFirst();
        d.removeLast();

        System.out.println("Size: " + d.size());

        i = d.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }



    }

    private class LinkItem<T> {
        private LinkItem<T> prev;
        private T cur;
        private LinkItem<T> next;

        public T getCur() {
            return cur;
        }

        public void setCur(T cur) {
            this.cur = cur;
        }

        public LinkItem<T> getPrev() {
            return prev;
        }

        public void setPrev(LinkItem<T> prev) {
            this.prev = prev;
        }

        public LinkItem<T> getNext() {
            return next;
        }

        public void setNext(LinkItem<T> next) {
            this.next = next;
        }
    }
}
