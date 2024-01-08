package aero.minova.cas.resources;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static aero.minova.cas.resources.ResourceFileSystemProvider.FILE_SYSTEM_PROVIDER;

public class ResourceFileSystem extends FileSystem {
    private ClassLoader classLoader;

    public ResourceFileSystem(ClassLoader classLoaderArg) {
        classLoader = classLoaderArg;
    }

    @Override
    public FileSystemProvider provider() {
        return FILE_SYSTEM_PROVIDER;
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReadOnly() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSeparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path getPath(String first, String... more) {
        final List<String> path = new ArrayList<>();
        for (final var m : first.split("/")) {
            if (!m.isEmpty()) {
                path.add(m);
            }
        }
        for (final var m : more) {
            if (!m.isEmpty()) {
                path.add(m);
            }
        }
        return new ResourcePath(path, this);

    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        throw new UnsupportedOperationException();
    }

    @Override
    public WatchService newWatchService() throws IOException {
        throw new UnsupportedOperationException();
    }
}
