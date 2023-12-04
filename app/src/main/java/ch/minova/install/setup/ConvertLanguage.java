package ch.minova.install.setup;

import java.io.File;
import java.io.FilenameFilter;

public class ConvertLanguage {
	public static void main(final String[] args) {
		if (args.length < 1) {
			printHelp();
			return;
		}

		String locale = args[0];
		if (locale.startsWith("-locale=")) {
			locale = locale.substring(8);
		}

		String folder = "../Program Files/reports/";
		if (args.length > 1) {
			folder = args[1] + "/";
		}

		final ConvertLanguage me = new ConvertLanguage();
		me.replace(folder, locale);
	}

	private void replace(final String folderName, final String locale) {
		final File folder = new File(folderName);

		final File[] reports = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File arg0, final String filename) {
				if (filename.endsWith(".report.xsl")) {
					return true;
				}
				return false;
			}
		});

		if (reports == null || reports.length == 0) {
			System.out.println("no files found in " + folderName);
			return;
		}

		for (int i = 0; i < reports.length; i++) {
			final File file = reports[i];
			replaceFile(file, locale);
		}
	}

	private void replaceFile(final File file, final String locale) {
		String filename = file.getAbsolutePath();
		File reportFile;
		filename = filename.substring(0, filename.length() - 11) + "_" + locale + ".dtd";
		reportFile = new File(filename);
		if (!reportFile.exists()) {
			filename = filename.substring(0, filename.length() - 10) + locale.substring(0, (locale + "_").indexOf("_")) + ".dtd";
			reportFile = new File(filename);
		}

		if (!reportFile.exists()) {
			System.out.println(file.getName() + " not replaced!");
			return;
		}

		System.out.println(file.getName() + " will be replaced!");
		throw new UnsupportedOperationException("This is not migrated yet.");
		// TODO ch.minova.report.tools.XSLTransformer.main(new String[] { "-v9", "-locales=" + locale, "-xsl=" + file.getAbsolutePath() });
	}

	private static void printHelp() {
		System.out.println("usage:");
		System.out.println("ConvertLanguage {locale} [folder]");
		System.out.println("  locale: e.g. fr_CH");
		System.out.println("  folder: default = ../Program Files/reports");
	}
}