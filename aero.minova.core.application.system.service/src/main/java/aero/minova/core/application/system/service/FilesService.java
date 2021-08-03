package aero.minova.core.application.system.service;

import static java.nio.file.Files.isDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import aero.minova.core.application.system.CustomLogger;
import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.domain.Row;

@Service
public class FilesService {

	@Value("${aero_minova_core_application_root_path:.}")
	private String rootPath;

	@Value("${files.permission.check:false}")
	boolean permissionCheck;

	@Autowired
	SqlViewController svc;

	private Path programFilesFolder;
	private Path sharedDataFolder;
	private Path systemFolder;
	private Path internalFolder;
	private Path logsFolder;
	private Path zipsFolder;
	private Path md5Folder;

	private final Logger logger = LoggerFactory.getLogger(FilesService.class);
	static CustomLogger customLogger = new CustomLogger();

	public FilesService() {}

	public FilesService(String rootPath) {
		this.rootPath = rootPath;
	}

	/**
	 * Initialisiert alle nötigen Ordner. Mit {@link Path#toAbsolutePath()} und {@link Path#normalize} werden die Pfade so eindeutig wie möglich.
	 */
	@PostConstruct
	public void setUp() {
		if (rootPath == null || rootPath.isEmpty()) {
			rootPath = Paths.get(".").toAbsolutePath().normalize().toString();
		}
		systemFolder = Paths.get(rootPath).toAbsolutePath().normalize();
		sharedDataFolder = systemFolder.resolve("Shared Data").toAbsolutePath().normalize();
		internalFolder = systemFolder.resolve("Internal").toAbsolutePath().normalize();
		logsFolder = internalFolder.resolve("UserLogs").toAbsolutePath().normalize();
		md5Folder = internalFolder.resolve("MD5").toAbsolutePath().normalize();
		zipsFolder = internalFolder.resolve("Zips").toAbsolutePath().normalize();
		programFilesFolder = sharedDataFolder.resolve("Program Files").toAbsolutePath().normalize();
		if (!isDirectory(systemFolder)) {
			logger.error("msg.SystemFolder %" + systemFolder);
		}
		if (!isDirectory(sharedDataFolder)) {
			logger.error("msg.SharedFolder %" + sharedDataFolder);
		}
		if (!isDirectory(programFilesFolder)) {
			logger.error("msg.ProgramFilesFolder %" + programFilesFolder);
		}
		if (!isDirectory(internalFolder)) {
			logger.error("msg.InternalFolder %" + internalFolder);
		}
		if (!isDirectory(programFilesFolder)) {
			logger.error("msg.LogsFolder %" + logsFolder);
		}

		if (md5Folder.toFile().mkdirs()) {
			customLogger.logFiles("Creating directory " + md5Folder);
		}
		if (zipsFolder.toFile().mkdirs()) {
			customLogger.logFiles("Creating directory " + zipsFolder);
		}

	}

	/**
	 * Gibt den Pfad zum Systems-Ordner zurück.
	 * 
	 * @return Pfad zum System-Ordner.
	 */
	public Path getSystemFolder() {
		return systemFolder;
	}

	/**
	 * Gibt den Pfad zum UserLogs-Ordner zurück.
	 * 
	 * @return Pfad zum UserLogs-Ordner.
	 */
	public Path getLogsFolder() {
		return logsFolder;
	}

	/**
	 * Gibt den Pfad zum MD5-Ordner zurück.
	 * 
	 * @return Pfad zum MD5-Ordner.
	 */
	public Path getMd5Folder() {
		return md5Folder;
	}

	/**
	 * Gibt den Pfad zum Zips-Ordner zurück.
	 * 
	 * @return Pfad zum Zip-Ordner.
	 */
	public Path getZipsFolder() {
		return zipsFolder;
	}

	/**
	 * Diese Methode erzeugt eine Liste aller vorhandenen Files in einem Directory. Falls sich noch weitere Directories in diesem befinden, wird deren Inhalt
	 * ebenfalls aufgelistet
	 * 
	 * @param dir
	 *            das zu durchsuchende Directory
	 * @return eine Liste an allen Files in dem übergebenen Directory
	 * @throws FileNotFoundException
	 *             Falls das Directory nicht existiert oder der übergebene Pfad nicht auf ein Directory zeigt.
	 */
	public List<Path> populateFilesList(Path dir) throws FileNotFoundException {
		List<Path> filesListInDir = new ArrayList<>();
		File[] files = dir.toFile().listFiles();
		if (files == null) {
			throw new FileNotFoundException("Cannot access sub folder: " + dir);
		}
		for (File file : files) {
			filesListInDir.add(Paths.get(file.getAbsolutePath()));
			if (file.isDirectory()) {
				filesListInDir.addAll(populateFilesList(file.toPath()));
			}
		}
		return filesListInDir;
	}

	/**
	 * Überprüft, ob die angeforderte Datei existiert und ob der Pfad dorthin innerhalb des dedizierten Dateisystems liegt.
	 * 
	 * @param path
	 *            Pfad zur gewünschten Datei.
	 * @throws Exception
	 *             RuntimeException, falls User nicht erforderliche Privilegien besitzt, IllegalAccessException, falls der Pfad nicht in das abgegrenzte
	 *             Dateisystem zeigt, NoSuchFileException, falls gewünschte Datei nicht existiert.
	 */
	public void checkLegalPath(Path path) throws Exception {
		if (permissionCheck) {
			List<Row> privileges = svc.getPrivilegePermissions("files/read:" + path).getRows();
			if (privileges.isEmpty()) {
				throw new RuntimeException("msg.PrivilegeError %" + "files/read:" + path);
			}
		}
		Path inputPath = getSystemFolder().resolve(path).toAbsolutePath().normalize();
		File f = inputPath.toFile();
		if (!inputPath.startsWith(getSystemFolder())) {
			throw new IllegalAccessException("msg.PathError %" + path + " %" + inputPath);
		}
		if (!f.exists()) {
			throw new NoSuchFileException("msg.FileError %" + path);
		}
	}

}