package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {
    T[] items = (T[]) new Object[Initial_Cap];
    int size;
    int nextFirst;
    int nextLast;
    static final int Initial_Cap = 8;
    static final int Factor = 2;
    static final double usage_factor= 0.25;
    int cap;

    public ArrayDeque() {
        T[] items = (T[]) new Object[Initial_Cap];
        cap = Initial_Cap;
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    @Override
    public void addFirst(T item) {
        if (size == cap)
            resize(size * Factor);
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + cap) % cap;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size == cap)
            resize(size * Factor);
        items[nextLast] = item;
        nextLast = (nextLast + 1 + cap) % cap;
        size++;
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        //TODO
    }

    @Override
    public T removeFirst() {
        if (size > 0) {
            if (size < cap * usage_factor) {
                resize((int) (cap * usage_factor * 2));
            }
            nextFirst = (nextFirst + 1 + cap) % cap;
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
            nextLast = (nextLast - 1 + cap) % cap;
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
            return items[(nextFirst + index + 1) % cap];
        }
        return null;
    }



    private void resize(int capacity) {
        if (capacity >= Factor * Initial_Cap) {
            T[] a = (T[])new Object[capacity];
            int firstIndex = (nextFirst + 1 + cap) % cap;
            int lastIndex = (nextLast - 1 + cap) % cap;
            for (int i = 0; i < size; i++) {
                a[i] = items[firstIndex];
                firstIndex = (firstIndex + 1 + cap) % cap;
            }
            cap = capacity;
            nextFirst = capacity - 1;
            nextLast = size;
            items = a;
        }



    }

    @Override
    public Iterator<T> iterator() {
        //TODO
        return null;
    }


}
