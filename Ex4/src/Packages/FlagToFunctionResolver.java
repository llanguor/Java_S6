package Packages;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.nio.file.*;


public final class FlagToFunctionResolver
{
    //region Fields

    final static Map<String, BiConsumer<Path, Object[]>> _consumerMap = new HashMap<>()
    {{
        put("/s", FlagToFunctionResolver::imageScale);
        put("/n", FlagToFunctionResolver::imageNegative);
        put("/r", FlagToFunctionResolver::imageRemove);
        put("/c", FlagToFunctionResolver::imageCopy);
    }};

    //endregion

    //region Methods get

    public static BiConsumer<Path, Object[]> getFunction(String flag)
    {
       return _consumerMap.getOrDefault(flag, null);
    }

    //endregion

    //region Methods for images

    public static void imageScale(Path path, Object... args)
    {
        if(!Files.exists(path))
            throw new IllegalArgumentException(
                    "File from path is not exists");

        if(args.length<1)
            throw new IllegalArgumentException(
                    "The scale function requires an input argument containing the path to the output directory");

        if(args[0]==null)
            throw new IllegalArgumentException("Input scale argument can't be null");

        double stretchFactor = Double.parseDouble(args[0].toString());

        if(stretchFactor<=0)
            throw new IllegalArgumentException("Input scale argument can't be less then zero");

        BufferedImage outputImage;
        try
        {
            outputImage = ImageIO.read(path.toFile());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Can't read image", e);
        }

        int width = (int) (outputImage.getWidth() * stretchFactor);
        int height = (int) (outputImage.getHeight() * stretchFactor);
        Image stretchedImage = outputImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


        Graphics2D g2d = outputImage.createGraphics();
        try
        {
            g2d.drawImage(stretchedImage, 0, 0, null);
        }
        finally
        {
            g2d.dispose();
        }

        saveImageToPath(outputImage, path);

    }

    public static void imageNegative(Path path, Object... args) throws RuntimeException, IllegalArgumentException
    {
        BufferedImage inputFile;
        try
        {
            inputFile = ImageIO.read(path.toFile());
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException(
                    "File from path is not exists", e);
        }
        catch (IOException e)
        {
            throw new RuntimeException("File from input path can't be read", e);
        }

        for (int x = 0; x < inputFile.getWidth(); x++)
        {
            for (int y = 0; y < inputFile.getHeight(); y++) {
                int rgba = inputFile.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue());
                inputFile.setRGB(x, y, col.getRGB());
            }
        }

        saveImageToPath(inputFile, path);
    }

    public static void imageRemove(Path path, Object... args)
    {
        if(Files.exists(path))
            path.toFile().delete();
    }

    public static void imageCopy(Path inputPath, Object... args)
            throws
            IllegalArgumentException,
            InvalidPathException,
            RuntimeException

    {
        if(!Files.exists(inputPath))
            throw new IllegalArgumentException(
                    "File from path is not exists");

        if(args.length<1)
            throw new IllegalArgumentException(
                    "The copy function requires an input argument containing the path to the output directory");

        Path outputDir = Path.of(args[0].toString());

        if(args[0]==null)
            throw new IllegalArgumentException("Input copy path argument can't be null");

        if(!Files.exists(outputDir))
            throw new IllegalArgumentException(
                    "Output directory doesn't exists");

         try
        {
            String outputName = inputPath.getFileName().toString();
            Path outputPath = Path.of(String.format("%s\\%s", outputDir.toString(), outputName));
            Files.copy(inputPath, outputPath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Can't copy image", e);
        }
    }

    //endregion

    //region Methods private

    private static void saveImageToPath(BufferedImage image, Path path)
    {
        try
        {
            if(image==null)
                throw new IllegalArgumentException("Image for saving can't equals null");

            String extension = path.toString().substring(path.toString().lastIndexOf('.') + 1);
            File outputFile = path.toFile();
            ImageIO.write(image, extension, outputFile);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Can't save image", e);
        }
    }

    //endregion
}
