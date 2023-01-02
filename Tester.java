import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class Tester {

    @Test
    void isEmpty() {
        FibonacciHeap heap = new FibonacciHeap();
        assertTrue(heap.isEmpty());
        heap.insert(5);
        assertFalse(heap.isEmpty());
        assertEquals(heap.size(), heap.nonMarked());
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
        assertEquals(heap.size(), heap.nonMarked());

        heap.insert(0);
        for (int i = 1; i <= 50; i++){
            assertEquals(i-1, heap.findMin().getKey());
            heap.deleteMin();
            assertEquals(i, heap.findMin().getKey());
            assertEquals(heap.size(), heap.nonMarked());
        }
        for (int i = 0; i <= 49; i++){
            heap.insert(i);
        }
        assertEquals(0, heap.findMin().getKey());
        for (int i = 1; i < 100; i++){
            assertEquals(i-1, heap.findMin().getKey());
            heap.deleteMin();
            assertEquals(i, heap.findMin().getKey());
            assertEquals(heap.size(), heap.nonMarked());
        }
        for (int i = 1; i <= 101; i++){
            heap.deleteMin();
        }
        assertNull(heap.findMin());
        assertEquals(heap.size(), heap.nonMarked());

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
        assertEquals(heap1.size(), heap1.nonMarked());

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
        assertEquals(heap1.size(), heap1.nonMarked());

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
        assertEquals(heap1.size(), heap1.nonMarked());

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
        assertEquals(heap1.size(), heap1.nonMarked());

        heap1 = new FibonacciHeap();
        heap2 = new FibonacciHeap();
        heap2.insert(0);
        heap1.insert(1);
        heap1.meld(heap2); // case6: num of roots of heap2 and heap1 is 1.

        assertEquals(0, heap1.findMin().getKey());
        assertEquals(2, heap1.size());
        assertEquals(2, heap1.getNumOfRoots()); // 1 for heap1 and  heap2.
        assertEquals(heap1.size(), heap1.nonMarked());

    }

    @Test
    void size() {
        FibonacciHeap heap = new FibonacciHeap();
        assertEquals(0, heap.size());
        for (int i = 100; i >= 0; i--){
            heap.insert(i);
            assertEquals(101 - i, heap.size());
            assertEquals(heap.size(), heap.nonMarked());
        }
        for (int i = 1; i <= 101; i++){
            heap.deleteMin();
            assertEquals(101 - i, heap.size());
            assertEquals(heap.size(), heap.nonMarked());
        }
        assertEquals(0, heap.size());
        assertEquals(heap.size(), heap.nonMarked());
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

    @Test
    void decreaseKey(){
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode inserted = heap.insert(1);
        assertNull(inserted.getParent());
        heap.decreaseKey(inserted, 1); // case1: the node has no parent, meaning it is a root and
        // we only need to decrease its key and search check if it's the new min.
        assertEquals(0, heap.findMin().getKey());

        heap = new FibonacciHeap();
        FibonacciHeap.HeapNode insertedParent;
        for (int i = 0; i <= 10; i++){
            if (i == 10){
                inserted = heap.insert(i);
            } else {
                heap.insert(i);
            }
        }
        heap.deleteMin();
        insertedParent = inserted.getParent();
        //System.out.println(inserted.getKey());
        //System.out.println(insertedParent.getKey());
        //System.out.println(insertedParent.getParent().getKey());
        //System.out.println(insertedParent.getParent().getParent().getKey());

        heap.decreaseKey(inserted, 10); // case2: the node has an unmarked parent and no siblings,
        // we decrease it key to be 0, so it's supposed to be the new min.

        // all the values are from the simulator of https://dichchankinh.com/~galles/visualization/FibonacciHeap.html .
        assertEquals(0, inserted.getKey());
        assertEquals(0, heap.findMin().getKey());
        assertNull(inserted.getParent());
        assertEquals(0, insertedParent.getRank()); // was 1, went down by 1 so now it's zero.
        assertNull(insertedParent.getChild());
        assertTrue(insertedParent.isMarked());
        assertFalse(inserted.isMarked());

        inserted = insertedParent;
        insertedParent = insertedParent.getParent();

        heap.decreaseKey(inserted, 10); // case3: the node has an unmarked parent and siblings,
        // we decrease it key to be -1, so it's supposed to be the new min.


        assertEquals(-1, inserted.getKey());
        assertEquals(-1, heap.findMin().getKey());
        assertNull(inserted.getParent());
        assertEquals(1, insertedParent.getRank()); // was 2, went down by 1, so now it's 1.
        assertEquals(8, insertedParent.getChild().getPrev().getKey());
        assertTrue(insertedParent.isMarked());
        assertFalse(inserted.isMarked());


        inserted = insertedParent.getChild().getPrev();
        FibonacciHeap.HeapNode insertedGrandParent = insertedParent.getParent();

        assertEquals(3, insertedGrandParent.getKey());
        assertTrue(insertedParent.isMarked());

        heap.decreaseKey(inserted, 10); // case4: the node has a marked parent and no siblings,
        // we decrease it key to be -2, so it's supposed to be the new min.


        assertEquals(-2, inserted.getKey());
        assertEquals(-2, heap.findMin().getKey());
        assertNull(inserted.getParent());
        assertEquals(0, insertedParent.getRank()); // was 1, went down by 1, so now it's zero.
        assertNull(insertedParent.getChild());
        assertFalse(insertedParent.isMarked()); // cascading cuts made it a root.
        assertFalse(inserted.isMarked());
        assertFalse(insertedGrandParent.isMarked()); // 3 is a root.

        int[] keysByOrderLeftToRight = new int[] {7, -2, -1, 0, 1, 3};
        FibonacciHeap.HeapNode temp = heap.getSentinel().getNext();
        for (int i = 0; i < keysByOrderLeftToRight.length; i++){
            assertEquals(keysByOrderLeftToRight[i], temp.getKey());
            temp = temp.getNext();
        }
    }

    @Test
    void delete(){
        // all the values are from the simulator of https://dichchankinh.com/~galles/visualization/FibonacciHeap.html .

        FibonacciHeap heap = new FibonacciHeap();
        assertEquals(0, heap.nonMarked());
        FibonacciHeap.HeapNode inserted = heap.insert(1);
        assertEquals(1, heap.nonMarked());
        assertNull(inserted.getParent());
        heap.delete(inserted); // case1, inserted = min.
        assertTrue(heap.isEmpty());

        heap = new FibonacciHeap();
        FibonacciHeap.HeapNode inserted2 = null, inserted3= null, inserted4 = null, inserted5 = null;
        for (int i = 0; i <= 10; i++){
            if (i == 9){
                inserted = heap.insert(i);
            } else if (i == 8){
                inserted2 = heap.insert(i);
            } else if (i == 4){
                inserted3 = heap.insert(i);
            } else if (i == 5){
                inserted4 = heap.insert(i);
            } else if (i == 1){
                inserted5 = heap.insert(i);
            } else {
                heap.insert(i);
            }
        }
        heap.deleteMin();


        assertEquals(9, inserted.getKey());
        heap.delete(inserted); // case2 inserted isn't the min and its parent is unmarked, and it's a child of a node.

        assertEquals(1, heap.findMin().getKey()); // 1 still is the min.

        int[] keysByOrderLeftToRight = new int[] {10, 1 , 3};
        int[] keysByOrderLeftToRight2 = new int[] {7, 5 , 4};
        FibonacciHeap.HeapNode temp = heap.getSentinel().getNext();
        FibonacciHeap.HeapNode temp2 = heap.getSentinel().getPrev().getChild().getNext();
        for (int i = 0; i < keysByOrderLeftToRight.length; i++){
            assertEquals(keysByOrderLeftToRight[i], temp.getKey());
            temp = temp.getNext();
        }
        for (int i = 0; i < keysByOrderLeftToRight2.length; i++){
            assertEquals(keysByOrderLeftToRight2[i], temp2.getKey());
            temp2 = temp2.getNext();
        }

        assertTrue(inserted2 == heap.getSentinel().getPrev().getChild().getNext().getChild().getNext());
        assertTrue(heap.getSentinel().getPrev().getChild().getNext().isMarked()); // 7 should be marked.
        assertEquals(9, heap.size());
        assertEquals(8, heap.nonMarked());


        assertEquals(8, inserted2.getKey());
        heap.delete(inserted2); // case3 parent is marked.

        assertEquals(1, heap.findMin().getKey()); // 1 still is the min.
        assertTrue(heap.getSentinel().getNext().getNext().isSentinel()); // 1 is the only root according to on-paper debugging work.
        keysByOrderLeftToRight = new int[] {3,7,2};
        temp = heap.getSentinel().getNext().getChild().getNext();
        for (int i = 0; i < keysByOrderLeftToRight.length; i++){
            assertEquals(keysByOrderLeftToRight[i], temp.getKey());
            temp = temp.getNext();
        }
        assertEquals(8, heap.size);
        assertEquals(8, heap.nonMarked()); // every node is not marked.


        assertEquals(3, inserted3.getParent().getKey());
        assertEquals(4, inserted3.getKey());
        heap.delete(inserted3);
        assertEquals(1, heap.findMin().getKey()); // 1 still is the min.
        assertTrue(heap.getSentinel().getNext().getNext().isSentinel()); // 1 is the only root according to on-paper debugging work.
        keysByOrderLeftToRight = new int[] {3,7,2};
        temp = heap.getSentinel().getNext().getChild().getNext();
        for (int i = 0; i < keysByOrderLeftToRight.length; i++){
            assertEquals(keysByOrderLeftToRight[i], temp.getKey());
            temp = temp.getNext();
        }
        assertEquals(7, heap.size());
        assertEquals(6, heap.nonMarked());

        heap.delete(inserted4);
        assertEquals(1, heap.findMin().getKey()); // 1 still is the min.
        keysByOrderLeftToRight = new int[] {3, 1};
        temp = heap.getSentinel().getNext();
        for (int i = 0; i < keysByOrderLeftToRight.length; i++){
            assertEquals(keysByOrderLeftToRight[i], temp.getKey());
            temp = temp.getNext();
        }
        assertEquals(6, heap.nonMarked());

        heap.delete(inserted5);
        assertEquals(2, heap.findMin().getKey()); // 2 still is the min.
        keysByOrderLeftToRight = new int[] {2, 3};
        temp = heap.getSentinel().getNext();
        for (int i = 0; i < keysByOrderLeftToRight.length; i++){
            assertEquals(keysByOrderLeftToRight[i], temp.getKey());
            temp = temp.getNext();
        }
        assertEquals(5, heap.size());
        assertEquals(5, heap.nonMarked());

    }

    @Test
    void kMin(){
        FibonacciHeap heap = new FibonacciHeap();
        assertEquals(0, FibonacciHeap.kMin(heap, 0).length);

        int m = 1;
        int[] arr = new int[m];
        for (int i = 0; i <= 16; i++){
            int n = 5*i;
            heap.insert(n);
            if (i != 0 && i <= m) arr[i-1] = n;
        }
        heap.deleteMin(); // now heap is a binomial heap.

        assertArrayEquals(arr, FibonacciHeap.kMin(heap, m));
    }
}

