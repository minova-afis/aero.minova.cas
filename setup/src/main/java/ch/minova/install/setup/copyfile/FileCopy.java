package ch.minova.install.setup.copyfile;

public class FileCopy {
	private String filename;
	private String todir;
	private String fromdir;

	public FileCopy(final String filename2, final String todir2, final String fromdir2) {
		setFilename(filename2);
		setTodir(todir2);
		setFromdir(fromdir2);
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public String getTodir() {
		return this.todir;
	}

	public void setTodir(final String todir) {
		this.todir = todir;
	}

	public String getFromdir() {
		return this.fromdir;
	}

	public void setFromdir(final String fromdir) {
		this.fromdir = fromdir;
	}
}