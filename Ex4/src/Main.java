import Packages.ConsoleInputProcessing;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        if(args.length<2)
        {
            System.err.println("Error: more than 2 input arguments expected");
            return;
        }

        int currentArgIndex = 0;
        var path = args[currentArgIndex++];
        var subFlag = args[currentArgIndex].equals("/sub");
        if(subFlag)
        {
            ++currentArgIndex;
            if(args.length<3)
                System.err.println("Error: operation flag not found among input arguments");
        }
        var flag = args[currentArgIndex++];
        var input = Arrays.copyOfRange(args, currentArgIndex, args.length);


        try(ExecutorService executor = Executors.newSingleThreadExecutor())
        {
            Future<?> future = executor.submit(() ->
            {
                    ConsoleInputProcessing.process(path, subFlag, flag, input, ".*\\.(png|jpg|jpeg)$");
            });

            //Do something

            future.get(); //Join
            System.out.println("Executed");
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("An error occurred while applying the method to files: " + e.getMessage());
        }
        catch (CancellationException e)
        {
            System.out.println("Error: Operation was canceled");
        }
        catch (ExecutionException | InterruptedException e)
        {
            System.err.println("An error occurred while organizing the multithreading process: " + e.getMessage());
        }
        catch (RuntimeException e)
        {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}