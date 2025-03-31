package Packages;
import java.util.*;

public final class Matrix
{
    public static void print(int[][] matrix)
    {
        for (int[] row : matrix)
        {
            for (int num : row)
            {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    public static int[][] randomize(int rows, int cols)
    {
        if(rows<1 || cols<1)
            throw new IllegalArgumentException("Incorrect size of array");
        
        List<Integer> numbers = new ArrayList<>();
        for(int i=0; i<rows*cols; ++i)
        {
            numbers.add(i+1);
        }
        Collections.shuffle(numbers);

        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                matrix[i][j] = numbers.get(i * rows + j);
            }
        }
        return matrix;
    }

    public static int[][] initFromString(String input, int rows, int cols)
    {
        if(rows<1 || cols<1)
            throw new IllegalArgumentException("Incorrect size of array");

        if(!input.matches("\\d+"))
            throw new IllegalArgumentException("Incorrect input. Values must be numbers");

        if (input.length() != rows * cols)
            throw new IllegalArgumentException("Incorrect length of input data");

        int[][] matrix = new int[rows][cols];
        for (int i = 0, index = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                matrix[i][j] = Character.getNumericValue(
                        input.charAt(index++));
            }
        }

        return matrix;
    }

    public static final class MaxNumberFinder
    {
        private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        public static int find(int[][] matrix)
        {
            //Начинаем с 1 из 9 ячеек и выбираем большее число

            int rows = matrix.length;
            int cols = matrix[0].length;
            int maxNumber = 0;

            for (int i = 0; i < rows; ++i)
            {
                for (int j = 0; j < cols; ++j)
                {
                    //Не можем начинать с ячеек, где сумма индексов нечетно,
                    //если и число стобцов и число строк нечетно
                    //Упрёмся в стенку, тк не хватит "ширины"

                    if(rows%2==1 && cols%2==1 && (i+j)%2==1)
                        continue;

                    int tempNumber = depthFirstSearch(
                            matrix,
                            i,
                            j,
                            new HashSet<>(),
                            matrix[i][j],
                            0);

                    maxNumber = Math.max(tempNumber, maxNumber);
                }
            }

            return maxNumber;
        }

        private static int depthFirstSearch(int[][] matrix, int i, int j, Set<Integer> visited, int number, int maxNumber)
        {
            maxNumber = Math.max(maxNumber, number);
            visited.add(i*10+j);

            for (int[] dir : DIRECTIONS)
            {
                int ni = i + dir[0], nj = j + dir[1];
                if (ni < 0 || ni > 2 || nj < 0 || nj > 2 || visited.contains(ni*10+nj))
                    continue;

                maxNumber = depthFirstSearch(
                        matrix,
                        ni,
                        nj,
                        new HashSet<>(visited),
                        number*10 + matrix[ni][nj],
                        maxNumber);

            }
            return maxNumber;
        }
    }
}
