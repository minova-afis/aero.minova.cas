package ch.minova.install.setup;

import java.text.MessageFormat;

public class VersionInfo {
	private final int majorVersion;
	private final int minorVersion;
	private final int patchLevel;
	private final int buildnumber;
	private final String buildDateTime;
	private final String versionString;
	private final String modulName;

	/**
	 * Methode: Object getSetup_xmlOne_of() Beschreibung: Auslesen des One-Of Module aus der Setup.xml Übergabeparameter: - Return: Object
	 */
	public VersionInfo(final String versionString, final String modulname) {
		this.versionString = versionString;
		final String VersionInfoDummy = this.versionString.substring(versionString.indexOf(".") + 1);
		this.majorVersion = Integer.parseInt(versionString.substring(0, versionString.indexOf(".")));
		this.minorVersion = Integer.parseInt(VersionInfoDummy.substring(0, VersionInfoDummy.indexOf(".")));
		this.patchLevel = Integer.parseInt(VersionInfoDummy.substring(VersionInfoDummy.indexOf(".") + 1, VersionInfoDummy.indexOf("-")));
		this.buildnumber = Integer.parseInt(VersionInfoDummy.substring(VersionInfoDummy.indexOf("-") + 1, VersionInfoDummy.indexOf(" ")));
		this.buildDateTime = VersionInfoDummy.substring(VersionInfoDummy.indexOf(" ") + 1);
		this.modulName = modulname;
	}

	public VersionInfo(final String version, final String builddatetime, final String modulname) {
		this(version + " " + builddatetime, modulname);
	}

	public VersionInfo(final String major, final String minor, final String patchlevel, final String buildNumber, final String buildDate,
			final String modulname) {
		this(major + "." + minor + "." + patchlevel + "-" + buildNumber + " " + buildDate, modulname);
	}

	/**
	 * vergleicht die übergebene Versionsnummer mit der Nummer, die über den Konstruktor übergeben worden ist
	 * 
	 * @param versionToCompare
	 * @return int
	 * @throws VersionInfoException
	 */
	public int compareTo(final String versionToCompare) throws VersionInfoException {
		String dummy = versionToCompare.substring(versionToCompare.indexOf(".") + 1);
		if (Integer.parseInt(versionToCompare.substring(0, versionToCompare.indexOf("."))) < this.majorVersion) {
			throw new VersionInfoException(MessageFormat.format("MajorVersion of modul{0} is {1}: required is {2}", this.modulName,
					versionToCompare.substring(0, versionToCompare.indexOf(".")), this.majorVersion));
		} else {
			if ((Integer.parseInt(dummy.substring(0, dummy.indexOf(".")))) < this.minorVersion) {
				throw new VersionInfoException(MessageFormat.format("MinorVersion of modul{0} is {1}: required is {2}", this.modulName,
						dummy.substring(0, dummy.indexOf(".")), this.minorVersion));
			} else {
				dummy = dummy.substring(dummy.indexOf(".") + 1);
				if ((Integer.parseInt(dummy)) < this.patchLevel) {
					throw new VersionInfoException(MessageFormat.format("PathLevel of modul{0} is {1}: required is {2}", this.modulName,
							dummy.substring(0, dummy.indexOf(".")), this.patchLevel));
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Überprüfen, ob die übergebene Version mit dieser Version kompatibel ist. Damit Version a mit Version b kompatibel ist, - müssen beie Versionen die
	 * gleiche MajorVersion besitzen - die MinorVersion von a muss kleiner oder gleich der MinorVersion von b entsprechen - Wenn die MinorVersion(en) gleich
	 * sind, muss der PatchLevel von a kleiner oder gleich dem von b sein.
	 * 
	 * @param verinfo
	 *            die zu überprüfende Version
	 * @return true, wenn diese Version abwärtskompatible zu verinfo ist
	 */
	public boolean compatibleTo(final VersionInfo verinfo) {
		if (verinfo.majorVersion == this.majorVersion) {
			if (verinfo.minorVersion <= this.minorVersion) {
				if (verinfo.patchLevel <= this.patchLevel || verinfo.minorVersion < this.minorVersion) {
					return true;
				}
			}
		}
		return false;
	}

	public Object getBuildDateTime() {
		return this.buildDateTime;
	}

	public int getBuildNumber() {
		return this.buildnumber;
	}

	public int getMajorVersion() {
		return this.majorVersion;
	}

	public int getMinorVersion() {
		return this.minorVersion;
	}

	public String getModulName() {
		return this.modulName;
	}

	public int getPatchLevel() {
		return this.patchLevel;
	}

	public Object getSQLPath() {
		// String path = new BaseSetup().getSetup_xml().toString();
		// return path;
		return null;
	}

	@Override
	public String toString() {
		return this.versionString;
	}
}