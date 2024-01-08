package aero.minova.cas.resources;

import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;

import static java.util.Arrays.asList;

public class ResourceFileSystemProvider extends FileSystemProvider {
    public static ResourceFileSystemProvider FILE_SYSTEM_PROVIDER = new ResourceFileSystemProvider(ResourceFileSystemProvider.class.getClassLoader());
    public static final String SCHEME = "aero-minova-classloader-resource";
    private final ClassLoader classLoader;
    private final List<String> resourceList = new ArrayList<>();

    public ResourceFileSystemProvider(ClassLoader classLoaderArg) {
        classLoader = classLoaderArg;
        try {
            final var deployedResources = new String(getClass().getResourceAsStream("/aero.minova.app.resources/deployed.resources.txt").readAllBytes());
            for (final var resourceListPath : deployedResources.split("\n")) {
                if (resourceListPath.isBlank()) {
                    continue;
                }
                final var resourceListStr = new String(getClass().getResourceAsStream(resourceListPath).readAllBytes());
                for (final var resource : resourceListStr.split("\n")) {
                    if (resource.isBlank()) {
                        continue;
                    }
                    resourceList.add(resource);
                }
            }
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }

    @Override
    public String getScheme() {
        return SCHEME;
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        return new ResourceFileSystem(classLoader);
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        return new ResourceFileSystem(classLoader);
    }

    @Override
    public Path getPath(URI uri) {
        if (!uri.getScheme().equals(SCHEME)) {
            throw new IllegalArgumentException("Expecting `" + SCHEME + "` scheme, but got `" + uri.getScheme() + "` instead.");
        }
        new ResourcePath(asList(uri.getPath().split("/")), null);
        throw new UnsupportedOperationException();
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        if (path instanceof ResourcePath) {
            final var resourcePath = (ResourcePath) path;
            final var loadedResource = classLoader.getResourceAsStream(resourcePath.resourcePath());
            if (loadedResource == null) {
                throw new FileNotFoundException(resourcePath.resourcePath());
            }
            return new SeekableInMemoryByteChannel(loadedResource.readAllBytes());
        } else {
            throw new UnsupportedOperationException("Only " + getClass().getName() + " are supported and not " + path.getClass().getName() + ".");
        }
    }

    public List<Path> walk(Path rawPath) {
        final ResourcePath path;
        if (rawPath instanceof ResourcePath) {
            path = (ResourcePath) rawPath;
        } else {
            throw new IllegalArgumentException();
        }
        try {
            final var pathStr = path.resourcePath() + "/";
            final List<Path> matchingResources = new ArrayList<>();
            for (final var resource : resourceList) {
                if (resource.startsWith("/" + pathStr)) {
                    matchingResources.add(getFileSystem(null).getPath(resource));
                }
            }
            return matchingResources;
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }

    private List<Path> walk(ResourcePath path, int maxDepth) {
        try {
            final var pathStr = path.resourcePath() + "/";
            final List<Path> matchingResources = new ArrayList<>();
            for (final var resource : resourceList) {
                if (resource.startsWith("/" + pathStr)) {
                    if (resource.endsWith("/") && resource.substring(pathStr.length()).split("/").length < 3) {
                        matchingResources.add(getFileSystem(null).getPath(resource));
                    } else if (resource.substring(pathStr.length()).split("/").length < 2) {
                        matchingResources.add(getFileSystem(null).getPath(resource));
                    }
                }
            }
            return matchingResources;
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        if (dir instanceof ResourcePath) {
            final var dirIterator = walk((ResourcePath) dir, 2).stream();
            return new DirectoryStream<>() {

                @Override
                public void close() throws IOException {

                }

                @Override
                public Iterator<Path> iterator() {
                    return dirIterator.iterator();
                }
            };
        }
        throw new IllegalArgumentException(dir.getClass().getName());
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void copy(Path source, Path target, CopyOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        if (path instanceof ResourcePath) {
            final var resourcePath = ((ResourcePath) path).resourcePath();
            if (classLoader.getResourceAsStream(resourcePath) == null) {
                throw new FileNotFoundException(resourcePath);
            }
        }
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        if (type.equals(BasicFileAttributes.class)) {
            return (A) new BasicFileAttributes() {

                @Override
                public FileTime lastModifiedTime() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public FileTime lastAccessTime() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public FileTime creationTime() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean isRegularFile() {
                    if (path instanceof ResourcePath) {
                        return classLoader.getResourceAsStream(((ResourcePath) path).resourcePath()) != null;
                    }
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean isDirectory() {
                    if (path instanceof ResourcePath) {
                        return classLoader.getResourceAsStream(((ResourcePath) path).resourcePath()) != null;
                    }
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean isSymbolicLink() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean isOther() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public long size() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Object fileKey() {
                    return path;
                }
            };
        } else {
            throw new IllegalArgumentException("Attributes type is not supported: " + type.getName());
        }
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

}
