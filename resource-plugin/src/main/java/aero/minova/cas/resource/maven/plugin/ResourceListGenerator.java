package aero.minova.cas.resource.maven.plugin;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mojo(name = "generate-resource-file", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class ResourceListGenerator extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    public void execute() throws MojoExecutionException {
        final var resourceFolder = Path.of(project.getBuild().getDirectory(), "classes", "aero.minova.app.resources");
        final var resourceFile = resourceFolder.resolve(project.getGroupId() + "." + project.getArtifactId() + ".resources.txt");
        try {
            Files.createDirectories(resourceFolder);

        } catch (IOException e) {
            throw new MojoExecutionException("Could not generate resource file: " + resourceFile, e);
        }
    }
}
