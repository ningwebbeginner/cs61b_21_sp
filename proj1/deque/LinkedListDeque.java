package deque;

import java.util.Iterator;

public class LinkedListDeque<T> {
    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = null;
        size = 0;
    }

    public void addFirst(T item) {
        addLast(item);
        sentinel = sentinel.previous;
    }

    public void addLast(T x) {

        Node p = sentinel;
        if (size != 0) {
            Node sentinelPrevious = p.previous;
            p.previous = new Node(x, sentinelPrevious, p);
            sentinelPrevious.next = p.previous;
        }
        else {
            sentinel = new Node(x, null, null);
            sentinel.previous = sentinel;
            sentinel.next = sentinel.previous;
        }
        size += 1;
    }

    public T removeFirst() {
        if (size > 0) {
            Node p = sentinel;
            sentinel = sentinel.next;
            p.previous.next = sentinel;
            sentinel.previous = p.previous;
            size -= 1;
            return p.item;
        }
        else
            return null;
    }

    public T removeLast() {
        if (size > 0) {
            sentinel = sentinel.previous;
            return removeFirst();
        }
        else
            return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        System.out.println("The Deque:");
        if (size > 0) {
            Node itrat = sentinel;
            System.out.print(itrat.item + " ");
            while (itrat.next != sentinel) {
                itrat = itrat.next;
                System.out.print(itrat.item + " ");
            }
        }
        System.out.println();
    }

    public T get(int index) {
        if (size > 0 && index < size && index >= 0) {
            Node itrat = sentinel;
           for (int i = 0; i < index; i++){
               itrat = itrat.next;
           }
           return itrat.item;
        }
        else
            return null;
    }

    public T getRecursive(int index) {
        if (size > 0 && index < size && index >= 0) {
            return getRecursiveHelper(sentinel, index);
        }
        else
            return null;
    }

    private T getRecursiveHelper(Node p,int index) {
        if (index == 0)
            return p.item;
        else
            return getRecursiveHelper(p.next,index - 1);
    }

    private class Node {
        public T item;
        public Node previous;
        public Node next;

        public Node(T i, Node p, Node n) {
            item = i;
            previous = p;
            next = n;
        }

    }

    /*public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node current = sentinel;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (hasNext()) {
                    T data = current.item;
                    current = current.next;
                    return data;
                }
                return null;
            }
        };
    }*/

    @Override
    public boolean equals(Object o) {
        if(o instanceof LinkedListDeque) {
            LinkedListDeque lis = (LinkedListDeque) o;
            if(lis.size() == size) {
                for (int i = 0; i < size; i++) {
                    if (!lis.get(i).equals(this.get(i)))
                        return false;
                }
                return true;
            }
        }
        return false;

    }
}
