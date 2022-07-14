package aero.minova.cas.storage.lowlevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Data
public class AwsStorageLowLevelConfig {
	@Value("${application.storage.aws.bucket.name:#{null}}")
	private String storageBucketName;

	@Value("${application.storage.aws.access.key:#{null}}")
	private String storageAwsAccessKey;

	@Value("${application.storage.aws.secret.access.key:#{null}}")
	private String storageAwsSecretAccessKey;

	@Value("${application.storage.aws.region:#{null}}")
	private String storageAwsRegion;

	@Value("${application.storage.aws.endpoint.override:#{null}}")
	private String storageAwsEndpointOverride;
}
