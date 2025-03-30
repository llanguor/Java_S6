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

        public static Builder create(String path)
        {
            return new Builder(path);
        }

        private Builder(String path)
        {
            _path = path;
        }

        public Builder setPath(String path)
        {
            _path = path;
            return this;
        }

        public Builder setMaxDepth(int maxDepth)
        {
            _maxDepth = maxDepth;
            return this;
        }

        public Builder setValidateRegex(String regex)
        {
            _validateRegex = regex;
            return this;
        }

        public DirectoryContents build() throws IOException
        {
            var contentStream = Files.walk(Path.of(_path), _maxDepth)
                    .filter(file -> file.toString().matches(_validateRegex));

            return new DirectoryContents(contentStream);
        }
    }

    //endregion
}
