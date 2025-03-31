package Packages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class DirectoryContents implements Iterable<Path>
{
    Stream<Path> _contentStream;

    private DirectoryContents(Stream<Path> contentStream)
    {
        _contentStream = contentStream;
    }

    @Override
    public Iterator<Path> iterator()
    {
        return _contentStream.iterator();
    }


    //region Nested builder


    public static final class Builder
    {
        String _path;
        int _maxDepth = Integer.MAX_VALUE;
        String _validateRegex = "^.*$";

        public static Builder create(String path) throws  IllegalArgumentException
        {
            return new Builder(path);
        }

        private Builder(String path) throws  IllegalArgumentException
        {
            if(!Files.exists(Path.of(path)))
                throw new IllegalArgumentException("Folder at this path not found");

            _path = path;
        }

        public Builder setPath(String path) throws  IllegalArgumentException
        {
            if(!Files.exists(Path.of(path)))
                throw new IllegalArgumentException("Folder at this path not found");

            _path = path;
            return this;
        }

        public Builder setMaxDepth(int maxDepth) throws  IllegalArgumentException
        {
            if(maxDepth<1)
                throw new IllegalArgumentException("Max depth can't be lower than 1");

            _maxDepth = maxDepth;
            return this;
        }

        public Builder setValidateRegex(String regex)
        {
            _validateRegex = regex;
            return this;
        }

        public DirectoryContents build() throws IllegalArgumentException
        {
            try
            {
                var contentStream = Files.walk(Path.of(_path), _maxDepth)
                        .filter(file -> file.toString().matches(_validateRegex));
                return new DirectoryContents(contentStream);
            }
            catch (IOException e)
            {
                throw new IllegalArgumentException(String.format("Folder at this path not found %s", e.getMessage()));
            }
            catch (IllegalArgumentException e)
            {
                throw new IllegalArgumentException("Max depth can't be lower than 1");
            }

        }
    }

    //endregion
}
