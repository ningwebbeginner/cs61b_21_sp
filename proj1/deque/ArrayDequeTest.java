package deque;

public class ArrayDequeTest extends DequeInstanceTest{

    @Override
    public Deque<String> emptyStringInstance() {
        return new ArrayDeque<String>();
    }

    @Override
    public Deque<Integer> emptyIntegerInstance() {
        return new ArrayDeque<Integer>();
    }
}
