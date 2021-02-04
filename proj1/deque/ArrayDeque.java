package deque;

public class ArrayDeque<T> {
    T[] items;
    int size;
    int nextFirst;
    int nextLast;
    static final int Initial_Cap = 16;

    public ArrayDeque() {
        T[] items = (T[]) new Object[Initial_Cap];
        size = 0;
        nextFirst = 0;
        nextLast = 0;
    }


}
