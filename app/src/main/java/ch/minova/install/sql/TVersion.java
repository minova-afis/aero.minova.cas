package ch.minova.install.sql;

public class TVersion {
	int keylong;
	String keytext;
	String modulname;
	int majorversion;
	int minorversion;
	int patchlevel;
	int buildnumber;
	String lastuser;
	String date;
	int lastaction;
	String dataname;

	public TVersion(final int keylong, final String keytext, final String modulname, final int majorversion, final int minorversion, final int patchlevel,
			final int buildnumber, final String lastuser, final String date, final int lastaction) {
		this.keylong = keylong;
		this.keytext = keytext;
		this.modulname = modulname;
		this.majorversion = majorversion;
		this.minorversion = minorversion;
		this.patchlevel = patchlevel;
		this.buildnumber = buildnumber;
		this.lastuser = lastuser;
		this.date = date;
		this.lastaction = lastaction;
	}

	public String getMasks() {
		return this.dataname;
	}

	public void setMasks(final String masks) {
		this.dataname = masks;
	}

	public int getKeylong() {
		return this.keylong;
	}

	public void setKeylong(final int keylong) {
		this.keylong = keylong;
	}

	public String getKeytext() {
		return this.keytext;
	}

	public void setKeytext(final String keytext) {
		this.keytext = keytext;
	}

	public String getModulname() {
		return this.modulname;
	}

	public void setModulname(final String modulname) {
		this.modulname = modulname;
	}

	public int getMajorversion() {
		return this.majorversion;
	}

	public void setMajorversion(final int majorversion) {
		this.majorversion = majorversion;
	}

	public int getMinorversion() {
		return this.minorversion;
	}

	public void setMinorversion(final int minorversion) {
		this.minorversion = minorversion;
	}

	public int getPatchlevel() {
		return this.patchlevel;
	}

	public void setPatchlevel(final int patchlevel) {
		this.patchlevel = patchlevel;
	}

	public long getBuildnumber() {
		return this.buildnumber;
	}

	public void setBuildnumber(final int buildnumber) {
		this.buildnumber = buildnumber;
	}

	public String getLastuser() {
		return this.lastuser;
	}

	public void setLastuser(final String lastuser) {
		this.lastuser = lastuser;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(final String date) {
		this.date = date;
	}

	public int getLastaction() {
		return this.lastaction;
	}

	public void setLastaction(final int lastaction) {
		this.lastaction = lastaction;
	}
}