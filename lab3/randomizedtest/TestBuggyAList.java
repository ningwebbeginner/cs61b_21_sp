package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {


    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> noResizingTest = new AListNoResizing<>();
        BuggyAList<Integer> buggyListTest = new BuggyAList<>();

        noResizingTest.addLast(4);
        buggyListTest.addLast(4);
        noResizingTest.addLast(5);
        buggyListTest.addLast(5);
        noResizingTest.addLast(6);
        buggyListTest.addLast(6);


        assertEquals(noResizingTest.size(), buggyListTest.size());

        assertEquals(noResizingTest.removeLast(), buggyListTest.removeLast());
        assertEquals(noResizingTest.removeLast(), buggyListTest.removeLast());
        assertEquals(noResizingTest.removeLast(), buggyListTest.removeLast());


    }
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> noResizingTest = new AListNoResizing<>();
        BuggyAList<Integer> buggyListTest = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                noResizingTest.addLast(randVal);
                buggyListTest.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = noResizingTest.size();
                System.out.println("size: " + size);
                assertEquals(buggyListTest.size(),noResizingTest.size());
            }
            else if (operationNumber == 2) {
                if (noResizingTest.size() > 0) {
                    System.out.println("getLast(" + noResizingTest.getLast() + ")");
                    assertEquals(buggyListTest.getLast(),noResizingTest.getLast());
                }
            }
            else if (operationNumber == 3) {
                if (noResizingTest.size() > 0) {
                    System.out.println("getLast(" + noResizingTest.removeLast() + ")");
                    assertEquals(buggyListTest.removeLast(),noResizingTest.removeLast());
                }
            }
        }
    }

}