package deque;

public class ArrayDequeTest extends DequeInstanceTest{

    @Override
    public Deque<String> emptyStringInstance() {
        return new ArrayDeque<>();
    }

    @Override
    public Deque<Integer> emptyIntegerInstance() {
        return new ArrayDeque<>();
    }
}
