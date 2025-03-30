import packages.Polynome;

public class Main
{
    public static void main(String[] args) throws CloneNotSupportedException
    {
        var p1 = new Polynome(3,3,3,3);
        var p2 = new Polynome(5,5,5,5);


        var res = Polynome.divide(p1, p2);

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(res.first);
        System.out.println(res.second);
        //System.out.println(result._result);
       // System.out.println(result._quotient);

        System.out.println(p1.compareTo(p2));


    }
}