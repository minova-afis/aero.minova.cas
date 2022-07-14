package aero.minova.cas.controller;

import aero.minova.cas.service.AttachmentsService;
import aero.minova.cas.exceptions.StorageException;
import aero.minova.cas.storage.lowlevel.StorageLowLevelMetaData;
import cas.openapi.api.server.api.AttachmentsApi;
import cas.openapi.api.server.model.AllAttachmentsResponse;
import cas.openapi.api.server.model.AttachmentMetaEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AttachmentsController implements AttachmentsApi {
	private final AttachmentsService service;

	@Override
	public ResponseEntity<AllAttachmentsResponse> listAllAttachments() {
		return ResponseEntity.ok(new AllAttachmentsResponse().attachments(
				service.listAttachments().stream()
						.map(AttachmentsController::convert)
						.filter(Objects::nonNull)
						.collect(Collectors.toList())
		));
	}

	@Override
	public ResponseEntity<AttachmentMetaEntry> createAttachment(Resource body) {
		try {
			StorageLowLevelMetaData metaData = service.store(UUID.randomUUID().toString(), body.getInputStream());

			return ResponseEntity
					.created(generateResourceUri(UUID.fromString(metaData.getFileName())))
					.body(convert(metaData));
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public ResponseEntity<AttachmentMetaEntry> overwriteAttachmentById(UUID attachmentId, @Valid Resource body) {
		try {
			return ResponseEntity
					.ok(convert(service.store(attachmentId.toString(), body.getInputStream())));
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public ResponseEntity<Void> removeAttachmentById(UUID attachmentId) {
		service.remove(attachmentId.toString());

		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Resource> retrieveAttachmentById(UUID attachmentId) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		service.retrieve(attachmentId.toString(), out);

		return ResponseEntity.ok(new ByteArrayResource(out.toByteArray()));
	}

	private URI generateResourceUri(UUID id) {
		return ServletUriComponentsBuilder //
				.fromCurrentRequest() //
				.replaceQuery("") //
				.path("/{id}") //
				.buildAndExpand(id) //
				.toUri();
	}

	private static AttachmentMetaEntry convert(StorageLowLevelMetaData metaData) {
		try {
			return new AttachmentMetaEntry()
					.id(UUID.fromString(metaData.getFileName()))
					.fileSize(Integer.valueOf("" + metaData.getFileSize()))
					.lastModified(metaData.getLastModifiedTimestamp() == null ? null : metaData.getLastModifiedTimestamp().toOffsetDateTime());
		} catch (IllegalArgumentException e) {
			log.error("Unable to convert metaData '" + metaData + "' to AttachmentMetaEntry: " + e.getMessage(), e);
			return null;
		}
	}
}