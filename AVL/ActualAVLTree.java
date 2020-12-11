import java.util.ArrayList;
import java.util.List;

/**
 *
 * AVLTree
 *
 * An implementation of a WAVL Tree with distinct integer keys and info
 *
 * An RB_Tree is composed of a sentinel RB_Tree_Node. If the tree is not empty,
 * then the root is the left child of the sentinel
 *
 */

public class ActualAVLTree {
	public ActualAVLTree() {

	}

	public interface IAVLNode extends AVLTree.IAVLNode{
        public int getKey(); //returns node's key (for virtuval node return -1)
        public String getValue(); //returns node's value [info] (for virtuval node return null)
        public void setLeft(AVLTree.AVLNode node); //sets left child
        public AVLTree.AVLNode getLeft(); //returns left child (if there is no left child return null)
        public void setRight(AVLTree.AVLNode node); //sets right child
        public AVLTree.AVLNode getRight(); //returns right child (if there is no right child return null)
        public void setParent(AVLTree.AVLNode node); //sets parent
        public AVLTree.AVLNode getParent(); //returns the parent (if there is no parent return null)
        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
        public void setSubtreeSize(int size); // sets the number of real nodes in this node's subtree
        public int getSubtreeSize(); // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))
        public void setHeight(int height); // sets the height of the node
        public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
	}
}
