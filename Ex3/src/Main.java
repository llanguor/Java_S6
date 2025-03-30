import java.util.*;

public class Main
{
    public static void main(String[] args)
    {

        HashMap<Integer,Integer> hashMap = new HashMap<>();
        hashMap.put(2,2);
        hashMap.put(1,1);

        HashMap<Integer,Integer> hashMap2 = new HashMap<>();
        hashMap2.put(5,5);
        hashMap2.put(4,4);

        List<Integer> integerSet = new ArrayList<>(hashMap.keySet());
        integerSet.addAll(hashMap2.keySet());
        integerSet.sort(Collections.reverseOrder());

        for(var s: integerSet)
        {
            System.out.print(s);
        }
    }
}