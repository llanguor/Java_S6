import java.util.*;

public class Main
{

    public static void main(String[] args)
    {
        //Число не всегда максимальное.
        //Иначе алгоритм запирал бы сам себя
        //А по условию нужно обязательно обойти ВСЕ ячейки
        
        int[][] matrix = Matrix.randomizeMatrix();
        Matrix.print(matrix);
        System.out.println("Max number: " + Matrix.findMaxNumber(matrix, 3, 3));

    }

    public static final class Matrix
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

        public static int[][] randomizeMatrix()
        {

            List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
            Collections.shuffle(numbers);

            int[][] matrix = new int[3][3];
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    matrix[i][j] = numbers.get(i * 3 + j);
                }
            }
            return matrix;
        }


        private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        public static int findMaxNumber(int[][] matrix, int rows, int cols) {

            for (int i = 0; i < rows; ++i)
            {
                for (int j = 0; j < cols; ++j)
                {
                    if (matrix[i][j] == 9)
                    {
                        return depthFirstSearch(
                                matrix,
                                i,
                                j,
                                new HashSet<>(),
                                matrix[i][j],
                                0);
                    }
                }
            }

            throw new IllegalArgumentException("The input array is invalid. It must contain digits from 1 to 9");
        }

        private static int depthFirstSearch(int[][] matrix, int x, int y, Set<String> visited, int number, int maxNumber)
        {
            maxNumber = Math.max(maxNumber, number);
            visited.add(x + "," + y);

            //Перебор направлений
            for (int[] dir : DIRECTIONS)
            {
                //Получаем новые индексы.
                int nx = x + dir[0], ny = y + dir[1];

                //Переходим к след направлению если какой-то индекс вне массива или ячейка уже посещена
                if(nx<0 || nx>2 || ny<0 || ny>2 || visited.contains(nx + "," + ny))
                    continue;

                //Продолжаем рекурсивно идти по ячейкам, пока не обойдём все в каждом рекурсивном вызове
                maxNumber = depthFirstSearch(
                        matrix,
                        nx,
                        ny,
                        new HashSet<>(visited),
                        number*10 + matrix[nx][ny],
                        maxNumber);

            }

            return maxNumber;
        }
    }
}