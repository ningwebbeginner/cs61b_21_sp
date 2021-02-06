package deque;

import java.util.Iterator;

public interface Deque<T> extends Iterable<T> {


    /**
     * Create an empty graph.
     *
     * @param <L> type of vertex labels in the graph, must be immutable
     * @return a new empty weighted directed graph
     */
    public static <T> Deque<T> empty() {
        return new ArrayDeque<T>();
    }

    /**
     * Adds an item of type T to the front of the deque. You can assume that item is never null.
     *
     * @param item label for the new item
     */
    public void addFirst(T item);

    /**
     * Adds an item of type T to the back of the deque. You can assume that item is never null.
     *
     * @param item label for the new item
     */
    public void addLast(T item);

    /**
     *
     * @return  Returns true if deque is empty, false otherwise
     */
    public default boolean isEmpty() {
        return size() == 0;
    };

    /**
     *
     * @return Returns the number of items in the deque.
     */
    public int size();

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque();

    /**
     * Removes and returns the item at the front of the deque. If no such item exists, returns null.
     *
     * @return the item at the front of the deque. If no such item exists, returns null.
     */
    public T removeFirst();

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     *
     * @return the item at the back of the deque. If no such item exists, returns null.
     */
    public T removeLast();

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null.
     *
     * @param index 0 is the front, 1 is the next item, and so forth.
     * @return the item at the given index. If no such item exists, returns null.
     */
    public T get(int index);

    @Override
    /**
     * The Deque is an iterable (i.e. Iterable<T>) so we must provide this method to return an iterator.
     *
     * @return an iterator
     */
    public Iterator<T> iterator();

    @Override
    /**
     * Returns whether or not the parameter o is equal to the Deque.
     * o is considered equal if it is a Deque and if it contains the same contents
     * (as goverened by the generic T’s equals method) in the same order.
     *
     * @param o
     * @return  True if the parameter o is equal to the Deque,
     *          otherwise return false.
     */
    public boolean equals(Object o);

    @Override
    public int hashCode();
}
