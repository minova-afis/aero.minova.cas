package aero.minova.cas.api.domain;

public class VersionResponse {

	private final String groupId;
	private final String artefactId;
	private final Integer majorVersion;
	private final Integer minorVersion;
	private final Integer patchVersion;
	private final String versionQualifier;

	public VersionResponse(String groupId, String artefactId, Integer majorVersion, Integer minorVersion, Integer patchVersion, String versionQualifier) {
		this.groupId = groupId;
		this.artefactId = artefactId;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.patchVersion = patchVersion;
		this.versionQualifier = versionQualifier;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtefactId() {
		return artefactId;
	}

	public Integer getMajorVersion() {
		return majorVersion;
	}

	public Integer getMinorVersion() {
		return minorVersion;
	}

	public Integer getPatchVersion() {
		return patchVersion;
	}

	public String getVersionQualifier() {
		return versionQualifier;
	}
}
