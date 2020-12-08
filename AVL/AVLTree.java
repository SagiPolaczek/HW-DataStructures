/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {

	/** pos - indicates index in array, while implementing keysToArray() & infoToArray() methods
	 *  size - indicates the nummber of AVLnodes in our tree
	 *  min, max - indicates the AVLnodes in our tree with the min,max keys
	 */

	IAVLNode root, min, max;
	int size, pos;

  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
    return (size == 0);
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)
  {
	  IAVLNode curr = this.root;
	  while(curr != null) {
		  int currKey = curr.getKey();
		  if(currKey == k) {
			  return curr.getValue();
		  }
		  if(currKey < k) {
			  curr = curr.getRight();
		  }
		  else {
			  curr = curr.getLeft();
		  }
	  }
	  return null;
  }

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k already exists in the tree.
   */
  public int insert(int k, String i) {
	  IAVLNode node = new AVLNode(k,i);
	  if(root == null) {
		  root = node;
		  min = node;
		  max = node;
		  return 0;
	  }
	  if(search(k) != null) { // if k exist, return -1
		  return -1;
	  }
	  // update min, max if needed
	  if(k < min.getKey()) {
		  min = node;
	  }
	  if(k > max.getKey()) {
		  max = node;
	  }
	  // insert node to the tree
	  // update & rotate

	  return 42;	// to be replaced by student code
  }

	/* MISSING in rotations: ranks update */

	private IAVLNode rotateLeft(IAVLNode root) {
		IAVLNode parent = root.getParent();
		IAVLNode rightNode = root.getRight();
		root.setRight(rightNode.getLeft());
		if(root.getRight() != null) {
			rightNode.getLeft().setParent(root);
		}
		rightNode.setLeft(root);
		root.setParent(rightNode);
		if(parent != null) {
			if(parent.getLeft() == root) {
				parent.setLeft(rightNode);
			}
			else {
				parent.setRight(rightNode);
			}
		}
		return rightNode;
	}

	private IAVLNode rotateRight(IAVLNode root) {
		IAVLNode parent = root.getParent();
		IAVLNode leftNode = root.getLeft();
		root.setLeft(leftNode.getRight());
		if(root.getLeft() != null) {
			leftNode.getRight().setParent(root);
		}
		leftNode.setRight(root);
		root.setParent(leftNode);
		leftNode.setParent(parent);
		if(parent != null) {
			if(parent.getLeft() == root) {
				parent.setLeft(leftNode);
			}
			else {
				parent.setRight(leftNode);
			}
		}
		return leftNode;
	}


  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   return 42;	// to be replaced by student code
   }

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   return this.min.getValue();
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   return this.max.getValue();
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  if(size == 0) {
		  return null;
	  }
	  int[] result = new int[size];
	  IAVLNode runner = this.root;
	  keysInorderTraversal(result, runner);
	  this.pos = 0;
	  return result;
  }

	/**
	 * private void keysInorderTraversal(String[] arr, IAVLNode root)
	 *
	 * recursive function which insert the key value of each node in our AVLTree into an existing array of integers,
	 * using inorder traversal.
	 */

	private void keysInorderTraversal(int[]arr, IAVLNode root) {
		if(root != null) {
			keysInorderTraversal(arr, root.getLeft());
			arr[this.pos] = root.getKey();
			this.pos++;
			keysInorderTraversal(arr, root.getRight());
		}
	}

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  String[] result = new String[size];
	  IAVLNode runner = this.root;
	  infoInorderTraversal(result, runner);
	  this.pos = 0;
	  return result;
  }

	/**
	 * private void infoInorderTraversal(String[] arr, IAVLNode root)
	 *
	 * recursive function which insert the info value of each node in our AVLTree into an existing array of Strings,
	 * using inorder traversal.
	 */

	private void infoInorderTraversal(String[] arr, IAVLNode root) {
		if(root != null) {
			infoInorderTraversal(arr, root.getLeft());
			arr[this.pos] = root.getValue();
			this.pos++;
			infoInorderTraversal(arr, root.getRight());
		}
	}

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   return this.size;
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot()
   {
	   return this.root;
   }
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x. 
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
    * postcondition: none
    */   
   public AVLTree[] split(int x)
   {
	   return null; 
   }
   /**
    * public join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree. 	
    * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	  * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
    * postcondition: none
    */   
   public int join(IAVLNode x, AVLTree t)
   {
	   return 0; 
   }

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
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
	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)כן
   */
  public class AVLNode implements IAVLNode{
  		public int key;
  		public String info;
  		public IAVLNode parent = null; // Consider change to type 'AVLNode' AND\OR delete 'null'
  		public IAVLNode left = null;   // ^^
  		public IAVLNode right = null;  // ^^
		public int height;
		public int size;

		// Real node constructor
		public AVLNode(int key, String val) {
			this.key = key;
			this.info = val;
		}

		// Virtual node constructor
		public AVLNode (){
			this.key = -1;
			this.info = null;
			this.height = -1;
		}


		public int getKey()
		{
			return this.key;
		}
		public String getValue()
		{
			return this.info;
		}
		public void setLeft(IAVLNode node)
		{
			this.left = node;
		}
		public IAVLNode getLeft()
		{
			return this.left;

		}
		public void setRight(IAVLNode node)
		{
			this.right = node;
		}
		public IAVLNode getRight()
		{
			return this.right;
		}
		public void setParent(IAVLNode node)
		{
			this.parent = node;
		}
		public IAVLNode getParent()
		{
			return this.parent;
		}
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			return (this.key != -1);
		}
    public void setHeight(int height)
    {
      this.height = height;
    }
    public int getHeight()
    {
      return this.height;
    }
  }

}

