package ch.minova.install.setup.copyfile;

public class DirCopy {
	private String fromdir;
	private String todir;

	public DirCopy(final String fromdir2, final String todir2) {
		setFromdir(fromdir2);
		setTodir(todir2);
	}

	public String getFromdir() {
		return this.fromdir;
	}

	public void setFromdir(final String fromdir) {
		this.fromdir = fromdir;
	}

	public String getTodir() {
		return this.todir;
	}

	public void setTodir(final String todir) {
		this.todir = todir;
	}
}