package aero.minova.cas.resource.maven.plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

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

	@Override
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
										Path i18nFilePath = resourceFolder.resolve(jarPath.getFileName() + ".resources").resolve(jarEntry.getName());
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
			mergeI18n(resourceFolder, classesFolder);
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
		try (InputStream in = fromJar.getInputStream(jarDir)) {
			Files.write(destDir.toPath(), in.readAllBytes());
		} catch (IOException e) {
			throw new IOException("Could not copy asset from jar file.", e);
		}
	}

	public void mergeI18n(Path resourceFolder, Path classesFolder) {
		/*
		 * Der Key der äußeren Map ist der Name der message.properties-Datei, da dieser für jede Sprache leicht anders ist, z.B. message_de.properties. Der Key
		 * der inneren Map ist der zu übersetzende Part, z.B. xtcasUserGroup. Der Value der inneren Map ist dann die Übersetzung, z.B. Benutzergruppen.
		 */
		Map<String, Map<String, String>> mergedI18nMapping = new HashMap<>();

		try (final var resources = Files.walk(resourceFolder)) {
			resources.forEach(r -> {
				if (Files.isRegularFile(r) && r.getParent().endsWith("i18n")) {

					try (Stream<String> stream = Files.lines(r, StandardCharsets.ISO_8859_1)) {

						stream.forEach(l -> {
							l = l.strip();
							// Kommentare überspringen
							if (!l.startsWith("#") && !l.isBlank()) {
								String messagePropertiesName = r.getFileName().toString();
								
								int divider = l.indexOf("=");
								if (divider == -1) {
									throw new RuntimeException(
											"Line \"" + l + "\" in file " + r.toAbsolutePath().toString() + " does not contain divider character \"=\"");
								}

								String key = l.substring(0, divider);
								String value = l.substring(divider + 1, l.length());

								// Diese Sprache wurde noch nicht bearbeitet, also in die Map einfügen.
								if (!mergedI18nMapping.containsKey(messagePropertiesName)) {
									Map<String, String> propertyContent = new HashMap<>();
									propertyContent.put(key, value);
									mergedI18nMapping.put(messagePropertiesName, propertyContent);

									// Für diese Sprache haben wir schon einen Eintrag. Weitere Prüfung nötig.
								} else if (mergedI18nMapping.containsKey(messagePropertiesName)) {
									if (!mergedI18nMapping.get(messagePropertiesName).containsKey(key)) {
										Map<String, String> propertyContent = mergedI18nMapping.get(messagePropertiesName);
										propertyContent.put(key, value);
										mergedI18nMapping.put(messagePropertiesName, propertyContent);

									} else if (!mergedI18nMapping.get(messagePropertiesName).get(key).equals(value)) {
										throw new RuntimeException("Could not merge i18n because translation is different! "
												+ mergedI18nMapping.get(messagePropertiesName).get(key) + " does not equal " + value + " for Key " + key);

									}
								}
							}
						});
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});

			// Nachdem wir alle Dateien eingelesen haben, können wir die zusammen gemergten Übersetzungsdateien anlegen.
			for (String fileName : mergedI18nMapping.keySet()) {
				File file = classesFolder.resolve("i18n").resolve(fileName).toFile();
				File parent = new File(file.getParent());

				if (!parent.exists()) {
					parent.mkdirs();
				} else {
					// Die Datei, die davor noch existiert hat, ist die alte aus dem Kundenprojekt, die wir vorhin schon weggesichert haben.
					if (file.exists()) {
						file.delete();
					}
				}

				try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
					for (Map.Entry<String, String> entry : mergedI18nMapping.get(fileName).entrySet()) {
						bf.write(entry.getKey() + "=" + entry.getValue());
						bf.newLine();
					}
					bf.flush();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
