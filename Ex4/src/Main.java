import Packages.ConsoleInputProcessing;
import Packages.DirectoryContents;
import Packages.FlagToFunctionResolver;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class Main
{

    final static Set<String> allowedExtensions = Set.of("png", "jpg", "jpeg");


    public static void main(String[] args) throws IOException
    {

        if(args.length<2)
        {
            System.out.println("error1");
            return; //todo
        }

        int currentArgIndex = 0;
        var path = args[currentArgIndex++];
        var subFlag = args[currentArgIndex].equals("/sub");
        if(subFlag) ++currentArgIndex;
        var flag = args[currentArgIndex++];
        var input = Arrays.copyOfRange(args, currentArgIndex, args.length);

        try
        {
            ConsoleInputProcessing.process(path, subFlag, flag, input, ".*\\.(png|jpg|jpeg)$");
        }
        catch (IOException e)
        {
            return; //todo
        }
    }
}