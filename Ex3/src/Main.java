import Packages.Matrix;
import java.util.*;

public class Main
{

    public static void main(String[] args)
    {
        String input = null;
        Scanner in = new Scanner(System.in);
        int[][] matrix = {};

        while (true)
        {
            System.out.print("\nEnter numbers in array (r - random generation, e - exit)\n");
            input = in.nextLine();

            switch (input)
            {
                case "e":
                    return;

                case "r":
                    matrix = Matrix.randomize(3,3);
                    break;

                default:
                    try
                    {
                        matrix = Matrix.initFromString(input, 3, 3);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(e.getMessage());
                        continue;
                    }
            }

            Matrix.print(matrix);
            System.out.println("Max number: " + Matrix.MaxNumberFinder.find(matrix));
        }
    }
}