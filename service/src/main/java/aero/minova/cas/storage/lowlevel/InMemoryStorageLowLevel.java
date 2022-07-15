package aero.minova.cas.storage.lowlevel;

import aero.minova.cas.exceptions.StorageException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryStorageLowLevel extends AbstractStorageLowLevel {
	private final Map<String, byte[]> dataMap = new ConcurrentHashMap<>();
	private final Map<String, StorageLowLevelMetaData> metaDataMap = new ConcurrentHashMap<>();

	@Override
	public StorageLowLevelMetaData store(String key, InputStream sourceStream) {
		try {
			final var now = ZonedDateTime.now();
			final int size = sourceStream.available();

			dataMap.put(key, sourceStream.readAllBytes());
			metaDataMap.put(key, StorageLowLevelMetaData.builder()
					.fileName(key)
					.createdTimestamp(now)
					.lastModifiedTimestamp(now)
					.fileSize(size)
					.build()
			);
			return metaDataMap.get(key);
		} catch (IOException e) {
			throw new StorageException(e);
		}

	}

	@Override
	public Collection<StorageLowLevelMetaData> listAll() {
		return metaDataMap.values();
	}

	@Override
	public boolean delete(String key) {
		boolean result = true;

		if (null == metaDataMap.remove(key))
			result = false;
		if (null == dataMap.remove(key))
			result = false;

		return result;
	}

	@Override
	public boolean retrieve(String key, OutputStream outputStream) {
		if (!dataMap.containsKey(key))
			return false;

		try {
			outputStream.write(dataMap.get(key));
			return true;
		} catch (IOException e) {
			throw new StorageException("Unable to retrieve stream for key '" + key + "': " + e.getMessage(), e);
		}
	}

	@Override
	public boolean deleteBucket() {
		dataMap.clear();
		metaDataMap.clear();

		return true;
	}
}
