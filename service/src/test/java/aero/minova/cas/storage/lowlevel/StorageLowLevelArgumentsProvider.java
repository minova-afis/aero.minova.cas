package aero.minova.cas.storage.lowlevel;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

class StorageLowLevelArgumentsProvider implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
		return Stream.of(
				Arguments.of(new InMemoryStorageLowLevel()),
				Arguments.of(new AwsStorageLowLevel(
						AwsStorageLowLevelConfig.builder()
								.storageAwsAccessKey(System.getProperty("AWS_ACCESS_KEY"))
								.storageAwsSecretAccessKey(System.getProperty("AWS_SECRET_ACCESS_KEY"))
								.storageBucketName("aerominovacaspluginattachmentsdevtest")
								.storageAwsEndpointOverride("https://s3-eu-central-1.ionoscloud.com")
								.build()
				)));
	}
}
