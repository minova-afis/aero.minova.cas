package aero.minova.cas.storage.lowlevel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class StorageLowLevelTest {
	private static final byte[] TEST_CONTENT = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.".getBytes(
			StandardCharsets.UTF_8);
	private static final String TEST_KEY = String.format("%s-key-%s", StorageLowLevelTest.class.getSimpleName(), System.currentTimeMillis());

	@ParameterizedTest
	@ArgumentsSource(StorageLowLevelArgumentsProvider.class)
	void testCRUD(AbstractStorageLowLevel storage) {
		try {
			storage.open();
			Collection<StorageLowLevelMetaData> metaDataList;

			metaDataList = storage.listAll();
			assertThat(metaDataList)
					.isEmpty();

			ByteArrayInputStream bis = new ByteArrayInputStream(TEST_CONTENT);
			StorageLowLevelMetaData metaData = storage.store(TEST_KEY, bis);

			assertThat(metaData)
					.isNotNull()
					.hasFieldOrPropertyWithValue("fileName", TEST_KEY)
					.hasFieldOrPropertyWithValue("fileSize", 0L + TEST_CONTENT.length);

			metaDataList = storage.listAll();
			assertThat(metaDataList)
					.hasSize(1);
			assertThat(metaDataList.toArray()[0])
					.isNotNull()
					.hasFieldOrPropertyWithValue("fileName", TEST_KEY)
					.hasFieldOrPropertyWithValue("fileSize", 0L + TEST_CONTENT.length)
					.hasFieldOrProperty("lastModifiedTimestamp");

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			storage.retrieve(TEST_KEY, bos);

			assertThat(bos.toByteArray())
					.isEqualTo(TEST_CONTENT);

			assertThat(storage.delete(TEST_KEY))
					.isTrue();
			metaDataList = storage.listAll();
			assertThat(metaDataList)
					.isEmpty();

			assertThat(storage.delete(TEST_KEY))
					.isFalse();
		} finally {
			storage.close();
		}
	}

	@Test
	// Rainer: IONOS-cloud doesn't properly create a bucket using the AWS S3 SDK
	void testDeleteBucket() {
		AbstractStorageLowLevel storage = new InMemoryStorageLowLevel();
		try {
			storage.open();

			final int numberOfFilesAtStart = storage.listAll().size();

			storage.store(TEST_KEY, new ByteArrayInputStream(TEST_CONTENT));

			assertThat(storage.listAll())
					.hasSize(numberOfFilesAtStart + 1);

			assertThat(storage.deleteBucket())
					.isTrue();

			assertThat(storage.listAll())
					.isEmpty();
		} finally {
			storage.close();
		}
	}
}
