package ch.minova.install.setup.copyfile;

import java.util.HashMap;
import java.util.Vector;

public class CopyFile {
	private final HashMap<String, FileCopy> filemap = new HashMap<String, FileCopy>();
	private final Vector<FileCopy> files = new Vector<FileCopy>();
	private final HashMap<String, DirCopy> dirmap = new HashMap<String, DirCopy>();
	private final Vector<DirCopy> dirs = new Vector<DirCopy>();

	public boolean existsFileCopy(final String filename) {
		if (this.filemap.containsKey(filename.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean existsDirCopy(final String fromdir) {
		if (this.dirmap.containsKey(fromdir.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public FileCopy getFileCopy(final String filename, final String todir, String fromdir, final boolean create) {
		if (this.filemap.containsKey(filename.toLowerCase())) {
			return this.filemap.get(filename.toLowerCase());
		} else {
			if (create) {
				if (fromdir == null) {
					fromdir = "/";
				}
				final FileCopy n = new FileCopy(filename, todir, fromdir);

				if (fromdir.endsWith("/")) {
					this.filemap.put(fromdir.toLowerCase() + filename.toLowerCase(), n);
				} else {
					this.filemap.put(fromdir.toLowerCase() + "/" + filename.toLowerCase(), n);
				}
				this.files.add(n);
				return n;
			}
			return null;
		}
	}

	public DirCopy getDirCopy(final String fromdir, final String todir, final boolean create) {
		if (this.dirmap.containsKey(fromdir.toLowerCase())) {
			return this.dirmap.get(fromdir.toLowerCase());
		} else {
			if (create) {

				final DirCopy n = new DirCopy(fromdir, todir);
				if (!n.getFromdir().endsWith("/")) {
					this.dirmap.put(fromdir.toLowerCase() + "/", n);
				} else {
					this.dirmap.put(fromdir.toLowerCase(), n);
				}
				this.dirs.add(n);
				return n;
			}
			return null;
		}
	}

	public int getDirCopyCount() {
		return this.dirs.size();
	}

	public int getFileCopyCount() {
		return this.files.size();
	}
}