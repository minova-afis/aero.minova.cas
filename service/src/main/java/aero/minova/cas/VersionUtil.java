package aero.minova.cas;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aero.minova.cas.api.domain.VersionResponse;
import aero.minova.cas.service.mdi.Main;

public class VersionUtil {

	private VersionUtil() {}

	private static Pattern VERSION_REGEX = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)[-.]?([A-Z]+)?");

	public static Properties getVersion() throws IOException {
		Properties properties = new Properties();
		properties.load(Main.class.getResourceAsStream("/pom.properties"));
		return properties;
	}

	public static String getVersionString() throws IOException {
		Properties properties = getVersion();
		return properties.getProperty("groupId") + "." + properties.getProperty("artifactId") + "-" + properties.getProperty("version");
	}

	public static VersionResponse getVersionResponse() throws IOException {
		Properties properties = getVersion();

		Matcher matcher = VERSION_REGEX.matcher(properties.getProperty("version"));
		matcher.matches();

		VersionResponse response = new VersionResponse(properties.getProperty("groupId"), //
				properties.getProperty("artifactId"), //
				Integer.valueOf(matcher.group(1)), //
				Integer.valueOf(matcher.group(2)), //
				Integer.valueOf(matcher.group(3)), //
				matcher.groupCount() == 4 ? matcher.group(4) : null);

		return response;
	}

}
