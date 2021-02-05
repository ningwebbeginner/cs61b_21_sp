package deque;


import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {
    T[] items;
    int size;
    int nextFirst;
    int nextLast;
    static final int Initial_Cap = 8;
    static final int Factor = 2;
    static final double usage_factor= 0.25;
    int cap;

    public ArrayDeque() {
        items = (T[]) new Object[Initial_Cap];
        cap = Initial_Cap;
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }


    /**
     * remove index to keep the index in the array
     *
     * @param starPos the starter position
     * @param movePos the movement, positive to move forward
     *                negative to move backward.
     * @return the position after removing
     */
    private int moveIndexPos(int starPos,int movePos) {
        return (starPos + movePos + cap) % cap;
    }

    @Override
    public void addFirst(T item) {
        if (size == cap)
            resize(size * Factor);
        items[nextFirst] = item;
        nextFirst = moveIndexPos(nextFirst, -1);
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size == cap) {
            resize(size * Factor);
        }
        items[nextLast] = item;
        nextLast = moveIndexPos(nextLast, 1);
        size++;
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        System.out.println("The Deque:");
        for (T item:this) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size > 0) {
            if (size < cap * usage_factor) {
                resize((int) (cap * usage_factor * 2));
            }
            //nextFirst = (nextFirst + 1 + cap) % cap;
            nextFirst = moveIndexPos(nextFirst, 1);
            size--;
            T result = items[nextFirst];
            items[nextFirst] = null;
            return result;
        }
        return null;
    }

    @Override
    public T removeLast() {
        if (size > 0) {
            if (size < cap * usage_factor) {
                resize((int) (cap * usage_factor * 2));
            }
            nextLast = moveIndexPos(nextLast, -1);
            size--;
            T result = items[nextLast];
            items[nextLast] = null;
            return result;
        }
        return null;
    }


    @Override
    public T get(int index) {
        if (size > 0 && index >= 0 && index < size) {
            return items[moveIndexPos(nextFirst, index + 1)];
        }
        return null;
    }



    private void resize(int capacity) {
        if (capacity >= Initial_Cap) {
            T[] a = (T[])new Object[capacity];
            int i = 0;
            for (T item:this) {
                a[i] = item;
                i++;
            }
            cap = capacity;
            nextFirst = capacity - 1;
            nextLast = size;
            items = a;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int itPos;

        public ArrayDequeIterator() {
            itPos = 0;
        }

        @Override
        public boolean hasNext() {
            return itPos < size;
        }

        @Override
        public T next() {
            T returnItem = items[moveIndexPos(nextFirst, itPos + 1)];
            itPos ++;
            return returnItem;
        }
    }


}
