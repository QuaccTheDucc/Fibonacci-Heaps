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
    void findMin() {
        FibonacciHeap heap = new FibonacciHeap();
        for (int i = 100; i >= 0; i--){
            heap.insert(i);
            assertEquals(i, heap.findMin().getKey());
        }
        for (int i = 1; i < 50; i++){
            assertEquals(i-1, heap.findMin().getKey());
            heap.deleteMin();
            assertEquals(i, heap.findMin().getKey());
        }
        for (int i = 0; i <= 49; i++){
            heap.insert(i);
        }
        assertEquals(0, heap.findMin().getKey());
        for (int i = 1; i < 50; i++){
            assertEquals(i-1, heap.findMin().getKey());
            heap.deleteMin();
            assertEquals(i, heap.findMin().getKey());
        }

    }

    @Test
    void meld() {
        FibonacciHeap heap1 = new FibonacciHeap();
        for (int i = 100; i >= 0; i--){
            heap1.insert(i);
        }
        for (int i = 0; i < 25; i++){
            heap1.deleteMin();
        }
        FibonacciHeap heap2 = new FibonacciHeap();
        for (int i = 24; i >= 0; i--){
            heap2.insert(i);
        }
        heap2.deleteMin();
        heap1.meld(heap2); // heap1 should have all numbers between 1 and 100 now.
        for (int i = 2; i <= 100; i++){
            assertEquals(i-1, heap1.findMin().getKey());
            heap1.deleteMin();
            assertEquals(i, heap1.findMin().getKey());
        }
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

}

