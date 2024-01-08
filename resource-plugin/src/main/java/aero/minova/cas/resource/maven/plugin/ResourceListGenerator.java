package aero.minova.cas.resource.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarFile;

@Mojo(name = "generate-resource-file", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class ResourceListGenerator extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "createDeployList", required = false, defaultValue = "false")
    boolean createDeployList;

    public void execute() throws MojoExecutionException {
        final var targetFolder = Path.of(project.getBuild().getDirectory());
        final var libsFolder = targetFolder.resolve("jar-dependencies");
        final var classesFolder = targetFolder.resolve("classes");
        final var resourceFolderName = "aero.minova.app.resources";
        final var resourceFolder = classesFolder.resolve(resourceFolderName);
        final var resourceFileName = project.getGroupId() + "." + project.getArtifactId() + ".resources.txt";
        final var resourceFile = resourceFolder.resolve(resourceFileName);
        final var deployFile = resourceFolder.resolve("deployed.resources.txt");
        try {
            Files.createDirectories(resourceFolder);
            final var resourceList = new StringBuilder();
            for (String resourceSubFolderName : new String[]{"files", "forms", "i18n", "images", "pdf", "plugins", "reports", "setup", "sql", "tables"}) {
                final var resourceSubFolder = classesFolder.resolve(resourceSubFolderName);
                if (Files.isDirectory(resourceSubFolder)) {
                    try (final var resources = Files.walk(resourceSubFolder)) {
                        resources.forEach(r -> {
                            if (Files.isRegularFile(r)) {
                                resourceList.append(normalize(classesFolder, r));
                                resourceList.append('\n');
                            } else if (Files.isDirectory(r)) {
                                resourceList.append(normalize(classesFolder, r) + "/");
                                resourceList.append('\n');
                            } else {
                                getLog().warn("Strange resource file: " + r);
                            }
                        });
                    }
                }
            }
            final var resourceSubFolder = classesFolder;
            if (Files.isDirectory(resourceSubFolder)) {
                try (final var resources = Files.walk(resourceSubFolder, 1)) {
                    resources.forEach(r -> {
                        if (Files.isRegularFile(r)) {
                            resourceList.append(normalize(classesFolder, r));
                            resourceList.append('\n');
                        }
                    });
                }
            }
            Files.writeString(resourceFile, resourceList.toString());
            if (createDeployList && Files.isDirectory(libsFolder)) {
                final var deployList = new StringBuilder();
                try (final var jarFiles = Files.walk(libsFolder)) {
                    jarFiles.forEach(jarPath -> {
                        if (Files.isRegularFile(jarPath) && jarPath.toString().endsWith(".jar")) {
                            try {
                                final var jar = new JarFile(jarPath.toFile());
                                final var jarContent = jar.entries();
                                while (jarContent.hasMoreElements()) {
                                    final var jarEntry = jarContent.nextElement();
                                    if (!jarEntry.getName().equals("aero.minova.app.resources/")
                                            && jarEntry.getName().startsWith("aero.minova.app.resources/")) {
                                        deployList.append("/" + jarEntry.getName());
                                        deployList.append('\n');
                                    }
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
                deployList.append("/" + resourceFolderName + "/" + resourceFileName);
                deployList.append('\n');
                Files.writeString(deployFile, deployList.toString());
            } else if (createDeployList) {
                throw new FileNotFoundException("libs folder is missing for `createDeployList` goal: " + libsFolder);
            }
        } catch (
                IOException e) {
            throw new MojoExecutionException("Could not generate resource file: " + resourceFile, e);
        }

    }

    private static String normalize(Path classesFolder, Path arg) {
        return arg.toString().substring(classesFolder.toString().length())
                .replace(FileSystems.getDefault().getSeparator(), "/");
    }
}
