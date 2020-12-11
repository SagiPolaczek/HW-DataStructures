@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef", "RedundantTypeArguments"})
public class ExTester {
    //ActualAVLTree fakeTree;
    ActualAVLTree actualTree;
    AVLTree avlTree;

    int actualOperations;
    int avlOperations;

    int[] valuesTemp;
    int[] values;
    int[] values3;
    int[] values4;


    public ExTester() {

        //fakeTree = null;
        actualTree = null;
        avlTree = null;

        // create array of values between 800-1800
        // like this - 800, 801, 802, 803, 804
        valuesTemp = new int[1000];
        for (int j = 0; j < valuesTemp.length; j++) {
            valuesTemp[j] = 800 + j;
        }

        // mix the values - create a new list of values taken
        // one from the start one from the end, alternately
        // i.e. values[0], values[-1], values[1], values[-2] ...
        values = new int[1000];
        {
            int k = 0;
            for (int j = 0; j < (values.length / 2); j++) {
                values[k] = valuesTemp[j];
                k++;
                values[k] = valuesTemp[valuesTemp.length - 1 - j];
                k++;
            }
        }

        // create custom array of values

        values3 = new int[]{17, 6, 1, 19, 18, 3, 2, 10, 13, 12,
                20, 15, 4, 11, 7, 16, 9, 5, 8, 14, 28};
        values4 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        actualOperations = 0;
        avlOperations = 0;

    }

    boolean caseIsBalanced() {
        avlTree = new AVLTree();
        for (int v : values3) {
            avlTree.insert(v, "" + v);
        }
        AVLTree.IAVLNode root = avlTree.getRoot();
        return isBalanced(root);
    }

    /* Returns true if binary tree with root as root is height-balanced */
    boolean isBalanced(AVLTree.IAVLNode node) {
        int lh; /* for height of left subtree */

        int rh; /* for height of right subtree */
        
        /* If tree is empty then return true */
        if (node == null || node.isRealNode() == false)
            return true;
  
        /* Get the height of left and right sub trees */
        lh = height(node.getLeft());
        rh = height(node.getRight());

        if (Math.abs(lh - rh) <= 1
                && isBalanced(node.getLeft())
                && isBalanced(node.getRight()))
            return true;
  
        /* If we reach here then tree is not height-balanced */
        return false;
    }

    /* UTILITY FUNCTIONS TO TEST isBalanced() FUNCTION */
    /*  The function Compute the "height" of a tree. Height is the
        number of nodes along the longest path from the root node
        down to the farthest leaf node.*/
    private int height(AVLTree.IAVLNode node) {
        /* base case tree is empty */
        if (node == null)
            return 0;
  
        /* If tree is not empty then height = 1 + max of left
         height and right heights */
        return 1 + Math.max(height(node.getLeft()), height(node.getRight()));
    }


    private boolean caseIdempotent() {
        int n = 0;
        avlTree = new AVLTree();
        for (int aValues3 : values3) {
            avlOperations += avlTree.insert(aValues3, "" + aValues3);
            int cont;
            cont = avlTree.insert(aValues3, "" + (-1));
            if (cont != -1) {
                System.out.println("Its the second cont insert");
                System.out.println("Cont==" + cont);
                return false;
            }
        }
        if (!avlTree.max().equals("28") ||
                !avlTree.min().equals("1")) {
            // if the maximum / minimum are wrong
            return false;
        }

        for (int val1 : values3) {
            if ((TesterUtils.intValue(avlTree.search(val1)) != val1)) {
                System.out.println("Its the TesterUtils.search");
                return false;
            }
        }
        for (int val2 : values3) {
            if (!(TesterUtils.intValue(avlTree.search(val2)) == val2)) {
                System.out.println("Its val2 loop");
                return false;
            }
        }
        return true;
    }

    private boolean caseDelAll() {
        avlTree = new AVLTree();
        for (int aValues4: values4){
            avlTree.insert(aValues4, ""+aValues4);
        }
        int n = 0;
        for (int aValues4 : values4) {
            avlOperations += avlTree.delete(values4[aValues4 - 1]);
            if (avlTree.size() > 0) {
                // while avlTree is not empty, checking the min & max values
                if ((!avlTree.max().equals(avlTree.max())) ||
                        (!avlTree.min().equals(avlTree.min()))) {
                    n++;
                }
            } else {
                // if all items were deleted from avlTree, check if RBTree is empty as well
                if (!avlTree.empty()) {
                    n++;
                }
            }
        }
        for (int val : values4) {
            // checking that all the values that were supposed to be deleted are not in the RBTree
            if (!(avlTree.search(val) == null)) {
                n++;
            }
        }
        return (n == 0);
    }

    private boolean caseInsertRand() {
        int n = 0;
        avlTree = new AVLTree();
        for (int aValues4 : values4) {
            avlOperations += avlTree.insert(aValues4, "" + aValues4);
        }
        if (!avlTree.max().equals("10")) {
            // if the maximum / minimum are wrong
            return false;
        }

        for (int val : values4) {
            if (!(TesterUtils.intValue(avlTree.search(val)) == val)) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        // initialize tests success array to false
        final SuccessStatus[] success = new SuccessStatus[16];

        final Tests first_tester = new Tests();
        final ExTester second_tester = new ExTester();

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[0] = first_tester.empty() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[0] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 0);


        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[1] = first_tester.search() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[1] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 1);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[2] = first_tester.insert_and_size() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[2] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 2);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[3] = first_tester.delete() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            System.out.println(e.toString());
                            System.out.println(e.getCause());
                            success[3] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 3);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[4] = first_tester.min() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[4] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 4);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[5] = first_tester.max() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[5] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 5);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[6] = first_tester.min_equals_max() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[6] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 6);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[7] = first_tester.keysToArray() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[7] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 7);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[8] = first_tester.size() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[8] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 8);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[9] = first_tester.split() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[9] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 9);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[10] = first_tester.select() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[10] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 10);


        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[11] = first_tester.testRemove() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[11] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 11);


        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[12] = second_tester.caseIsBalanced() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[12] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 12);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[13] = second_tester.caseInsertRand() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[13] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 13);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[14] = second_tester.caseIdempotent() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[14] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 14);

        runWithInterrupt(success,
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            success[15] = second_tester.caseDelAll() ? SuccessStatus.PASS : SuccessStatus.FAIL;
                        } catch (Throwable e) {
                            success[15] = SuccessStatus.EXCEPTION;
                        }
                    }
                }), 15);

        int final_score = TesterUtils.printStatus(success, second_tester.actualOperations, second_tester.avlOperations);
		System.out.println("The final score is: " + final_score);
        System.exit(final_score);

    }

    private static void runWithInterrupt(SuccessStatus[] success, Thread thread, int idx) {
        thread.start();

        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!thread.isAlive())
                break;
        }
        if (thread.isAlive()) {
            thread.stop();
            success[idx] = SuccessStatus.EXCEPTION;
        }
    }
}