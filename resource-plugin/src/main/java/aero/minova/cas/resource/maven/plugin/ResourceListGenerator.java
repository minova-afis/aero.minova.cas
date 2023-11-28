package aero.minova.cas.resource.maven.plugin;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

@Mojo(name = "generate-resource-file", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class ResourceListGenerator extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    public void execute() throws MojoExecutionException {
        final var classesFolder = Path.of(project.getBuild().getDirectory(), "classes");
        final var resourceFolder = classesFolder.resolve("aero.minova.app.resources");
        final var resourceFile = resourceFolder.resolve(project.getGroupId() + "." + project.getArtifactId() + ".resources.txt");
        try {
            Files.createDirectories(resourceFolder);
            final var resourceList = new StringBuilder();
            for (String resourceSubFolderName : new String[]{"forms", "i18n", "images", "pdf", "plugins", "reports", "setup", "sql", "tables"}) {
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
            Files.writeString(resourceFile, resourceList.toString());
            // TODO files als Sonderfall
        } catch (IOException e) {
            throw new MojoExecutionException("Could not generate resource file: " + resourceFile, e);
        }
    }

    private static String normalize(Path classesFolder, Path arg) {
        return arg.toString().substring(classesFolder.toString().length())
                .replace(FileSystems.getDefault().getSeparator(), "/");
    }
}
