package aero.minova.cas;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.CoreConstants;

/**
 * Logback converter that truncates stack traces after the last frame belonging
 * to a minova class. All Spring/Tomcat/JDK frames below are replaced with a
 * single "... N frames omitted" line.
 */
public class MinovaStackTraceConverter extends ThrowableProxyConverter {

	@Override
	protected String throwableProxyToString(IThrowableProxy tp) {
		StringBuilder sb = new StringBuilder(2048);
		appendThrowable(tp, sb);
		return sb.toString();
	}

	private boolean isCutEnabled() {
		String val = getContext().getProperty("LOGGING_TRACECUT");
		return "true".equalsIgnoreCase(val);
	}

	private void appendThrowable(IThrowableProxy tp, StringBuilder sb) {
		if (tp.getCause() != null) {
			appendThrowable(tp.getCause(), sb);
			sb.append("Wrapped by: ");
		}

		sb.append(tp.getClassName()).append(": ").append(tp.getMessage()).append(CoreConstants.LINE_SEPARATOR);

		StackTraceElementProxy[] frames = tp.getStackTraceElementProxyArray();

		if (!isCutEnabled()) {
			for (StackTraceElementProxy frame : frames) {
				sb.append("\t").append(frame.getSTEAsString()).append(CoreConstants.LINE_SEPARATOR);
			}
			return;
		}

		int lastMinova = -1;
		for (int i = 0; i < frames.length; i++) {
			if (frames[i].getStackTraceElement().getClassName().contains("minova")) {
				lastMinova = i;
			}
		}

		int limit = lastMinova >= 0 ? lastMinova + 1 : frames.length;
		for (int i = 0; i < limit; i++) {
			sb.append("\t").append(frames[i].getSTEAsString()).append(CoreConstants.LINE_SEPARATOR);
		}

		int omitted = frames.length - limit;
		if (omitted > 0) {
			sb.append("\t... ").append(omitted).append(" frames omitted").append(CoreConstants.LINE_SEPARATOR);
		}
	}
}
