package aero.minova.cas.resource.maven.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

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
		final var customerProjectResourcesI18n = resourceFolder.resolve(project.getGroupId() + "." + project.getArtifactId() + ".resources").resolve("i18n");
		try {
			Files.createDirectories(resourceFolder);
			final var resourceList = new StringBuilder();
			for (String resourceSubFolderName : new String[] { "files", "forms", "i18n", "images", "pdf", "plugins", "reports", "setup", "sql", "tables" }) {
				final var resourceSubFolder = classesFolder.resolve(resourceSubFolderName);
				if (Files.isDirectory(resourceSubFolder)) {
					try (final var resources = Files.walk(resourceSubFolder)) {
						resources.forEach(r -> {
							if (Files.isRegularFile(r)) {
								resourceList.append(normalize(classesFolder, r));
								resourceList.append('\n');

								// Die vorhandenen i18ns sind nur die aus dem Kundenprojekt. Wir kopieren sie erst einmal weg, damit wir sie später mergen
								// können.
								if (resourceSubFolder.endsWith("i18n") && !resourceSubFolder.getFileName().toString().contains(resourceFolder.toString())) {
									try {
										if (!customerProjectResourcesI18n.toFile().exists()) {
											customerProjectResourcesI18n.toFile().mkdirs();
										}
										Files.copy(resourceSubFolder.resolve(r), customerProjectResourcesI18n.resolve(r.getFileName()));
									} catch (IOException e) {
										throw new RuntimeException(
												"ResourceListGenerator was not able to copy " + r.getFileName() + " to " + customerProjectResourcesI18n, e);
									}
								}

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
									if (!jarEntry.isDirectory() && jarEntry.getName().startsWith("i18n/") && jarEntry.getName().endsWith(".properties")) {
										Path i18nFilePath = resourceFolder.resolve(jarPath.getFileName()).resolve(jarEntry.getName() + ".resources");
										copyJarContentToDirectory(jar, jarEntry, i18nFilePath.toFile());
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
		} catch (IOException e) {
			throw new MojoExecutionException("Could not generate resource file: " + resourceFile, e);
		}

	}

	private static String normalize(Path classesFolder, Path arg) {
		return arg.toString().substring(classesFolder.toString().length()).replace(FileSystems.getDefault().getSeparator(), "/");
	}

	/**
	 * Copies a directory from a jar file to an external directory.
	 */
	public static void copyJarContentToDirectory(JarFile fromJar, JarEntry jarDir, File destDir) throws IOException {
		File parent = destDir.getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}
		InputStream in = fromJar.getInputStream(jarDir);
		try (FileOutputStream out = new FileOutputStream(destDir)) {

			byte[] buffer = new byte[8 * 1024];

			int s = 0;
			while ((s = in.read(buffer)) > 0) {
				out.write(buffer, 0, s);
			}
		} catch (IOException e) {
			throw new IOException("Could not copy asset from jar file", e);
		} finally {
			in.close();
		}
	}
}
