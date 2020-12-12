/**
 * AVLTree
 * <p>
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

public class AVLTree {

    /**
     * pos - indicates index in array, while implementing keysToArray() & infoToArray() methods
     * size - indicates the nummber of AVLnodes in our tree
     * min, max - indicates the AVLnodes in our tree with the min,max keys
     */

    // Initialize variables to default
    IAVLNode root, min, max;   // null
    int size, pos;             // 0

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {  // Takes O(1) time
        return (this.root == null);
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) { // Takes O(log(n)) time
        IAVLNode node = searchNode(k); // This node will be with key 'k', if there is one in the tree. Otherwise it will be null
        if (node != null) {
            return node.getValue();
        }
        return null;
    }

    // Search node with key 'k' and return THE NODE ITSELF, or null if didn't found
    public IAVLNode searchNode(int k) {  // Takes O(log(n)) time
        if (this.empty()) { // The tree is empty
            return null;
        }
        IAVLNode curr = this.root;
        // Preforming Binary Search as usual
        while (curr.isRealNode()) {
            int currKey = curr.getKey();
            if (currKey == k) {
                return curr;
            } else if (currKey > k) {
                curr = curr.getLeft();
            } else if (currKey < k) {
                curr = curr.getRight();
            }
        }
        // We arrived virtual node, thus node does not exist in tree
        return null;
    }

    /**
     * public int insert(int k, String i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) { // Takes O(log(n)) time
        AVLTree.IAVLNode node = new AVLTree.AVLNode(k,i);
        if(root == null) {
            this.root = node;
            this.min = node;
            this.max = node;
            this.size++;
            return 0;
        }
        AVLTree.IAVLNode parent = searchPosition(k);
        if(parent == null) {
            return -1;
        }
        boolean isLeaf = false;
        if (parent.isLeaf()) {
            isLeaf = true;
        }
        // update min, max if needed
        if(k < min.getKey()) {
            min = node;
        }
        if(k > max.getKey()) {
            max = node;
        }
        // insert node to the tree
        if(k < parent.getKey()) {
            parent.setLeft(node);
        }
        else { // k > parent.getKey()
            parent.setRight(node);
        }
        node.setParent(parent);
        this.size++; // update tree's size after insertion
        updatePathSize(node);
        int balanceCnt = 1; // since insert is also count as balance operation, we start to count from 1
        //check if case A, since if it's case B we don't need to do anything else
        if(isLeaf) {
            balanceCnt = insertRebalance(parent, 1);
        }
        return balanceCnt;
    }

    // Look for k insertion position.
    // If found k, return null (nothing to insert), else return last node encountered.
    // (similar to method implementation we saw in class)
    private IAVLNode searchPosition(int k) { // Take O(log(n)) time
        IAVLNode curr = this.root, next = this.root;
        while (next.isRealNode()) { // when next reach to an unreal node, curr is in the position we need for insertion
            curr = next;
            if (next.getKey() == k) {
                return null;
            }
            if (next.getKey() < k) {
                next = next.getRight();
            } else {
                next = next.getLeft();
            }
        }
        return curr;
    }

    // Rotate root node & its right child to the left
    // (after left rotation, root is the left child of the node that was its right child before rotation)
    private void rotateLeft(IAVLNode root) { // Takes O(1) time
        IAVLNode parent = root.getParent();
        IAVLNode rightNode = root.getRight();
        root.setRight(rightNode.getLeft());
        if (root.getRight() != null) {
            rightNode.getLeft().setParent(root);
        }
        rightNode.setLeft(root);
        root.setParent(rightNode);
        rightNode.setParent(parent);
        if (parent != null) {
            if (parent.getLeft() == root) {
                parent.setLeft(rightNode);
            } else {
                parent.setRight(rightNode);
            }
        }
        root.updateHeight(); // first update the lower, since its rightNode's child after rotation
        rightNode.updateHeight();
        root.updateSize();
        rightNode.updateSize();
        if(parent == null) {
            this.root = rightNode;
        }
    }

    // Rotate root node & its left child to the right
    // (after right rotation, root is the right child of the node that was its left child before rotation)
    private void rotateRight(IAVLNode root) {  // Takes O(1) time
        IAVLNode parent = root.getParent();
        IAVLNode leftNode = root.getLeft();
        root.setLeft(leftNode.getRight());
        if (root.getLeft() != null) {
            leftNode.getRight().setParent(root);
        }
        leftNode.setRight(root);
        root.setParent(leftNode);
        leftNode.setParent(parent);
        if (parent != null) {
            if (parent.getLeft() == root) {
                parent.setLeft(leftNode);
            } else {
                parent.setRight(leftNode);
            }
        }
        root.updateHeight(); // first update the lower, since its leftNode's child after rotation
        leftNode.updateHeight();
        root.updateSize();
        leftNode.updateSize();
        if(parent == null) {
            this.root = leftNode;
        }
    }

    // Rebalancing after insertion using cases and rotations.
    // Returns the number of operations required to balance the tree.
    // At start receive 1 as count (we count insert as one operation) and increase it recursively.
    private int insertRebalance(AVLTree.IAVLNode node, int balanceCnt){ // Takes O(log(n)) time
        if(node == null) {
            return balanceCnt;
        }
        int[] rankDiff = node.rankDifference();
        int dLeft = rankDiff[0], dRight = rankDiff[1];
        // When we reach node (1,1) or (1,2) or (2,1) the tree is balanced
        if((dLeft == 1 && dRight == 1) || (dLeft == 1 && dRight == 2) || (dLeft == 2 && dRight == 1)) {
            return balanceCnt;
        }
        // If the node is (1,0) or (0,1), we promote the node and move the problem up (case 1)
        if((dLeft==0 && dRight==1) || (dLeft==1 && dRight==0)){ //case1
            node.promoteNode();
            node = node.getParent();
            balanceCnt++;
            return insertRebalance(node, balanceCnt);
        }
        // If the node is (2,0) or (0,2), we need to rotate the node once (case 2- single rotation) or twice (case 3- double rotation)
        if((dLeft==0 && dRight==2) || (dLeft==2 && dRight==0)) { //case 2 or 3
            if(dLeft == 0) {
                int[]rankDiffL = node.getLeft().rankDifference();
                if(rankDiffL[0] == 1) { // case 2
                    rotateRight(node);
                    node = node.getParent().getParent();
                    balanceCnt+=2;
                    return insertRebalance(node, balanceCnt);
                }
                else if(rankDiffL[0] == 2) { // case 3
                    rotateLeft(node.getLeft());
                    rotateRight(node);
                    node = node.getParent().getParent();
                    balanceCnt+=3;
                    return insertRebalance(node, balanceCnt);
                }
            }
            if(dRight == 0) {
                int[]rankDiffR = node.getRight().rankDifference();
                if(rankDiffR[1] == 1) { // case 2
                    rotateLeft(node);
                    node = node.getParent().getParent();
                    balanceCnt+=2;
                    return insertRebalance(node, balanceCnt);
                }
                else if(rankDiffR[1] == 2) { // case 3
                    rotateRight(node.getRight());
                    rotateLeft(node);
                    node = node.getParent().getParent();
                    balanceCnt+=3;
                    return insertRebalance(node, balanceCnt);
                }
            }
        }
        return 42;
    }


    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) // Takes O(log(n)) time
    {
        IAVLNode target = searchNode(k);
        if (target == null) {  // Node was not found, O(log(n))
            return -1;
        }

        // Check if target node isn't a leaf or an unary node
        // If true, replace it with it's successor, and then delete the successor,
        // 										                    which must be leaf or unary.
        if (target.getLeft().isRealNode() && target.getRight().isRealNode()) {
            // Going right and all the way left.
            IAVLNode targetSuc = target.getRight();
            while (targetSuc.getLeft().isRealNode()) {
                targetSuc = targetSuc.getLeft();
            }
            // targetSuc is indeed the target's successor
            // Replacing by changing key and value, instead of naively changing pointers.
            target.setValue(targetSuc.getValue());
            target.setKey(targetSuc.getKey());

            // After replacing, the target shall be the successor,
            //     						and it must be leaf or unary.
            target = targetSuc;
        }
        IAVLNode targetParent = target.getParent();
        // Special CASE - target node is the root itself. It has at most one child.
        if (targetParent == null) {
            if (target.isLeaf()) {
                this.root = null;
            } else if (target.getLeft().isRealNode()) { // Target's real node is on his left
                this.root = target.getLeft();
                this.root.setParent(null); // Updating root's parent to null.
            } else {                                    // Target's real node is on his right
                this.root = target.getRight();
                this.root.setParent(null); // Updating root's parent to null.
            }
        }

        // CASE A - target is a leaf node (and not a root)
        else if (target.isLeaf()) {
            IAVLNode replaceNode = new AVLNode();
            replaceNode.setParent(targetParent);
            if (targetParent.getLeft().getKey() == target.getKey()) { // target is his parent left child
                targetParent.setLeft(replaceNode);
            } else {                                                  // target is his parent right child
                targetParent.setRight(replaceNode);
            }

            // CASE B - target is an unary node (and not a root)
        } else {
            IAVLNode replaceNode;
            if (target.getLeft().isRealNode()) {                      // Target's real node is on his left
                replaceNode = target.getLeft();
            } else {                                                  // Target's real node is on his right
                replaceNode = target.getRight();
            }
            replaceNode.setParent(targetParent);
            if (targetParent.getLeft().getKey() == target.getKey()) { // Target is his parent left child
                targetParent.setLeft(replaceNode);
            } else {                                                  // Target is his parent right child
                targetParent.setRight(replaceNode);
            }
        }

        // Need to update SIZE, MIN, MAX
        this.size--;                    // O(1)
        this.min = findMin(root);       // O(log(n))
        this.max = findMax(root);       // O(log(n))

        // Update size for all relevant nodes
        updatePathSize(targetParent);   // O(log(n))

        // After the deletion, now its time to rebalance the tree and return the amount of operations executed.
        int count = 0;
        return deleteRebalance(targetParent, count); // O(log(n))
    }


    // Rebalancing after deletion using Cases and rotations.
    // The amount of work is linear to the tree height, because the method does constant time in each call,
    //                                                 and we call it each time we climb from a node to his parent.
    // AVL is a BBST and thus time complexity is O(log(n)).
    // At start receive 0 as count and increase it recursively.
    public int deleteRebalance(IAVLNode node, int count) {

        // Out of tree - Final Case
        if (node == null) {
            return count;
        }

        int[] rankDiff = node.rankDifference();
        int dLeft = rankDiff[0];
        int dRight = rankDiff[1];

        // Node is balanced - Final Case
        if ((dLeft == 2 && dRight == 1) || (dLeft == 1 && dRight == 2) || (dLeft == 1 && dRight == 1)) {
            return count;
        }


        // Demote (Case 1)
        if (dLeft == 2 && dRight == 2) {
            node.demoteNode();
            count++;
            node = node.getParent();
            return deleteRebalance(node, count);
        }
        // Part 1 of Symmetry
        if (dLeft == 3 && dRight == 1) {
            int[] childRankDiff = node.getRight().rankDifference();
            int childDLeft = childRankDiff[0];
            int childDRight = childRankDiff[1];

            // Single Rotation (Case 2 and Case 3)
            if ((childDLeft == 1 && childDRight == 1) || (childDLeft == 2 && childDRight == 1)) {
                rotateLeft(node);
                count += 3;
                node = node.getParent().getParent();
                return deleteRebalance(node, count);
            }
            // Double Rotation (Case 4)
            if (childDLeft == 1 && childDRight == 2) {
                rotateRight(node.getRight());
                count++;
                rotateLeft(node);
                count++;
                node = node.getParent().getParent();
                return deleteRebalance(node, count);
            }

        }
        // Part 2 of Symmetry
        if (dLeft == 1 && dRight == 3) {
            int[] childRankDiff = node.getLeft().rankDifference();
            int childDLeft = childRankDiff[0];
            int childDRight = childRankDiff[1];

            // Single Rotation (Case 2 and Case 3)
            if ((childDLeft == 1 && childDRight == 1) || (childDLeft == 1 && childDRight == 2)) {
                rotateRight(node);
                count += 3; // One Rotation, one demotion and one promotion
                node = node.getParent().getParent();
                return deleteRebalance(node, count);
            }
            // Double Rotation (Case 4)
            if (childDLeft == 2 && childDRight == 1) {
                rotateLeft(node.getLeft());
                rotateRight(node);
                node = node.getParent().getParent();
                count += 6; // Two rotations, Three demotions, One promotion
                return deleteRebalance(node, count);
            }
        }
        // The Answer to the Ultimate Question of Life, the Universe, and Everything
        // Never gets here.
        return 42;
    }

    // By given root to a subtree, returns it's minimum node. - O(log(n))
    // if tree is empty, returns null
    public IAVLNode findMin(IAVLNode root) {
        if (root == null || (!root.isRealNode())) {
            return null;
        }
        IAVLNode curr = root;
        while (curr.getLeft().isRealNode()) {
            curr = curr.getLeft();
        }
        return curr;
    }

    // By given root to a subtree, returns it's maximum node. - O(log(n))
    // if tree is empty, return null
    public IAVLNode findMax(IAVLNode root) {
        if (root == null || (!root.isRealNode())) {
            return null;
        }
        IAVLNode curr = root;
        while (curr.getRight().isRealNode()) {
            curr = curr.getRight();
        }
        return curr;
    }

    // Given node, update his size using updateSize() which sum his two children size +1,
    // Then updating all his parents' sizes including the root.
    public void updatePathSize(IAVLNode node) {
        IAVLNode curr = node;
        while (curr != null) {       // while did not pass the root
            curr.updateSize();       // update size
            curr = curr.getParent(); // climb up
        }
    }


    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        if (this.empty()){
            return null;
        }
        return this.min.getValue();
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        if (this.empty()){
            return null;
        }
        return this.max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() { // Takes O(n) time
        if (size == 0) {         // Tree is empty
            return new int[0]; // Return an empty array
        }
        int[] result = new int[size];
        IAVLNode runner = this.root;
        keysInorderTraversal(result, runner); // insert all keys to result array, using inorder traversal over the tree
        // We use pos as the array's current index to insert values while using keysInorderTraversal method.
        // Therefore, each time we call this method we want pos to be equal to 0.
        this.pos = 0;
        return result;
    }

    /**
     * private void keysInorderTraversal(String[] arr, IAVLNode root)
     * <p>
     * recursive function which insert the key value of each node in our AVLTree into an existing array of integers,
     * using inorder traversal.
     */

    // Inorder traversal over AVL tree
    // While traversing the tree, we will insert the key values of the nodes in the tree into the array arr
    private void keysInorderTraversal(int[] arr, IAVLNode root) { // Takes O(n) time
        if (root.isRealNode()) {
            keysInorderTraversal(arr, root.getLeft());
            arr[this.pos] = root.getKey();
            this.pos++;
            keysInorderTraversal(arr, root.getRight());
        }
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() { // Takes O(n) time
        if (size == 0) {         // Tree is empty
            return new String[0]; // Return an empty array
        }
        String[] result = new String[size];
        IAVLNode runner = this.root;
        infoInorderTraversal(result, runner);
        // We use pos as the array's current index to insert values while using keysInorderTraversal method.
        // Therefore, each time we call this method we want pos to be equal to 0.
        this.pos = 0;
        return result;
    }

    /**
     * private void infoInorderTraversal(String[] arr, IAVLNode root)
     * <p>
     * recursive function which insert the info value of each node in our AVLTree into an existing array of Strings,
     * using inorder traversal.
     */

    // Inorder traversal over AVL tree
    // While traversing the tree, we will insert the string values of the nodes in the tree into the array arr
    private void infoInorderTraversal(String[] arr, IAVLNode root) { // Takes O(n) time
        if (root.isRealNode()) {
            infoInorderTraversal(arr, root.getLeft());
            arr[this.pos] = root.getValue();
            this.pos++;
            infoInorderTraversal(arr, root.getRight());
        }
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     * <p>
     * precondition: none
     * postcondition: none
     */
    public int size() { // Takes O(1) time
        return this.size;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     * <p>
     * precondition: none
     * postcondition: none
     */
    public IAVLNode getRoot() { // Takes O(1) time
        return this.root;
    }

    /**
     * public string split(int x)
     * <p>
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public AVLTree[] split(int x) {
        AVLTree[] splitted = new AVLTree[2];
        splitted[0] = new AVLTree();
        splitted[1] = new AVLTree();
        IAVLNode node = searchNode(x);
        if(x == this.min.getKey()) {
            this.delete(x);
            splitted[1] = this;
        }
        else if(x == this.max.getKey()) {
            this.delete(x);
            splitted[0] = this;
        }
        else {
            // set root,size values for each subtree
            setRootSizeValues(splitted, node);
            node.setLeft(new AVLNode());
            node.setRight(new AVLNode());
            node.updateHeight();
            node.updateSize();
            // split
            IAVLNode child = node, parent = child;
            while (child.getParent() != null) {
                parent = child.getParent();
                AVLTree temp = new AVLTree();
                if (parent.getLeft() == child) {
                    parent.setLeft(new AVLNode());
                    temp.root = parent.getRight();
                    temp.size = parent.getRight().getSize();
                    parent.setRight(new AVLNode());
                } else { // child is right child of parent
                    parent.setRight(new AVLNode());
                    temp.root = parent.getLeft();
                    temp.size = parent.getLeft().getSize();
                    parent.setLeft(new AVLNode());
                }
                temp.root.setParent(null);
                child.setParent(null);
                IAVLNode parentClone = new AVLNode(parent.getKey(), parent.getValue());
                if (parent.getKey() < x) {
                    splitted[0].join(parentClone, temp);
                } else {
                    splitted[1].join(parentClone, temp);
                }
                parent.updateHeight();
                parent.updateSize();
                child = parent;
            }
            // set min,max values for each subtree
            setMinMaxValues(splitted);
        }
        return splitted;
    }

    private void setRootSizeValues(AVLTree[] splitted, IAVLNode node) {
        splitted[0].root = node.getLeft();
        splitted[0].size = node.getLeft().getSize();
        splitted[0].root.setParent(null);
        splitted[1].root = node.getRight();
        splitted[1].size = node.getRight().getSize();
        splitted[1].root.setParent(null);
    }

    private void setMinMaxValues(AVLTree[] splitted) {
        splitted[0].min = findMin(splitted[0].root);
        splitted[0].max = findMax(splitted[0].root);
        splitted[1].min = findMin(splitted[1].root);
        splitted[1].max = findMax(splitted[1].root);
    }

    /**
     * public join(IAVLNode x, AVLTree t)
     * <p>
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t) {

        // Both trees are empty
        // Equivalent to new tree with x as a single node
        if (this.empty() && t.empty()) {
            this.insert(x.getKey(), x.getValue());
            return 1;
        }


        // this is empty, t not.
        // Equivalent to add x to t and make this point to t.
        if (this.empty()) {
            int rankT2 = t.root.getHeight();
            t.insert(x.getKey(), x.getValue());
            this.makeCopyOf(t);
            return rankT2 + 2;
        }
        // t is empty, this not.
        // Equivalent to add x to this.
        if (t.empty()) {
            int rankT1 = this.root.getHeight();
            this.insert(x.getKey(), x.getValue());
            return rankT1 + 2;
        }
        // Calculate ranks to return ( |rank(this) - rank(t)| + 1 )
        int rankT1 = this.root.getHeight();
        int rankT2 = t.root.getHeight();
        IAVLNode currNode;
        // keys(x,t) < keys()
        if (x.getKey() < this.root.getKey()) {

            // keys(x,t) < keys() and rankT1 >= rankT2
            // 'this' is higher than 't', thus we'll start from this.root and go down.
            if (this.root.getHeight() >= t.root.getHeight()) {
                currNode = this.root;
                while (currNode.getHeight() > t.root.getHeight()) {
                    currNode = currNode.getLeft();
                }
                x.setParent(currNode.getParent());
                currNode.setParent(x);
                t.root.setParent(x);
                x.setRight(currNode);
                x.setLeft(t.root);
                if (x.getParent() != null) {
                    x.getParent().setLeft(x);
                }

                // Defining this tree as the new one in case that 'x' is above root,
                //                                      possible if rankT1 == rankT2 for instance.
                if (this.root.getParent() != null) {
                    this.root = this.root.getParent();
                }

            }

            // keys(x,t) < keys() and rankT1 < rankT2
            // 't' is higher than 'this', thus we'll start from t.root and go down.
            else {
                currNode = t.root;
                while (currNode.getHeight() > this.root.getHeight()) {
                    currNode = currNode.getRight();
                }
                x.setParent(currNode.getParent());
                currNode.setParent(x);
                this.root.setParent(x);
                x.setRight(this.root);
                x.setLeft(currNode);
                if (x.getParent() != null) {
                    x.getParent().setRight(x);
                }
                // Defining this tree as the new one in case that 'x' is above t.root.
                if (t.root.getParent() != null) {
                    t.root = t.root.getParent();
                }
                // 't' is the complete tree after joining both, thus we need 'this' to copy t.
                //                                                           Shallow copy is sufficient.
                this.makeCopyOf(t);
            }

        }
        // keys(x,t) > keys()
        else {
            // keys(x,t) > keys() and rankT1 >= rankT2
            // 'this' is higher than 't', thus we'll start from this.root and go down.
            if (this.root.getHeight() >= t.root.getHeight()) {
                currNode = this.root;
                while (currNode.getHeight() > t.root.getHeight()) {
                    currNode = currNode.getRight();
                }
                x.setParent(currNode.getParent());
                currNode.setParent(x);
                t.root.setParent(x);
                x.setLeft(currNode);
                x.setRight(t.root);
                if (x.getParent() != null) {
                    x.getParent().setRight(x);
                }

                // Defining this tree as the new one in case that 'x' is above root,
                //                                      possible if rankT1 == rankT2 for instance.
                if (this.root.getParent() != null) {
                    this.root = this.root.getParent();
                }

            }


            // keys(x,t) > keys() and rankT1 < rankT2
            // 't' is higher than 'this', thus we'll start from t.root and go down.
            else {
                currNode = t.root;
                while (currNode.getHeight() > this.root.getHeight()) {
                    currNode = currNode.getLeft();
                }
                x.setParent(currNode.getParent());
                currNode.setParent(x);
                this.root.setParent(x);
                x.setLeft(this.root);
                x.setRight(currNode);
                if (x.getParent() != null) {
                    x.getParent().setLeft(x);
                }

                // Defining this tree as the new one in case that 'x' is above t.root.
                if (t.root.getParent() != null) {
                    t.root = t.root.getParent();
                }

                // 't' is the complete tree after joining both, thus we need 'this' to copy t.
                //                                                           Shallow copy is sufficient.
                this.makeCopyOf(t);
            }

        }
        // x's height need to be update after join.
        x.updateHeight();

        // Rebalance the tree. InsertRebalnce is sufficient since it covers all the relevant cases.
        insertRebalance(x.getParent(),0);

        return Math.abs(rankT1 - rankT2) + 1;
    }

    // Make this tree a shallow copy of another tree.
    // Takes O(1) time.
    public void makeCopyOf(AVLTree t) {
        this.root = t.root;
        this.min = t.min;
        this.max = t.max;
        this.size = t.size;
        this.pos = t.pos;
    }


    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode {
        public int getKey(); //returns node's key (for virtual node return -1)

        public String getValue(); //returns node's value [info] (for virtual node return null)

        public void setLeft(IAVLNode node); //sets left child

        public IAVLNode getLeft(); //returns left child (if there is no left child return null)

        public void setRight(IAVLNode node); //sets right child

        public IAVLNode getRight(); //returns right child (if there is no right child return null)

        public void setParent(IAVLNode node); //sets parent

        public IAVLNode getParent(); //returns the parent (if there is no parent return null)

        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

        public void setHeight(int height); // sets the height of the node

        public int getHeight(); // Returns the height of the node (-1 for virtual nodes)

        // ---- Additional methods ----
        public void setKey(int k);      // sets key -SAGI

        public void setValue(String s); // sets value -SAGI

        public void demoteNode();        // demote node -SAGI

        public boolean isLeaf();        // return true iff node is a leaf | *for virtual node return false -SAGI

        public void promoteNode();      // promote node -SAGI

        public int[] rankDifference(); // return array, arr[0] = rank difference with left node, arr[1] = rank difference with right node - ARIEL

        public void updateHeight(); // updates node's height, we will use this after rotation - ARIEL

        public int getSize();        // return size -SAGI

        public void updateSize();   // update size -SAGI
    }

    /**
     * public class AVLNode
     * <p>
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in
     * another file.
     * This class can and must be modified.
     * (It must implement IAVLNode)כן
     */
    public class AVLNode implements IAVLNode {
        public int key;
        public String info;
        public IAVLNode parent, left, right; // Consider change to type 'AVLNode' AND\OR delete 'null'
        public int height;
        public int size;

        // Real node constructor
        public AVLNode(int key, String val) {
            this.key = key;
            this.info = val;
            this.left = new AVLNode();
            this.right = new AVLNode();
        }

        // Virtual node constructor
        public AVLNode() {
            this.key = -1;
            this.height = -1;
        }


        public int getKey() {
            return this.key;
        }

        public String getValue() {
            return this.info;
        }

        public void setLeft(IAVLNode node) {
            this.left = node;
        }

        public IAVLNode getLeft() {
            return this.left;
        }

        public void setRight(IAVLNode node) {
            this.right = node;
        }

        public IAVLNode getRight() {
            return this.right;
        }

        public void setParent(IAVLNode node) {
            this.parent = node;
        }

        public IAVLNode getParent() {
            return this.parent;
        }

        // Returns True if this is a non-virtual AVL node
        public boolean isRealNode() {
            return (this.key != -1);
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return this.height;
        }

        public void setKey(int k) {
            this.key = k;
        }

        public void setValue(String s) {
            this.info = s;
        }

        public void demoteNode() {
            this.height--;
        }

        public boolean isLeaf() {
            if (!this.isRealNode()) {
                return false;
            }
            return ((!this.left.isRealNode()) && (!this.right.isRealNode()));
        }

        public void promoteNode() {
            this.height++;
        }

        public int[] rankDifference() {
            int[] result = new int[2];
            result[0] = this.height - this.left.getHeight();
            result[1] = this.height - this.right.getHeight();
            return result;
        }

        public void updateHeight() {
            this.height = Math.max(this.left.getHeight(), this.right.getHeight()) + 1;
        }

        public int getSize() {
            return this.size;
        }

        public void updateSize() {
            if (!this.isRealNode()) {
                this.size = 0;
            }
            this.size = this.left.getSize() + this.right.getSize() + 1;
        }
    }
}
