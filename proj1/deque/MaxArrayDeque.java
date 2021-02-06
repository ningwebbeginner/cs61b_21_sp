package deque;


import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        if (this.size() == 0) {
            return null;
        }
        T result = this.get(0);
        for (T item:this) {
            if (comparator.compare(result,item) < 0) {
                result = item;
            }
        }
        return result;
    }

    public T max(Comparator<T> c) {
        if (this.size() == 0) {
            return null;
        }
        T result = this.get(0);
        for (T item:this) {
            if (c.compare(result,item) < 0) {
                result = item;
            }
        }
        return result;
    }


}
