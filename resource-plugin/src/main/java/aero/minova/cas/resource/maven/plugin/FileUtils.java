package aero.minova.cas.resource.maven.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;

public class FileUtils {

	public static boolean copyJarResourcesRecursively(final File destDir, final JarFile jarFile, final JarEntry toCopy) throws IOException {

		File parent = destDir.getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}

		for (final Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
			final JarEntry entry = e.nextElement();

			if (!entry.toString().startsWith(toCopy.toString())) {
				continue;
			}

			final String filename = StringUtils.removeStart(entry.getName(), toCopy.getName());

			final File f = new File(destDir, filename);
			if (!entry.isDirectory()) {
				final InputStream entryInputStream = jarFile.getInputStream(entry);
				if (!FileUtils.copyStream(entryInputStream, f)) {
					return false;
				}
				entryInputStream.close();
			} else {
				if (!FileUtils.ensureDirectoryExists(f)) {
					throw new IOException("Could not create directory: " + f.getAbsolutePath());
				}
			}
		}
		return true;
	}

	private static boolean copyStream(final InputStream is, final File f) {
		try {
			return FileUtils.copyStream(is, new FileOutputStream(f));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean copyStream(final InputStream is, final OutputStream os) {
		try {
			final byte[] buf = new byte[1024];

			int len = 0;
			while ((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
			is.close();
			os.close();
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean ensureDirectoryExists(final File f) {
		return f.exists() || f.mkdir();
	}
}