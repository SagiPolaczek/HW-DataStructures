public class MeasurementsHeap {

    public static void main(String[]args) {

        // Part A
        System.out.println("Sequence 1:");
        // sequence1: k = 9
        FibonacciHeap heap0 = new FibonacciHeap();
        long startTime = System.nanoTime(), stopTime = System.nanoTime();
        startTime = System.nanoTime();
//        sequence1(heap0, 9);
//        sequence1(heap0, 9);
//        sequence1(heap0, 9);
        stopTime = System.nanoTime();
        // set links & cuts to zero
        FibonacciHeap.totalLinks = 0;
        FibonacciHeap.totalCuts = 0;

        // sequence1: k = 10

        FibonacciHeap heap1 = new FibonacciHeap();

        startTime = System.nanoTime();
        sequence1(heap1, 10);
        stopTime = System.nanoTime();

        System.out.println("Results for k = 10:");
        System.out.println("Run-Time = "+(stopTime-startTime));
        System.out.println("TotalLinks = " + FibonacciHeap.totalLinks());
        System.out.println("TotalCuts = " + FibonacciHeap.totalCuts());
        System.out.println("Potential = " + heap1.potential());

        // set links & cuts to zero
        FibonacciHeap.totalLinks = 0;
        FibonacciHeap.totalCuts = 0;

        // sequence1: k = 11

        FibonacciHeap heap2 = new FibonacciHeap();

        startTime = System.nanoTime();
        sequence1(heap2, 11);
        stopTime = System.nanoTime();

        System.out.println("Results for k = 11:");
        System.out.println("Run-Time = "+(stopTime-startTime));
        System.out.println("TotalLinks = " + FibonacciHeap.totalLinks());
        System.out.println("TotalCuts = " + FibonacciHeap.totalCuts());
        System.out.println("Potential = " + heap2.potential());

        // set links & cuts to zero
        FibonacciHeap.totalLinks = 0;
        FibonacciHeap.totalCuts = 0;

        // sequence1: k = 12
        FibonacciHeap heap3 = new FibonacciHeap();

        startTime = System.nanoTime();
        sequence1(heap3, 12);
        stopTime = System.nanoTime();

        System.out.println("Results for k = 12:");
        System.out.println("Run-Time = "+(stopTime-startTime));
        System.out.println("TotalLinks = " + FibonacciHeap.totalLinks());
        System.out.println("TotalCuts = " + FibonacciHeap.totalCuts());
        System.out.println("Potential = " + heap3.potential());

    }

    public static void sequence1(FibonacciHeap heap, int k) {
        int m = (int) Math.pow(2,k);
        FibonacciHeap.HeapNode [] nodes = new FibonacciHeap.HeapNode[m+1];
        for(int i=m ; i>=0 ; i--) {
            heap.insert(i);
            nodes[i] = heap.head;
        }
        heap.deleteMin();
        int sum;
        for(int i=0 ; i<k ; i++) {
            sum = 0;
            for(int j=1 ; j<=i ; j++) {
                sum += (m * Math.pow(0.5, j));
            }
            sum += 2;
            System.out.println("sum= "+sum);
            heap.decreaseKey(nodes[sum], m+1);
        }
        heap.decreaseKey(nodes[m-1] , m+1);
    }


}
