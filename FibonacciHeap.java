/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{

    HeapNode min;
    HeapNode sentinel;
    int size;
    int numOfRoots;
    int nonMarked;
    static int links = 0;
    static int cuts = 0;

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty which is if and only if size == 0.
    *   
    */
    public boolean isEmpty()
    {
    	return size == 0;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key) {
        HeapNode newNode = new HeapNode(key);
        if (isEmpty()){
            sentinel = new HeapNode(true);
            sentinel.setPrev(newNode);
            sentinel.setNext(newNode);
            newNode.setNext(sentinel);
            newNode.setPrev(sentinel);
            min = sentinel.getPrev();
        } else { // implement lazy insert.
            HeapNode prevLast = sentinel.getNext();
            newNode.setPrev(sentinel);
            newNode.setNext(prevLast);
            sentinel.setNext(newNode);
            prevLast.setPrev(newNode);
            if (key < min.getKey()) min = newNode; // change min if necessary.
        }
        size++;
        numOfRoots++;
        nonMarked++;
    	return newNode;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
        if (!isEmpty()){
            nonMarked--; // min is always a root so, it has to be not marked.
            if (size == 1){ // if min is the only node in the heap.
                size = 0;
                numOfRoots = 0;
                min = null;
            } else {
                if (numOfRoots == 1){
                    sentinel = min.getChild();
                    sentinel.setParent(null);

                    min = null;
                    size--;
                    numOfRoots--;
                    numOfRoots += getNumOfSiblingsOfSen(sentinel);
                } else {
                    if (min.getChild() != null) {
                        HeapNode minChild = min.getChild();
                        HeapNode prevOfMin = min.getPrev();
                        HeapNode nextOfMin = min.getNext();
                        HeapNode first = minChild.getPrev();
                        HeapNode last = minChild.getNext();
                        HeapNode temp = sentinel.getNext();

                        min = null;
                        size--;
                        numOfRoots--;
                        numOfRoots += getNumOfSiblingsOfSen(minChild);

                        first.setNext(nextOfMin);
                        nextOfMin.setPrev(first);
                        last.setPrev(prevOfMin);
                        prevOfMin.setNext(last);

                        while (temp != sentinel){
                            temp.setParent(null);
                            temp = temp.getNext();
                        } // making sure  nodes that have no parents if and only if they are roots. (# note 4 from the forum).

                    } else {
                        HeapNode prevOfMin = min.getPrev();
                        HeapNode nextOfMin = min.getNext();

                        min = null;
                        size--;
                        numOfRoots--;

                        prevOfMin.setNext(nextOfMin);
                        nextOfMin.setPrev(prevOfMin);
                    }
                }

            }
            // now we melded min's children to the forest, and we can start successive linking.
            if (size != 0){
                HeapNode[] buckets;
                if (numOfRoots > 1) // only if number of roots is bigger than 1 we shall consolidate.
                    buckets = successiveLinking();
                else
                    buckets = new HeapNode[] {sentinel.getNext()};
                sentinel = new HeapNode(true);
                for (int i = buckets.length - 1; i >= 0; i--) {
                    HeapNode node = buckets[i];
                    if (node != null) {
                        if (buckets.length > 1) // if buckets' length is 1, the tree may not be a binomial tree.
                            node.setRank(i);
                        node.setParent(null); // nodes are now roots (just to be sure).
                        if (min == null || min.getKey() > node.getKey())
                            min = node;
                        sentinel.connect(node);
                    }
                }
                numOfRoots = getNumOfSiblingsOfSen(sentinel);
            }
        }
    }

    /**
     *
     * @param sentinel
     * @return Number of sentinel's siblings.
     */
    private int getNumOfSiblingsOfSen(HeapNode sentinel) {
        HeapNode temp = sentinel.getNext();
        int count = 0;
        while (temp != sentinel){
            count ++;
            temp = temp.getNext();
        }
        return count;
    }


    /**
     * Mimics successive linking process taught in class.
     */
    private HeapNode[] successiveLinking() {
        int n = (int) Math.ceil(Math.log1p(size) / Math.log(2));
        HeapNode[] buckets = new HeapNode[n]; // there will be at most log_2(size) buckets.
        HeapNode temp = sentinel.getNext();
        for (int i = 0; i < numOfRoots; i++){
            int rank = temp.getRank();
            HeapNode temp2 = temp;
            temp = temp.getNext();
            while (buckets[rank] != null) {
                // linking
                HeapNode a = buckets[rank];
                HeapNode b = temp2;
                buckets[rank] = null;

                if (a.getKey() <= b.getKey()) {
                    a.addChild(b);
                    temp2 = a;
                } else {
                    b.addChild(a);
                }
                links ++;

                temp2.setNext(null);
                temp2.setPrev(null);
                temp2.setRank(temp2.getRank() + 1);

                rank++;
            }
            buckets[rank] = temp2;
        }
        return buckets;
    }

    /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()
    {
    	return min; // returns null if and only if isEmpty()
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld(FibonacciHeap heap2)
    {
        if (isEmpty() && !heap2.isEmpty()){
            HeapNode heap2First = heap2.getSentinel().getPrev();
            HeapNode heap2Last = heap2.getSentinel().getNext();
            sentinel = new HeapNode(true);

            sentinel.setPrev(heap2First);
            sentinel.setNext(heap2Last);
            heap2First.setNext(sentinel);
            heap2Last.setPrev(sentinel);

            min = heap2.findMin();
            size = heap2.size();
            numOfRoots = heap2.getNumOfRoots();
            nonMarked = heap2.nonMarked();
        }
        // implements lazy meld.
        else if (!heap2.isEmpty()) {
            // update min if necessary.
            HeapNode heap2Min = heap2.findMin();
            if (heap2Min.getKey() < min.getKey())
                min = heap2Min;

            HeapNode afterSen, beforeSen, bridgeNodeLeft, bridgeNodeRight;

            if (numOfRoots != 1 && heap2.numOfRoots != 1) {
                HeapNode first1 = sentinel.getPrev();
                HeapNode last1 = sentinel.getNext();
                HeapNode first2 = heap2.getSentinel().getPrev();
                HeapNode last2 = first2.getNext();

                afterSen = last1;
                beforeSen = first2;
                bridgeNodeLeft = first1;
                bridgeNodeRight = last2;
            } else if (numOfRoots == 1 && heap2.numOfRoots != 1){
                HeapNode a = sentinel.getPrev();
                HeapNode f = heap2.getSentinel().getPrev();
                HeapNode l = heap2.getSentinel().getNext();

                afterSen = a;
                beforeSen = f;
                bridgeNodeLeft = a;
                bridgeNodeRight = l;
            } else if (heap2.getNumOfRoots() == 1 && numOfRoots != 1){
                HeapNode a = heap2.getSentinel().getPrev();
                HeapNode f = sentinel.getPrev();
                HeapNode l = sentinel.getNext();

                afterSen = l;
                beforeSen = a;
                bridgeNodeLeft = f;
                bridgeNodeRight = a;
            } else {
                HeapNode a = sentinel.getPrev();
                HeapNode b = heap2.getSentinel().getPrev();

                afterSen = a;
                beforeSen = b;
                bridgeNodeLeft = a;
                bridgeNodeRight = b;
            }
            sentinel.setNext(afterSen);
            sentinel.setPrev(beforeSen);
            beforeSen.setNext(sentinel);
            afterSen.setPrev(sentinel);
            bridgeNodeLeft.setNext(bridgeNodeRight);
            bridgeNodeRight.setPrev(bridgeNodeLeft);

            // update size and number of roots.
            size += heap2.size();
            numOfRoots += heap2.getNumOfRoots();
            nonMarked += heap2.nonMarked();
        }

    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size() {
    	return size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of the array depends on the maximum order of a tree.)
    * 
    */
    public int[] countersRep()
    {
        if (isEmpty()) return new int[0];

    	HeapNode temp = sentinel.getNext();
        int[] ranks = new int[getMaximalRankOfForest(sentinel) + 1];
        while (temp != sentinel){
            int rank = temp.getRank();
            ranks[rank]++;
            temp = temp.getNext();
        }
        return ranks;
    }

    /**
     *
     * @param sentinel
     * @return The highest rank of sentinel's siblings.
     */
    private int getMaximalRankOfForest(HeapNode sentinel) {
        HeapNode temp = sentinel.getNext();
        int maxRank = temp.getRank();
        while (temp != sentinel){
            temp = temp.getNext();
            if (maxRank < temp.getRank())
                maxRank = temp.getRank();
        }
        return maxRank;
    }

    /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {
        if (x == min) deleteMin();
        else {
            int delta = 1 + (x.getKey() - min.getKey());
            decreaseKey(x, delta); // now x's key is the min's key minus 1, making it the new minimum.
            deleteMin();
        }
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	// we assume x is in the heap, meaning the heap isn't empty.
        x.setKey(x.getKey() - delta);
        if (x.getParent() != null) { // x isn't a root.
            HeapNode parent = x.getParent();
            boolean wasMarked = parent.isMarked();
            if (x.getKey() < parent.getKey()){
                cutFromParent(x);
                if (wasMarked) {
                    cascadingCuts(parent);
                }
            }
        }

        // update min if required.
        if (x.getKey() < min.getKey())
            min = x;
    }

    /**
     *
     * @param node
     * @pre node is marked.
     */
    private void cascadingCuts(HeapNode node) {
        boolean wasMarked;
        do {// if a node is marked it assures that its parent exists,
            // otherwise it would be a root and from that it is unmarked.
            HeapNode temp = node.getParent();
            wasMarked = temp.isMarked();
            cutFromParent(node);
            node = temp;
        } while (wasMarked);
    }

    /**
     *
     * @param node
     * @pre temp.getParent() != null
     * cuts node from its parent, and if parent is non - root it marks it if it is unmarked.
     */
    private void cutFromParent(HeapNode node) {
        cuts++;
        HeapNode parent = node.getParent();
        parent.setRank(parent.getRank()-1);
        if (parent.getParent() != null && !parent.isMarked()){ // parent isn't root.
            parent.setMarked(true);
            nonMarked--;
        }

        HeapNode sentinelOfNode = node;
        while (!sentinelOfNode.isSentinel()){
            sentinelOfNode = sentinelOfNode.getNext();
        }

        if (getNumOfSiblingsOfSen(sentinelOfNode) == 1){ // meaning node has no siblings.
            parent.setChild(null);
        } else { // node has at least one sibling.
            HeapNode prevOfNode = node.getPrev();
            HeapNode nextOfNode = node.getNext();
            prevOfNode.setNext(nextOfNode);
            nextOfNode.setPrev(prevOfNode);
        }

        sentinel.connect(node);
        node.setParent(null);
        if (node.isMarked()) {
            nonMarked++; // the node becomes not marked.
            node.setMarked(false);
        }

        numOfRoots++;
        if (node.getKey() < min.getKey())
            min = node;
    }

    /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
    */
    public int nonMarked() 
    {    
        return nonMarked;
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
        return numOfRoots + 2 * (size - nonMarked);
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return links;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return cuts;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {
        if (k == 0) return new int[0];

        FibonacciHeap help = new FibonacciHeap();
        help.myInsert(H.findMin()); // the root is the min.

        int[] kMin = new int[k];
        int j = 0;
        while (j < k){ // k iterations
            int minKey = help.findMin().getKey();
            HeapNode original = help.findMin().getOriginal();

            kMin[j] = minKey;
            insertChildren(help, original); // each insert is O(1) and there are at most deg(H) children.

            help.deleteMin(); // there are at most 2*deg(H) + 1 roots, so the runtime is at most O(deg(H)) (WC).
            j++;
        } // overall runtime of O(k*deg(H)) WC.
        return kMin;
    }

    /**
     * helper method for kMin, inserts the node's key to the heap and sets the original of the new inserted node
     * of the heap to be the original node.
     * @param node
     */
    private void myInsert(HeapNode node) {
        HeapNode inserted = insert(node.getKey());
        inserted.setOriginal(node);
    }

    /**
     * inserts with myInsert all of node's children to h.
     * @param h
     * @param node
     */
    private static void insertChildren(FibonacciHeap h, HeapNode node) {
        if (node.getChild() != null) {
            HeapNode temp = node.getChild().getNext();
            while (!temp.isSentinel()){
                h.myInsert(temp);
                temp = temp.getNext();
            }
        }
    }

    /**
     *
     * @return The sentinel of the heap.
     */
    public HeapNode getSentinel() {
        return sentinel;
    }

    public int getNumOfRoots() {
        return numOfRoots;
    }

    /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key;
        public int rank;
        public boolean isMarked;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;
        public boolean isSentinel;

    	public HeapNode(int key) {
    		this.key = key;
            this.isMarked = false; // initially, the node isn't marked.
            this.isSentinel = false; // initially, the node isn't a sentinel.
    	}

        public HeapNode(boolean isSentinel) {
            this.isSentinel = isSentinel;
        }

    	public int getKey() {
    		return this.key;
    	}

       public void setKey(int key) {
           this.key = key;
       }

       public int getRank() {
           return rank;
       }

       public void setRank(int rank) {
           this.rank = rank;
       }

       public boolean isMarked() {
           return isMarked;
       }

       public void setMarked(boolean marked) {
           isMarked = marked;
       }

        /**
         *
         * @return The sentinel child of the heap.
         */
       public HeapNode getChild() {
           return child;
       }

       public void setChild(HeapNode child) {
           this.child = child;
       }

       public HeapNode getNext() {
           return next;
       }

       public void setNext(HeapNode next) {
           this.next = next;
       }

       public HeapNode getPrev() {
           return prev;
       }

       public void setPrev(HeapNode prev) {
           this.prev = prev;
       }

       public HeapNode getParent() {
           return parent;
       }

       public void setParent(HeapNode parent) {
           this.parent = parent;
       }

        public boolean isSentinel() {
            return isSentinel;
        }

        /**
         *
         * @param node The node linked to this heap.
         * @pre: node.getRank() == getRank()
         */
        public void addChild(HeapNode node) {
            // notice: im not deleting as a next / prev of other nodes because addChild is a procedure of
            // successive linking and not a stand-alone method. So, S-L will take the responsibility to delete those.
            if (getChild() == null){
                child = new HeapNode(true);
                child.setParent(this);
            }
            child.connect(node);
            node.setParent(this);
        }

        /**
         * @param node
         * @pre: isSentinel()
         */
        public void connect(HeapNode node){
            if (getNext() == null){
                // sentinel has no siblings.
                setNext(node);
                setPrev(node);
                node.setNext(this);
                node.setPrev(this);
            } else {
                HeapNode prevLast = getNext();
                prevLast.setPrev(node);
                node.setNext(prevLast);
                node.setPrev(this);
                setNext(node);
            }
        }

        // this is for the kMin method, it has no use otherwise. it saves the original node.
        // so, at the end original.getKey() == key.
        private HeapNode original;

        public void setOriginal(HeapNode node) {
            original = node;
        }

        public HeapNode getOriginal() {
            return original;
        }
    }
}
