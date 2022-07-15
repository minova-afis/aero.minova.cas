package aero.minova.cas.service;

import aero.minova.cas.storage.lowlevel.AbstractStorageLowLevel;
import aero.minova.cas.storage.lowlevel.StorageLowLevelMetaData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AttachmentsService {
	private final AbstractStorageLowLevel storage;

	@PostConstruct
	private void init() {
		storage.open();
	}

	public StorageLowLevelMetaData store(String key, InputStream sourceStream) {
		return storage.store(key, sourceStream);
	}

	public Collection<StorageLowLevelMetaData> listAttachments() {
		return storage.listAll();
	}

	public void remove(String key) {
		storage.delete(key);
	}

	public boolean retrieve(String key, OutputStream outputStream) {
		return storage.retrieve(key, outputStream);
	}
}
