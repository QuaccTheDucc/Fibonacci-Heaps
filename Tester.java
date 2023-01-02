import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class Tester {

    @Test
    void isEmpty() {
        FibonacciHeap heap = new FibonacciHeap();
        assertTrue(heap.isEmpty());
        heap.insert(5);
        assertFalse(heap.isEmpty());
        heap.deleteMin();
        assertTrue(heap.isEmpty());
    }


    @Test
    void deleteMinAndFindMin() {
        FibonacciHeap heap = new FibonacciHeap();
        assertNull(heap.findMin());
        for (int i = 100; i >= 0; i--){
            heap.insert(i);
            assertEquals(i, heap.findMin().getKey());
        }
        heap.deleteMin();
        // from the binary representation of 100:
        assertEquals(97, heap.getSentinel().getNext().getKey());
        assertEquals(65, heap.getSentinel().getNext().getNext().getKey());
        assertEquals(1, heap.getSentinel().getNext().getNext().getNext().getKey());

        heap.insert(0);
        for (int i = 1; i <= 50; i++){
            assertEquals(i-1, heap.findMin().getKey());
            heap.deleteMin();
            assertEquals(i, heap.findMin().getKey());
        }
        for (int i = 0; i <= 49; i++){
            heap.insert(i);
        }
        assertEquals(0, heap.findMin().getKey());
        for (int i = 1; i < 100; i++){
            assertEquals(i-1, heap.findMin().getKey());
            heap.deleteMin();
            assertEquals(i, heap.findMin().getKey());
        }
        for (int i = 1; i <= 101; i++){
            heap.deleteMin();
        }
        assertNull(heap.findMin());

    }


    @Test
    void ranks(){
        FibonacciHeap heap = new FibonacciHeap();
        assertNull(heap.findMin());
        for (int i = 100; i >= 0; i--){
            heap.insert(i);
        }
        heap.deleteMin();
        // from the binary representation of 100:
        assertEquals(2, heap.getSentinel().getNext().getRank());
        assertEquals(5, heap.getSentinel().getNext().getNext().getRank());
        assertEquals(6, heap.getSentinel().getNext().getNext().getNext().getRank());

        heap.insert(0);
        for (int i = 1; i <= 65; i++){
            heap.deleteMin();
        }

        // from the binary representation of 36
        assertEquals(2, heap.getSentinel().getNext().getRank());
        assertEquals(5, heap.getSentinel().getNext().getNext().getRank());

    }


    @Test
    void meld() {
        FibonacciHeap heap1 = new FibonacciHeap();
        heap1.meld(new FibonacciHeap());
        assertTrue(heap1.isEmpty()); // case1: both are empty

        FibonacciHeap heap2 = new FibonacciHeap();
        for (int i = 100; i >= 1; i--){
            heap2.insert(i);
        }
        heap1.meld(heap2); // case2: heap1 empty and heap2 isn't.
        assertEquals(1, heap1.findMin().getKey());
        assertEquals(100, heap1.size());
        assertEquals(100, heap1.getNumOfRoots());

        heap1 = new FibonacciHeap();
        heap2 = new FibonacciHeap();
        for (int i = 100; i >= 51; i--){
            heap1.insert(i);
        }
        for (int i = 50; i >= 0; i--){
            heap2.insert(i);
        }
        heap2.deleteMin();
        heap1.meld(heap2); // case3: num of roots of both isn't 1.

        assertEquals(1, heap1.findMin().getKey());
        assertEquals(100, heap1.size());
        assertEquals(50 + 3, heap1.getNumOfRoots()); // 50 for heap1 and 3 for heap2 (number of ones in binary of 50).


        heap1 = new FibonacciHeap();
        heap2 = new FibonacciHeap();
        heap1.insert(51);
        for (int i = 50; i >= 0; i--){
            heap2.insert(i);
        }
        heap2.deleteMin();
        heap1.meld(heap2); // case4: num of roots of heap1 is 1 and heap2's isn't 1.

        assertEquals(1, heap1.findMin().getKey());
        assertEquals(51, heap1.size());
        assertEquals(1 + 3, heap1.getNumOfRoots()); // 1 for heap1 and 3 for heap2 (number of ones in binary of 50).


        heap1 = new FibonacciHeap();
        heap2 = new FibonacciHeap();
        heap2.insert(51);
        for (int i = 50; i >= 0; i--){
            heap1.insert(i);
        }
        heap1.deleteMin();
        heap1.meld(heap2); // case5: num of roots of heap2 is 1 and heap1's isn't 1.

        assertEquals(1, heap1.findMin().getKey());
        assertEquals(51, heap1.size());
        assertEquals(1 + 3, heap1.getNumOfRoots()); // 3 for heap1 and 1 for heap2 (number of ones in binary of 50).


        heap1 = new FibonacciHeap();
        heap2 = new FibonacciHeap();
        heap2.insert(0);
        heap1.insert(1);
        heap1.meld(heap2); // case6: num of roots of heap2 and heap1 is 1.

        assertEquals(0, heap1.findMin().getKey());
        assertEquals(2, heap1.size());
        assertEquals(2, heap1.getNumOfRoots()); // 1 for heap1 and  heap2.


    }

    @Test
    void size() {
        FibonacciHeap heap = new FibonacciHeap();
        assertEquals(0, heap.size());
        for (int i = 100; i >= 0; i--){
            heap.insert(i);
            assertEquals(101 - i, heap.size());
        }
        for (int i = 1; i <= 101; i++){
            heap.deleteMin();
            assertEquals(101 - i, heap.size());
        }
        assertEquals(0, heap.size());
    }

    @Test
    void countersRep(){
        FibonacciHeap heap = new FibonacciHeap();
        assertEquals(0, heap.countersRep().length);

        for (int i = 100; i >= 0; i--){
            heap.insert(i);
        }
        int[] ranks = heap.countersRep();
        assertEquals(1, ranks.length);
        assertEquals(101, ranks[0]);

        heap.deleteMin();

        ranks = heap.countersRep();
        assertEquals(7, ranks.length);
        // binary representation of 100 is 1100100.
        assertEquals(0, ranks[0]);
        assertEquals(0, ranks[1]);
        assertEquals(1, ranks[2]);
        assertEquals(0, ranks[3]);
        assertEquals(0, ranks[4]);
        assertEquals(1, ranks[5]);
        assertEquals(1, ranks[6]);

        for (int i = 100; i >= 1; i--){
            heap.insert(2*i);
        }

        ranks = heap.countersRep();
        assertEquals(7, ranks.length);
        assertEquals(100, ranks[0]);
        assertEquals(0, ranks[1]);
        assertEquals(1, ranks[2]);
        assertEquals(0, ranks[3]);
        assertEquals(0, ranks[4]);
        assertEquals(1, ranks[5]);
        assertEquals(1, ranks[6]);
    }
}

