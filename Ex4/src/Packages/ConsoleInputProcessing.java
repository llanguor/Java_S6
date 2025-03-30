package Packages;

import java.io.IOException;

public final class ConsoleInputProcessing
{
    public static void process(String path, Boolean recursiveTraverse, String flag, Object[] input, String regexMask)
            throws IOException
    {
        var function = FlagToFunctionResolver.getFunction(flag);
        if(function==null)
        {
            System.out.println("error2");
            return; //todo
        }

        var builder
                = DirectoryContents
                .Builder
                .create(path)
                .setValidateRegex(regexMask);
        if(!recursiveTraverse) builder.setMaxDepth(1);
        var directoryContents = builder.build();

        for(var p : directoryContents)
        {
            function.accept(p, input);
        }
    }
}
