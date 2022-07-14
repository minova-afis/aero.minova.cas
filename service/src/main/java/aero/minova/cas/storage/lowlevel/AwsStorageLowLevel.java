package aero.minova.cas.storage.lowlevel;

import aero.minova.cas.exceptions.StorageException;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Component
@RequiredArgsConstructor
@Slf4j
public class AwsStorageLowLevel extends AbstractStorageLowLevel {
	private static final DateTimeFormatter AWS_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	protected final AwsStorageLowLevelConfig config;

	private S3Client s3client = null;

	@Override
	public void open() {
		try {
			s3client = client(config);
		} catch (Exception e) {
			throw new StorageException(e);
		}
	}

	@Override
	public void close() {
		if (null != s3client)
			s3client.close();
	}

	@Override
	public StorageLowLevelMetaData store(String key, InputStream sourceStream) {
		checkPrecondition();

		final String errorTemplate = "Unable to store key " + key;
		try {
			StorageLowLevelMetaData metaData = StorageLowLevelMetaData.builder()
					.fileName(key)
					.fileSize(sourceStream.available())
					.build();

			PutObjectResponse response = s3client.putObject(
					PutObjectRequest.builder()
							.bucket(config.getStorageBucketName())
							.key(key)
							.contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
							.contentLength(0L + sourceStream.available())
							.metadata(new HashMap<>())
							.build(),
					RequestBody.fromInputStream(sourceStream, sourceStream.available())
			);
			checkResponse(response.sdkHttpResponse(), errorTemplate);
			return metaData;
		} catch (IOException e) {
			throw new StorageException(errorTemplate + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void retrieve(String key, OutputStream outputStream) {
		checkPrecondition();

		GetObjectRequest request =
				GetObjectRequest.builder().bucket(config.getStorageBucketName()).key(key).build();

		try {
			try (InputStream in =
						 s3client.getObject(request, ResponseTransformer.toBytes()).asInputStream()) {
				IOUtils.copy(in, outputStream);
			}
			outputStream.close();
		} catch (Exception e) {
			throw prepareStorageMinovaException(key, e);
		}
	}

	@Override
	public boolean delete(String key) {
		checkPrecondition();

		try {
			if (null == retrieveMetadata(key))
				return false;

			DeleteObjectRequest request =
					DeleteObjectRequest.builder().bucket(config.getStorageBucketName()).key(key).build();

			s3client.deleteObject(request);
			return true;
		} catch (NoSuchKeyException e) {
			// relax
		} catch (Exception e) {
			throw new StorageException(e);
		}
		return false;
	}

	@Override
	public boolean deleteBucket() {
		checkPrecondition();
		try {
			if (!retrieveBucketMetaData().sdkHttpResponse().isSuccessful())
				return false;

			listAll().forEach(metaData ->
					s3client.deleteObject(
							DeleteObjectRequest.builder()
									.bucket(config.getStorageBucketName())
									.key(metaData.getFileName())
									.build()));

			DeleteBucketRequest deleteRequest =
					DeleteBucketRequest.builder().bucket(config.getStorageBucketName()).build();
			s3client.deleteBucket(deleteRequest);
		} catch (Exception e) {
			throw new StorageException(e);
		}
		return true;
	}

	@Override
	public Collection<StorageLowLevelMetaData> list(Optional<String> prefix) {
		try {
			ListObjectsRequest.Builder builder = ListObjectsRequest.builder()
					.bucket(config.getStorageBucketName());

			if (prefix.isPresent()) {
				if (StringUtils.isNotBlank(prefix.get())) {
					builder.prefix(prefix.get());
				}
			}

			ListObjectsResponse response = s3client.listObjects(builder.build());

			return response.contents().stream()
					.map(AwsStorageLowLevel::convert)
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new StorageException(e);
		}
	}

	// Rainer: Doesn't work with IONOS-cloud S3
	@SuppressWarnings("java:S1144")
	private boolean createBucket() {
		final String errorTemplate = "Error creating storageBucketName '"
				+ config.getStorageBucketName()
				+ "', in storageAwsRegion '"
				+ config.getStorageAwsRegion()
				+ "'";
		try {
			CreateBucketResponse response = s3client.createBucket(
					CreateBucketRequest.builder()
							.bucket(config.getStorageBucketName())
							.build()
			);
			checkResponse(response.sdkHttpResponse(), errorTemplate);
			return true;
		} catch (Exception e) {
			throw new StorageException(errorTemplate + ": " + e.getMessage(), e);
		}
	}

	private void checkResponse(SdkHttpResponse sdkHttpResponse, String errorTemplate) {
		if (sdkHttpResponse.isSuccessful())
			return;

		throw new StorageException(errorTemplate + ": " + sdkHttpResponse.statusCode() + " " + sdkHttpResponse.statusText());
	}

	private static StorageLowLevelMetaData convert(S3Object s3Object) {
		return StorageLowLevelMetaData.builder()
				.fileName(s3Object.key())
				.fileSize(s3Object.size())
				.lastModifiedTimestamp(s3Object.lastModified().atZone(ZoneId.of("UTC")))
				.build();
	}

	private GetBucketLocationResponse retrieveBucketMetaData() {
		GetBucketLocationRequest request =
				GetBucketLocationRequest.builder().bucket(config.getStorageBucketName()).build();

		return s3client.getBucketLocation(request);
	}

	private HeadObjectResponse retrieveMetadata(String key) {
		HeadObjectRequest request =
				HeadObjectRequest.builder().bucket(config.getStorageBucketName()).key(key).build();

		return s3client.headObject(request);
	}

	private StorageException prepareStorageMinovaException(String key, Exception e) {
		return new StorageException("Unable to retrieve key '" + key + "': " + e.getMessage(), e);
	}

	private void checkPrecondition() {
		if (null == s3client)
			throw new StorageException("Storage not properly initialized");
	}

	private S3Client client(AwsStorageLowLevelConfig config) {
		S3ClientBuilder builder = S3Client.builder()
				.credentialsProvider(
						() ->
								new AwsCredentials() {
									@Override
									public String accessKeyId() {
										return config.getStorageAwsAccessKey();
									}

									@Override
									public String secretAccessKey() {
										return config.getStorageAwsSecretAccessKey();
									}
								})
				.serviceConfiguration(
						S3Configuration.builder()
								.checksumValidationEnabled(false)
								.chunkedEncodingEnabled(true)
								.build());
		builder.region(config.getStorageAwsRegion() == null ? Region.AWS_GLOBAL : Region.of(config.getStorageAwsRegion()));
		if (config.getStorageAwsEndpointOverride() != null)
			builder.endpointOverride(URI.create(config.getStorageAwsEndpointOverride()));

		return builder.build();
	}
}
