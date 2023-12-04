package aero.minova.cas.resources;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ResourcePath implements Path {
    private final List<String> path;
    private final FileSystem fileSystem;

    public ResourcePath(List<String> pathArg, FileSystem fileSystemArg) {
        path = new ArrayList<>(pathArg.size());
        for (final String element : pathArg) {
            if (!element.equals("/") && !element.isEmpty() && !element.equals(".")) {
                path.add(element);
            }
        }
        fileSystem = fileSystemArg;
    }

    @Override
    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public String resourcePath() {
        if (path.isEmpty()) {
            return "./";
        }
        return path.stream().reduce((a, b) -> a + "/" + b).orElseThrow();
    }

    @Override
    public boolean isAbsolute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path getRoot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path getFileName() {
        final var newPath = new ArrayList<String>();
        newPath.add(path.get(path.size() - 1));
        return new ResourcePath(newPath, this.fileSystem);
    }

    @Override
    public Path getParent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNameCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path getName(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean startsWith(Path other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean endsWith(Path other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path normalize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path resolve(Path other) {
        if (other instanceof ResourcePath) {
            final var otherResource = (ResourcePath) other;
            final var newPath = new ArrayList<>(path);
            newPath.addAll(((ResourcePath) other).path);
            return new ResourcePath(newPath, fileSystem);
        } else {
            throw new UnsupportedOperationException("Only " + getClass().getName() + " are supported and not " + other.getClass().getName() + ".");
        }
    }

    @Override
    public Path relativize(Path other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI toUri() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path toAbsolutePath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(Path other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return resourcePath();
    }
}
