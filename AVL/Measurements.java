import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Measurements {

    public static void main(String[] args){
        int n = 10;
        List<Integer> randomList = randomArrayList(n);


    }

    public static List<Integer> randomArrayList(int n){
        List<Integer> result = sortedArrayList(n);
        Collections.shuffle(result);
        return result;
    }

    public static List<Integer> sortedArrayList(int n){
        List<Integer> result = new ArrayList<>();
        for (int i=0; i<n; i++){
            result.add(i+1); // numbers in array are from 1 to n.
        }
        return result;
    }
}
