package Packages;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public final class FlagToFunctionResolver
{
    final static Map<String, BiConsumer<Path, Object[]>> _consumerMap = new HashMap<>()
    {{
        put("/s", FlagToFunctionResolver::imageScale);
        put("/n", FlagToFunctionResolver::imageNegative);
        put("/r", FlagToFunctionResolver::imageRemove);
        put("/c", FlagToFunctionResolver::imageCopy);
    }};

    public static BiConsumer<Path, Object[]> getFunction(String flag)
    {
       return _consumerMap.getOrDefault(flag, null);
    }

    public static void imageScale(Path path, Object... args)
    {
        System.out.println("SCALE: "+path);
    }

    public static void imageNegative(Path path, Object... args)
    {
        System.out.println("NEGATIVE: "+path);
    }

    public static void imageRemove(Path path, Object... args)
    {
        System.out.println("REMOVE: "+path);
        var res = path.toFile().delete();
    }

    public static void imageCopy(Path path, Object... args)
    {
        System.out.println("COPY: "+path);
    }

}
