package aero.minova.cas.storage.lowlevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Data
public class StorageLowLevelMetaData {
	private String fileName;
	private long fileSize;
	private ZonedDateTime lastModifiedTimestamp;
	private ZonedDateTime createdTimestamp;
}
