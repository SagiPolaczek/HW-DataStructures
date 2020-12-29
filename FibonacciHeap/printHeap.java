package Heap;
// By Elad Feldman 19/12/20

import java.util.ArrayList;
import java.util.Collections;

public class printHeap {
    public static void main(String[] args)  {
        FibonacciHeap h1 = new FibonacciHeap();
         insertN(h1,30);
        printHeapFib(h1);
         h1.deleteMin();
        printHeapFib(h1);

    }





    public static  void printHeapFib(FibonacciHeap heap) {
        System.out.println("-----------------------------------------------");
        String[] list = new String[heap.size()*10];
        for (int i = 0; i < heap.size(); i++) {
            list[i] = "";
        }
        Integer level = 0;
        printHeapFib(heap.findMin(), list, level);
        for (int i = 0; i < heap.size(); i++) {
            if (list[i]!="")
                System.out.println(list[i]);
        }
        System.out.println("-----------------------------------------------");
    }

    public  static void printHeapFib(HeapNode node, String[] list, Integer level) {

        list[level] += "(";
        if (node == null) {
            list[level] += ")";
            return;
        } else {
            HeapNode temp = node;
            do {
                list[level] += temp.getKey();
                HeapNode k = temp.getChild();
                printHeapFib(k, list, level + 1);

                list[level] +=getChain("=");
                temp = temp.getNextNode();
            } while (temp != node);
            list[level] += ")";
        }


    }
    private static String getChain(String link){
        int count =2;;
        String str="";
        if (count==0){
            return "";
        }
        for (int i=0;i<count/3+1;i++){
            str+=link;
        }
        return str+">";
    }
    public static void insertN(FibonacciHeap h1,int n) {
        insertN( h1,1, n);

    }


    public static void insertN(FibonacciHeap h1,int start, int amount) {
        ArrayList<Integer> mylist = new ArrayList();
        for(int i = start; i <  start+amount; i++) {
            mylist.add(i);
        }
        Collections.shuffle(mylist);
        for (int i = 0; i < amount; i++) {
            h1.insert(mylist.get(i));

        }
    }

}

