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
        // Heap is empty
        if (this.isEmpty()){
            return;
        }
        // If it is the only node in the heap, we shall
        if (this.size() == 1) {
            this.min = null;
            this.head = null;
            this.size = 0;
            this.treesAmount = 0;
        }


        HeapNode currMin = this.findMin();
        int newTrees = 0;       // Count the new trees amount
        int wasMarked = 0;      // Count the children which was marked, after deletion they roots.

        // If currMin has children we shall disconnect the 'parent's pointers they have.
        if (currMin.hasChild()){

            HeapNode firstChild = currMin.getChild();  // Keep pointer to the first child
            HeapNode lastChild = firstChild.getPrev(); // Keep pointer to the last child

            HeapNode currChild = firstChild;          // Init a runner
            do {
                currChild.setParentToNull();
                newTrees++;                    // Each child will become a tree
                if (currChild.isMarked()) {
                    currChild.unMarkNode();
                    wasMarked++;
                }
                currChild = currChild.getNext();
            } while (currChild != firstChild);

            // Insert the children to the relative position of currMin.
            // Handle below the case when currMin is a lonely root.
            HeapNode minPrev = currMin.getPrev();
            HeapNode minNext = currMin.getNext();

            // Setting new pointers
            minPrev.setNext(firstChild);
            firstChild.setPrev(minPrev);
            lastChild.setNext(minNext);
            minNext.setPrev(lastChild);

            // currMin is a lonely root.
            if (treesAmount == 1) {
                firstChild.setPrev(lastChild);
                lastChild.setNext(firstChild);
            }

            // Updating 'head'
            if (this.head == currMin) {
                this.head = firstChild;
            }
        }
        // If currMin doesn't have children
        else {
            // Updating pointers
            HeapNode minPrev = currMin.getPrev();
            HeapNode minNext = currMin.getNext();
            minPrev.setNext(minNext);
            minNext.setPrev(minPrev);
            // Updating head
            if (this.head == currMin){
                this.head = minNext;
            }
        }

        this.treesAmount += newTrees;
        this.markedAmount -= wasMarked;

        // Now we have a legal Fibonnaci Heap.
        // Remained to operate Successive linking

        this.successiveLinking();

        return;
    }

    // Successive Linking - UNDONE!
    /*
        IDEA:
        Iterating through the roots and using and array to match them as shown in class.
        NOTE:
        When putting a root in the array, we shall close the circle in the roots' "DLL".

     */
    public void successiveLinking() {
        HeapNode[] pool = new HeapNode[50]; // maxRank < log_2(2^32) * 1.5 = 48 (Conclusion from class)

        HeapNode currNode = this.head;
        HeapNode tempNode;

        while (currNode != null){
            int r = currNode.getRank();

            // If the 'r' spot in the pool is empty
            if (pool[r] == null){
                pool[r] = currNode;
                tempNode = currNode;
                currNode = currNode.getNext();
                tempNode.setNextToNull();
                tempNode.setPrevToNull();
            }
        }

        return;
    }
    // Standard link operation between two nodes (which are roots when called)
    public HeapNode link(HeapNode node1, HeapNode node2){
        if (node1.getKey() < node2.getKey()) {

            // Connecting node1 to node2's children
            node1.setNext(node2.getChild());
            node1.setPrev(node2.getChild().getPrev());
            node2.getChild().setPrev(node1);
            node2.getChild().getPrev().setNext(node1);

            // Connecting node1 to node2 *as a child*
            node2.setChild(node1);
            node1.setParent(node2);

            // Increasing rank and totalLinks
            node2.rank++; // Consider to change to separate function
            totalLinks++;
            return node2;
        } else {
            // Shifting the order between the two.
            // The keys are unique so it is a valid move.
            return link(node2, node1);
        }
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
    public void delete(HeapNode x) { // temporary - ask sagi
        // We decrease x's key to minimum value ("minus infinity")
        x.setKey(Integer.MIN_VALUE);
        decreaseKey(x , 0);
        // Now x is surely the minimum node in the heap, so we use deleteMin() to delete this node
        this.deleteMin();
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     * <p>
     * The function decreases the key of the node x by delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     */
    public void decreaseKey(HeapNode x, int delta) {
        x.setKey(x.getKey()-delta);
        // Update min HeapNode if necessary
        if(x.getKey() < this.min.getKey()) {
            this.min = x;
        }
        // If x's key is smaller then its parent's key - we need to cut the edge.
        // Therefore we will use cascadingCuts, as we saw in class
        if(x.getParent() != null && x.getParent().getKey() > x.getKey()) {
            cascadingCuts(x, x.getParent());
        }
    }

    /**
     * public void cascadingCuts(HeapNode x, HeapNode y)
     *
     * similar to cascadingCuts method we saw in class
     * here we assume that y is x's parent
     */
    public void cascadingCuts(HeapNode x, HeapNode y){ // add time complexity
        cut(x,y);
        if(y.getParent() != null) {
            if(! y.isMarked()) {
                y.markNode();
                this.markedAmount++;
            }
            else {
                cascadingCuts(y, y.getParent());
            }
        }
    }

    /**
     * private void cut(HeapNode x, HeapNode y)
     *
     * We use this method for implementing cascadingCuts.
     * Similar to cut method we saw in class
     * here we assume that y is x's parent
     */

    private void cut(HeapNode x, HeapNode y) { // O(1)
        // Cut x from y
        x.setParentToNull();
        x.unMarkNode();
        this.markedAmount--;
        // Update y's rank
        y.setRank(y.getRank()-1);
        if(x.getNext() == x) {
            y.setChild(null);
        }
        else {
            if(y.getChild() == x) {
                // Define new child for y
                HeapNode newChild = x.getNext();
                y.setChild(newChild);
                newChild.setPrev(x.getPrev());
                // x becomes a root, so we update its prev,next to null
                x.setNextToNull();
                x.setPrevToNull();
            }
            else {
                // Update pointers of y's children, after cutting x
                x.getNext().setPrev(x.getPrev());
                x.getPrev().setNext(x.getNext());
            }
        }
        // Move x to the beginning of the heap
        x.setNext(this.head);
        this.head = x;
        // Update the trees amount in the heap
        this.treesAmount++;
    }

    /**
     * public int potential()
     * <p>
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap.
     */
    public int potential() {
        return this.treesAmount + (2 * this.markedAmount);
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
        // Setup
        int[] result = new int[k];
        int i = 0;

        // Using FibonacciHeap as a min-priority queue
        FibonacciHeap Q = new FibonacciHeap();

        // Inserting the first element to Q, which must be exist according to instructions.
        // We want to keep a pointer to the original node in H, so we could travel there.
        HeapNode insertedNode = Q.insert((H.findMin().getKey()));
        insertedNode.setValue(H.findMin());

        // Initialize variables for readability
        HeapNode currNode;
        HeapNode firstChild;
        HeapNode currChild;

        // While the result array isn't full, we shall do the following:
        while (i < k) {
            currNode = Q.findMin().getValue();   // Retrieve the minimum key *value* in Q (It's node in H!)
            result[i] = currNode.getKey();       // Insert to result in the correct index 'i'

            if (currNode.hasChild()){            // If currNode has children in H to be explored, we shall add them to Q
                firstChild = currNode.getChild();
                currChild = firstChild;

                do {                             // Iterating through the children (in H) and add them to Q
                    insertedNode = Q.insert(currChild.getKey()); // Insert to Q
                    insertedNode.setValue(currChild);            // Keeping pointer to the node in H
                    currChild = currChild.getNext();             // Promote
                } while (currChild != firstChild);
            }

            i++;                                    // Promote 'i'
            Q.deleteMin();                          // Delete the node we worked on
        }

        return result;
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
        public int mark;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;

        public HeapNode value;

        public HeapNode(int key) {
            this.key = key;
            this.prev = this;
            this.next = this;
        }


        public int getKey() {
            return this.key;
        }

        public void setKey(int newKey) {
            this.key = newKey;
        }

        public int getRank() {
            return this.rank;
        }

        public void setRank(int newRank) {
            this.rank = newRank;
        }

        public boolean isMarked() {
            return (this.mark == 1);
        }

        public void markNode() {
            this.mark = 1;
        }

        public void unMarkNode() {
            this.mark = 0;
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

        // Returning true iff  node has a child
        public boolean hasChild() {
            return (this.child != null);
        }

        public HeapNode getValue() {
            return this.value;
        }

        public void setValue(HeapNode node) {
            this.value = node;
        }

        public void setParentToNull() {
            this.parent = null;
        }

        public void setNextToNull() {
            this.next = null;
        }

        public void setPrevToNull() {
            this.prev = null;
        }


    }
}
