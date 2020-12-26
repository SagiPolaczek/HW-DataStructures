import java.util.Arrays;

/**
 * FibonacciHeap
 * <p>
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap {

    public HeapNode min;
    public HeapNode head;
    public int size;
    static public int totalLinks;
    static public int totalCuts;
    public int markedAmount;
    public int treesAmount;

    public FibonacciHeap() {
    }


    /**
     * public boolean isEmpty()
     * <p>
     * precondition: none
     * <p>
     * The method returns true if and only if the heap
     * is empty.
     */
    public boolean isEmpty() {
        return (size == 0); // O(1)
    }

    /**
     * public HeapNode insert(int key)
     * <p>
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * <p>
     * Returns the new node created.
     */
    public HeapNode insert(int key) {       // Clearly, Trivial, Easy to see etc. O(1)
        // Initialize the insert node
        HeapNode newNode = new HeapNode(key);

        // Heap is empty
        if (this.isEmpty()){
            this.head = newNode;
            this.min = newNode;
        }
        // Heap is not empty
        else {
            // Updating pointers
            HeapNode last = this.head.getPrev();
            this.head.setPrev(newNode);
            newNode.setNext(this.head);
            newNode.setPrev(last);
            last.setNext(newNode);
            this.head = newNode;

            // Updating minimum
            if (key < this.min.getKey()){
                this.min = newNode;
            }
        }

        // Updating size
        this.size++;

        return newNode;
    }

    /**
     * public void deleteMin()
     * <p>
     * Delete the node containing the minimum key.
     */
    public void deleteMin() {
        return; // should be replaced by student code

    }

    // Successive Linking - May need to change signature!!!
    public void successiveLinking() {
        return;
    }

    /**
     * public HeapNode findMin()
     * <p>
     * Return the node of the heap whose key is minimal.
     */
    public HeapNode findMin() {
        return this.min;
    }

    /**
     * public void meld (FibonacciHeap heap2)
     * <p>
     * Meld the heap with heap2
     */
    public void meld(FibonacciHeap heap2) {
        // this heap is empty
        if (this.isEmpty()){
            this.head = heap2.head;
            this.min = heap2.min;
        }

        // None of the heaps are empty
        else if (!heap2.isEmpty()){
            HeapNode thisRightMost = this.head.getPrev();
            HeapNode otherRightMost = heap2.head.getPrev();
            // Connecting the heaps
            thisRightMost.setNext(heap2.head);
            heap2.head.setPrev(thisRightMost);
            // Closing the circle
            otherRightMost.setNext(this.head);
            this.head.setPrev(otherRightMost);

            // Updating minimum
            if(heap2.findMin().getKey() < this.min.getKey()){
                this.min = heap2.findMin();
            }
        }

        // Updating fields' values
        this.size += heap2.size;
        this.totalCuts += heap2.totalCuts;
        this.totalLinks += heap2.totalLinks;
        this.markedAmount += heap2.markedAmount;
        this.treesAmount += heap2.treesAmount;
    }

    /**
     * public int size()
     * <p>
     * Return the number of elements in the heap
     */
    public int size() {
        return this.size;
    }

    /**
     * public int[] countersRep()
     * <p>
     * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap.
     */
    public int[] countersRep() { // O(treesAmount)
        // If empty return an empty array
        if (this.isEmpty()){
            return new int[0];
        }
        // Settings
        int[] arr = new int[this.size];
        int maxRank = 0;
        int currRank;
        HeapNode currNode = this.head;

        // Iterating of the roots and increasing values as needed
        do {
            currRank = currNode.getRank();
            arr[currRank]++;
            currNode = currNode.getNext();
            if (currRank > maxRank){
                maxRank = currRank;
            }
        } while (currNode != this.head);

        // Cut the array to the appropriate range
        return Arrays.copyOfRange(arr, 0,maxRank+1);
    }

    /**
     * public void delete(HeapNode x)
     * <p>
     * Deletes the node x from the heap.
     */
    public void delete(HeapNode x) {
        return; // should be replaced by student code
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     * <p>
     * The function decreases the key of the node x by delta. The structure of the heap should be updated
     * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta) {
        return; // should be replaced by student code
    }

    // May need to change signature!!!
    public void cascadingCuts(){
        return;
    }

    /**
     * public int potential()
     * <p>
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap.
     */
    public int potential() {
        return treesAmount + (2 * markedAmount);
    }

    /**
     * public static int totalLinks()
     * <p>
     * This static function returns the total number of link operations made during the run-time of the program.
     * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of
     * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value
     * in its root.
     */
    public static int totalLinks() {
        return totalLinks;
    }

    /**
     * public static int totalCuts()
     * <p>
     * This static function returns the total number of cut operations made during the run-time of the program.
     * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods).
     */
    public static int totalCuts() {
        return totalCuts;
    }

    /**
     * public static int[] kMin(FibonacciHeap H, int k)
     * <p>
     * This static function returns the k minimal elements in a binomial tree H.
     * The function should run in O(k*deg(H)).
     * You are not allowed to change H.
     */
    public static int[] kMin(FibonacciHeap H, int k) {
        int[] arr = new int[42];
        return arr; // should be replaced by student code
    }

    /**
     * public class HeapNode
     * <p>
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in
     * another file
     */
    public class HeapNode {

        public int key;
        public int rank;
        public boolean mark;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;

        public HeapNode(int key) {
            this.key = key;
            this.prev = this;
            this.next = this;
        }

        public int getKey() {
            return this.key;
        }

        public int getRank() {
            return this.rank;
        }

        public boolean isMarked() {
            return this.mark;
        }

        public HeapNode getChild() {
            return this.child;
        }

        public void setChild(HeapNode node) {
            this.child = node;
        }

        public HeapNode getParent() {
            return this.parent;
        }

        public void setParent(HeapNode node) {
            this.parent = node;
        }

        public HeapNode getNext() {
            return this.next;
        }

        public void setNext(HeapNode node) {
            this.next = node;
        }

        public HeapNode getPrev() {
            return this.prev;
        }

        public void setPrev(HeapNode node) {
            this.prev = node;
        }


    }
}
