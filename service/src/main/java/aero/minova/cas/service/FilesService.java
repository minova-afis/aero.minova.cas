package aero.minova.cas.service;

import static aero.minova.cas.resources.ResourceFileSystemProvider.FILE_SYSTEM_PROVIDER;
import static java.nio.file.Files.isDirectory;
import static java.util.Arrays.asList;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.namespace.QName;

import aero.minova.cas.resources.ResourceFileSystem;
import aero.minova.cas.resources.ResourceFileSystemProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlViewController;
import aero.minova.cas.service.mdi.Main;
import aero.minova.cas.service.mdi.Main.Action;
import aero.minova.cas.service.mdi.Main.Entry;
import aero.minova.cas.service.mdi.Main.Menu;
import aero.minova.cas.service.mdi.MenuType;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import lombok.Setter;

@Service
@Setter
public class FilesService {

    @org.springframework.beans.factory.annotation.Value("${aero_minova_core_application_root_path:.}")
    private String rootPath;

    @org.springframework.beans.factory.annotation.Value("${files.permission.check:false}")
    boolean permissionCheck;
    @org.springframework.beans.factory.annotation.Value("${fat.jar.mode:false}")
    boolean isFatJarMode;

    @Autowired
    SecurityService securityUtils;

    @Autowired
    SqlViewController viewController;

    @Autowired
    public CustomLogger customLogger;
    private Path systemFolder;
    private Path internalFolder;
    private Path logsFolder;
    private Path zipsFolder;
    private Path md5Folder;

    public FilesService() {
    }

    public FilesService(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * Initialisiert alle nötigen Ordner. Mit {@link Path#toAbsolutePath()} und {@link Path#normalize} werden die Pfade so eindeutig wie möglich.
     */
    @PostConstruct
    public void setUp() {
        if (isFatJarMode) {
            systemFolder = FILE_SYSTEM_PROVIDER.getFileSystem(null).getPath(rootPath);
            final var notExistingFolder = systemFolder.resolve("not-existing-folder");
            internalFolder = systemFolder.resolve("Internal");
            logsFolder = internalFolder.resolve("UserLogs");
            md5Folder = internalFolder.resolve("MD5");
            zipsFolder = internalFolder.resolve("Zips");
        } else {
            if (rootPath == null || rootPath.isEmpty()) {
                rootPath = Paths.get(".").toAbsolutePath().normalize().toString();
            }
            systemFolder = Paths.get(rootPath).toAbsolutePath().normalize();
            internalFolder = systemFolder.resolve("Internal").toAbsolutePath().normalize();
            logsFolder = internalFolder.resolve("UserLogs").toAbsolutePath().normalize();
            md5Folder = internalFolder.resolve("MD5").toAbsolutePath().normalize();
            zipsFolder = internalFolder.resolve("Zips").toAbsolutePath().normalize();
            if (!isDirectory(systemFolder)) {
                customLogger.logFiles("msg.SystemFolder %" + systemFolder);
            }
            if (!isDirectory(internalFolder)) {
                customLogger.logFiles("msg.InternalFolder %" + internalFolder);
            }
            if (!isDirectory(logsFolder)) {
                customLogger.logFiles("msg.LogsFolder %" + logsFolder);
            }
            if (md5Folder.toFile().mkdirs()) {
                customLogger.logFiles("Creating directory " + md5Folder);
            }
            if (zipsFolder.toFile().mkdirs()) {
                customLogger.logFiles("Creating directory " + zipsFolder);
            }
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
     * @param dir das zu durchsuchende Directory
     * @return eine Liste an allen Files in dem übergebenen Directory
     * @throws FileNotFoundException Falls das Directory nicht existiert oder der übergebene Pfad nicht auf ein Directory zeigt.
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
     * @param path Pfad zur gewünschten Datei.
     * @throws Exception RuntimeException, falls User nicht erforderliche Privilegien besitzt, IllegalAccessException, falls der Pfad nicht in das abgegrenzte
     *                   Dateisystem zeigt, NoSuchFileException, falls gewünschte Datei nicht existiert.
     */
    public Path checkLegalPath(Path path) throws Exception {
        if (permissionCheck) {
            List<Row> privileges = securityUtils.getPrivilegePermissions("files/read:" + path);
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
        return inputPath;
    }

    /**
     * Methode zum Zippen einer Datei.
     *
     * @param source   String, Teil des ursprünglichen Pfades, welcher abgeschnitten werden muss.
     * @param zipFile  File, gewünschtes finales Zip-File.
     * @param fileList List&lt;Path&gt;, Pfade zu Dateien, welche gezipped werden sollen.
     * @throws RuntimeException      Falls eine Datei nicht gezipped werden kann, zum Beispiel aufgrund eines falschen Pfades.
     * @throws FileNotFoundException
     */
    public void zip(String source, File zipFile, List<Path> fileList) throws Exception {
        ZipEntry ze = null;
        // Jede Datei wird einzeln zu dem ZIP hinzugefügt.
        FileOutputStream fos = new FileOutputStream(zipFile);
        try (ZipOutputStream zos = new ZipOutputStream(fos);) {

            for (Path filePath : fileList) {

                // noch mehr zipps in einer zip sind sinnlos
                if (filePath.toFile().isFile() && (!filePath.toString().contains("zip"))) {
                    ze = new ZipEntry(filePath.toString().substring(source.length() + 1, filePath.toString().length()).replace('\\', '/'));

                    // CreationTime der Zip und Änderungs-Zeitpunkt der Zip auf diese festen
                    // Zeitpunkte setzen, da sich sonst jedes Mal der md5 Wert ändert,
                    // wenn die Zip erstellt wird.
                    ze.setCreationTime(FileTime.from(Instant.EPOCH));
                    ze.setTime(0);
                    zos.putNextEntry(ze);

                    // Jeder Eintrag wird nacheinander in die ZIP Datei geschrieben mithilfe eines
                    // Buffers.
                    FileInputStream fis = new FileInputStream(filePath.toFile());

                    int len;
                    byte[] buffer = new byte[1024];

                    try (BufferedInputStream entryStream = new BufferedInputStream(fis, 2048)) {
                        while ((len = entryStream.read(buffer, 0, 1024)) != -1) {
                            zos.write(buffer, 0, len);
                        }
                    } finally {
                        zos.closeEntry();
                        fis.close();
                    }
                }
            }
        } catch (Exception e) {
            if (ze != null) {
                customLogger.logFiles("Error while zipping file " + ze.getName());
                throw new RuntimeException("msg.ZipError %" + ze.getName());
            } else {
                // Landet nur hier, wenn es nicht mal bis in das erste if geschafft hat.
                customLogger.logFiles("Error while accessing file path for file to zip.");
                throw new RuntimeException("Error while accessing file path " + source + " for file to zip.", e);
            }
        } finally {
            fos.close();
        }
    }

    /**
     * Methode zum Entpacken einer Datei.
     *
     * @param fileZip     File, die gepackte Datei.
     * @param destDirName Path, Pfad im Dateisystem, an welchem der Inhalt des Zips gespeichert werden soll.
     * @throws IOException Falls das Directory nicht existiert oder kein Directory ist oder falls die Datei nicht entpackt werden kann.
     */
    public void unzipFile(File fileZip, Path destDirName) throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileZip);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String zippedFileEntry = ze.getName();
                if (zippedFileEntry.startsWith(File.separator)) {
                    zippedFileEntry = zippedFileEntry.substring(1);
                }
                File newFile = destDirName.resolve(zippedFileEntry).toFile();
                // create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            customLogger.logFiles("Error while unzipping file " + fileZip + " into directory " + destDirName);
            throw new RuntimeException("msg.UnZipError %" + fileZip + " %" + destDirName, e);

        }
    }

    /**
     * Liest die Tabelle xtcasMDI aus und generiert userspezifisch die MDI.
     *
     * @return die generierte MDI als byte Array.
     */
    public byte[] readMDI() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        Table mdiQuery = new Table();
        mdiQuery.setName("xtcasMdi");

        mdiQuery.addColumn(new Column("KeyText", DataType.STRING));
        mdiQuery.addColumn(new Column("Icon", DataType.STRING));
        mdiQuery.addColumn(new Column("Label", DataType.STRING));
        mdiQuery.addColumn(new Column("Menu", DataType.STRING));
        mdiQuery.addColumn(new Column("Position", DataType.DOUBLE));
        mdiQuery.addColumn(new Column("SecurityToken", DataType.STRING));
        mdiQuery.addColumn(new Column("MdiTypeKey", DataType.INTEGER));
        mdiQuery.addColumn(new Column("LastAction", DataType.INTEGER));

        // Es muss nach Lastaction > 0 geschaut werden.
        Row mdiRow = new Row();
        mdiRow.setValues(asList(null, null, null, null, null, null, null, new Value(0, ">")));

        List<Row> queryRows = new ArrayList<>();
        queryRows.add(mdiRow);
        mdiQuery.setRows(queryRows);

        Table mdiData;
        try {
            mdiData = viewController.getIndexView(mdiQuery);
        } catch (Exception e) {
            throw new RuntimeException("Error while trying to access xtcasMdi.", e);
        }
        customLogger.logFiles("Generating MDI for User " + user);

        if (mdiData.getRows().isEmpty()) {
            throw new RuntimeException("No MDI definition for " + user);
        }

        // Rückgabe nach Position sortieren.
        int position = mdiData.findColumnPosition("Position");
        int idPosition = mdiData.findColumnPosition("KeyText");

        mdiData.getRows().sort((r1, r2) -> {
            int compareReturn;

            // Auch beachten, wenn eines der Values null ist
            if (r1.getValues().get(position) == null && r2.getValues().get(position) == null) {
                compareReturn = 0;
            } else if (r1.getValues().get(position) == null) {
                compareReturn = -1;
            } else if (r2.getValues().get(position) == null) {
                compareReturn = 1;
            } else {

                // "Normalfall", beide Positionen != null -> direkt vergleichen
                Double position1 = r1.getValues().get(position).getDoubleValue();
                Double position2 = r2.getValues().get(position).getDoubleValue();
                compareReturn = position1.compareTo(position2);
            }

            // Bei gleicher Position nach ID sortieren
            if (compareReturn == 0) {
                Value id1v = r1.getValues().get(idPosition);
                Value id2v = r2.getValues().get(idPosition);

                if (id1v == null && id2v == null) {
                    compareReturn = 0;
                } else if (id1v == null) {
                    compareReturn = -1;
                } else if (id2v == null) {
                    compareReturn = 1;
                } else {
                    // Verleich anhand der IDs
                    String id1 = id1v.getStringValue();
                    String id2 = id2v.getStringValue();
                    compareReturn = id1.compareTo(id2);
                }
            }

            return compareReturn;
        });

        // Ab hier wird die MDI erstellt.

        Main main = new Main();

        // Das Menu in Main ist das EINZIGE Menu, dass wirklich von der Menu-Klasse ist. Alle anderen sind MenuType.
        Menu mainMenu = new Menu();
        mainMenu.setId("main");
        main.setMenu(mainMenu);
        List<Row> formRows = new ArrayList<>();
        Map<String, MenuType> menuById = new HashMap<>();
        Map<String, List<MenuType>> menuBySupermenu = new HashMap<>();

        // Hier das Ergebnis der Abfrage nach den verschiedenen MDITypeKeys unterscheiden.
        for (Row r : mdiData.getRows()) {
            int mdiKey = mdiData.getValue("MdiTypeKey", r).getIntegerValue();

            // Key = 1 ist Eintrag, um Maske zu öffen.
            if (mdiKey == 1) {
                formRows.add(r);

                // Key = 2 ist Menü / Untermenü.
            } else if (mdiKey == 2) {
                MenuType menu = new MenuType();
                menu.setId(mdiData.getValue("KeyText", r).getStringValue());
                menu.setText(mdiData.getValue("Label", r).getStringValue());

                String supermenu = mdiData.getValue("Menu", r) == null ? "null" : mdiData.getValue("Menu", r).getStringValue();
                menuBySupermenu.putIfAbsent(supermenu, new ArrayList<>());
                menuBySupermenu.get(supermenu).add(menu);

                menuById.put(menu.getId(), menu);

                // Key = 3 ist der Eintrag ganz oben in der Toolbar. Dieser darf nur einmal vorhanden sein.
            } else if (mdiKey == 3) {
                main.setIcon(mdiData.getValue("Icon", r).getStringValue());
                main.setTitle(mdiData.getValue("Label", r).getStringValue());
            } else {
                throw new IllegalArgumentException("MdiKey " + mdiKey + " not implemented!");
            }
        }

        // Anhängen von Entries an die Menüs und Erstellen der Actions.
        for (Row r : formRows) {
            Action action = new Action();
            action.setAction(mdiData.getValue("KeyText", r).getStringValue() + ".xml");
            action.setId(mdiData.getValue("KeyText", r).getStringValue());
            action.setIcon(mdiData.getValue("Icon", r).getStringValue());
            action.setText(mdiData.getValue("Label", r).getStringValue());
            main.getAction().add(action);

            Entry entry = new Entry();
            entry.setId(action);
            entry.setType("action");

            if (mdiData.getValue("Menu", r) != null && menuById.get(mdiData.getValue("Menu", r).getStringValue()) != null) {
                menuById.get(mdiData.getValue("Menu", r).getStringValue()).getEntryOrMenu().add(entry);
            } else {
                customLogger.logFiles("No menutype found for Action with ID " + action.getAction() + ". The mask will not be chooseable.");
            }

        }

        if (menuBySupermenu.isEmpty()) {
            RuntimeException e = new RuntimeException("No menu defined. Mdi cannot be build!");
            customLogger.logError("Error while building mdi for user " + user + ".", e);
            throw e;
        }

        // Hier rekursiver Aufruf zum Anhängen der Untermenüs an die Menüs bzw. der Menüs an das MainMenu. Leere Menüs werden nicht angehängt.
        for (MenuType m : menuBySupermenu.get("null")) {
            addMenus(m, menuBySupermenu.get(m.getId()), menuBySupermenu);
            if (!m.getEntryOrMenu().isEmpty()) {
                mainMenu.getMenuOrEntry().add(m);
            } else {
                customLogger.logFiles("Menu " + m.getId() + " is empty and has no Actions. It will not be pickable over the main menu.");
            }
        }

        // Umwandeln in byteArray.
        return xml2byteArray(main);
    }

    /**
     * Fügt durch den rekursiven Aufruf Untermenüs an die Menüs hinzu. Leere Menüs werden nicht angehängt.
     *
     * @param superMenu       Obermenü als MenuType.
     * @param subMenues       Liste an MenuTypes, welche an das superMenu gehängt werden soll.
     * @param menuBySupermenu Gesamte Map an anzuhängenden Menüs.
     */
    private void addMenus(MenuType superMenu, List<MenuType> subMenues, Map<String, List<MenuType>> menuBySupermenu) {
        if (subMenues == null) {
            return;
        }

        for (MenuType m : subMenues) {
            addMenus(m, menuBySupermenu.get(m.getId()), menuBySupermenu);
            if (!m.getEntryOrMenu().isEmpty()) {
                superMenu.getEntryOrMenu().add(m);
            } else {
                customLogger.logFiles("Menu " + m.getId() + " is empty and will not be in the finished Mdi.");
            }
        }
    }

    /**
     * Erzeugt aus der übergebenen Main-Klasse einen byte Array.
     *
     * @param mainXML ein "befülltes" Main-Objekt.
     * @return die übergebene Main-Klasse als byte Array.
     */
    public byte[] xml2byteArray(Main mainXML) {
        try {
            // Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Main.class);
            // Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, java.lang.Boolean.TRUE);

            QName qName = new QName("", "main");
            JAXBElement<Main> root = new JAXBElement<>(qName, Main.class, mainXML);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            jaxbMarshaller.marshal(root, out);
            return out.toByteArray();

        } catch (Exception e) {
            customLogger.logError(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}