import packages.Polynomial;

import java.util.HashMap;

public class Main
{
    public static void main(String[] args) throws CloneNotSupportedException
    {
        var p1 = new Polynomial(3,3,3,3);
        var p2 = new Polynomial(5,5,5,5);

        var res = Polynomial.divide(p1, p2);

        System.out.printf("First polynomial: %s\n", p1);
        System.out.printf("Second polynomial: %s\n", p2);
        System.out.printf("Divide result: %s\n", res);

        //System.out.println(result._result);
       // System.out.println(result._quotient);

        System.out.printf("Compare result: %s", p1.compareTo(p2));
    }
}