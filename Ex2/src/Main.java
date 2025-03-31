import packages.Polynomial;

import java.util.HashMap;

public class Main
{
    public static void main(String[] args) throws CloneNotSupportedException
    {
        //Input from lowest to highest degrees

        var p1 = new Polynomial(8,8,4,4);
        var p2 = new Polynomial(2,2,2,2);

        System.out.print("Polynomials:\n");
        System.out.println(p1);
        System.out.println(p2);
        System.out.println();
        System.out.printf("Compare result: %s\n", p1.compareTo(p2));
        System.out.printf("Addition result: %s\n", Polynomial.addition(p1,p2));
        System.out.printf("Subtract result: %s\n", Polynomial.subtract(p1,p2));
        System.out.printf("Multiply result: %s\n", Polynomial.multiply(p1, p2));
        System.out.printf("Divide result: %s\n", Polynomial.divide(p1, p2));

    }
}