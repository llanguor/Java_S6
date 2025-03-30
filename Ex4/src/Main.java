import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class Main
{

    final static Set<String> allowedExtensions = Set.of("png", "jpg", "jpeg");
    final static Map<String, BiConsumer<Path, Object[]>> consumerMap = new HashMap<>()
    {{
        put("/s", Main::imageScale);
        put("/n", Main::imageNegative);
        put("/r", Main::imageRemove);
        put("/c", Main::imageCopy);
    }};

    public static void main(String[] args)
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
        var input = args[currentArgIndex];


        var action = consumerMap.getOrDefault(flag, null);
        if(action==null)
        {
            System.out.println("error2");
            return; //todo
        }


        filesTraverse(
                args[0],
                subFlag? Integer.MAX_VALUE : 1,
                action,
                input
        );

    }

    public static void filesTraverse(String path, Integer maxDepth, BiConsumer<Path, Object[]> consumer, Object... args)
    {
        Path dirPath = Paths.get(path);

        try (Stream<Path> files = Files.walk(dirPath, maxDepth)
                .filter(file -> file.toString().matches(".*\\.(png|jpg|jpeg)$")))
        {
            //yeild return :(
            files.forEach(file->consumer.accept(file, args));
        }
        catch (IOException e)
        {
            System.err.println("Error reading directory: " + e.getMessage());
        }
    }

    public static void imageScale(Path path, Object[] args)
    {
        System.out.println("SCALE: "+path);
    }

    public static void imageNegative(Path path, Object[] args)
    {
        System.out.println("NEGATIVE: "+path);
    }

    public static void imageRemove(Path path, Object[] args)
    {
        System.out.println("REMOVE: "+path);
        var res = path.toFile().delete();
    }

    public static void imageCopy(Path path, Object[] args)
    {
        System.out.println("COPY: "+path);
    }
}