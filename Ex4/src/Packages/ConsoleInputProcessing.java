package Packages;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.concurrent.ExecutionException;

public final class ConsoleInputProcessing
{
    public static void process(String path, Boolean recursiveTraverse, String flag, Object[] input, String regexMask)
            throws IllegalArgumentException, InvalidPathException, RuntimeException
    {
        var function = FlagToFunctionResolver.getFunction(flag);
        if(function==null)
        {
            throw new IllegalArgumentException(String.format("Error: No functions found for flag %s", flag));
        }

        var builder
                = DirectoryContents
                .Builder
                .create(path)
                .setValidateRegex(regexMask);

        if(!recursiveTraverse)
            builder.setMaxDepth(1);

        for(var p : builder.build())
        {
            function.accept(p, input);
        }
    }
}
