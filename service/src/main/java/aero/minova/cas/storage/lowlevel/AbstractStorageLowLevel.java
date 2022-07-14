package aero.minova.cas.storage.lowlevel;

import aero.minova.cas.exceptions.StorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractStorageLowLevel {
	protected static final String NOT_IMPLEMENTED = "not implemented";

	public void open() {}

	public void close() {}

	/**
	 *
	 * @param key ID des Attachments
	 * @param outputStream Inhalt des Attachments wird in diesen Stream geschrieben.
	 */
	public abstract void retrieve(String key, OutputStream outputStream);

	public abstract StorageLowLevelMetaData store(String key, InputStream sourceStream);

	public boolean delete(String key) {
		throw new StorageException(NOT_IMPLEMENTED);
	}

	public boolean deleteBucket() {
		throw new StorageException(NOT_IMPLEMENTED);
	}

	public Collection<StorageLowLevelMetaData> listAll() {
		return list(Optional.empty());
	}

	public Collection<StorageLowLevelMetaData> list(Optional<String> prefix) {
		throw new StorageException(NOT_IMPLEMENTED);
	}
}
