package aero.minova.cas.profiling;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.LongConsumer;

import aero.minova.cas.api.domain.ProfilingResult;
import aero.minova.cas.api.domain.TransactionOverhead;

/**
 * Sammelt pro Request (via ThreadLocal) die für SQL-Ausführung aufgewendete Zeit, um sie von der restlichen (Java-)Laufzeit unterscheiden zu können. Ist
 * Profiling nicht aktiv, kostet jeder instrumentierte Aufruf nur ein {@code ThreadLocal.get()} - es wird keine Zeit gemessen.
 * <p>
 * Mehrere Messungen können auf demselben Thread verschachtelt aktiv sein (z.B. die Gesamt-Transaktion einer XProcedure UND die gerade laufende
 * Einzel-Prozedur darin) - dafür wird ein Stack statt eines einzelnen Werts verwendet. Eine instrumentierte SQL-Zeit wird JEDEM aktuell aktiven Profiler auf
 * dem Stack gutgeschrieben, sodass sowohl die Einzel-Prozedur als auch die Gesamt-Transaktion eine korrekte, unabhängige SQL/Java-Aufteilung erhalten.
 */
public class Profiler {

	@FunctionalInterface
	public interface SqlAction<T> {
		T call() throws SQLException;
	}

	@FunctionalInterface
	public interface Action<T, E extends Exception> {
		T call() throws E;
	}

	private static final ThreadLocal<Deque<Profiler>> STACK = ThreadLocal.withInitial(ArrayDeque::new);
	/**
	 * Während einer Rechteprüfung aktiv (siehe {@link #timePrivilegeCheck(Action)}) - lenkt SQL-Zeit, die intern z.B. über
	 * {@link aero.minova.cas.sql.SqlUtils#convertSqlResultToTable} anfällt, in {@link #privilegeCheckNanos} statt {@link #sqlNanos} um, damit sie nicht
	 * doppelt gezählt wird.
	 */
	private static final ThreadLocal<Boolean> RECORDING_PRIVILEGE_CHECK = ThreadLocal.withInitial(() -> false);

	private final long startNanos = System.nanoTime();
	private long sqlNanos;
	private long privilegeCheckNanos;
	private long connectionAcquisitionNanos;
	private long commitNanos;
	private long queueDispatchNanos;
	private long loggingNanos;
	private long rowConversionNanos;

	/**
	 * Startet eine neue, unabhängige Messung und legt sie auf den Profiling-Stack dieses Threads. Eine bereits laufende Messung (z.B. für die gesamte
	 * XProcedure-Transaktion) bleibt unberührt und sammelt weiterhin mit.
	 */
	public static Profiler push() {
		Profiler profiler = new Profiler();
		STACK.get().push(profiler);
		return profiler;
	}

	public static boolean isActive() {
		return !STACK.get().isEmpty();
	}

	/**
	 * Liefert die zuletzt gepushte (aktuell "innerste") Messung, ohne sie zu beenden.
	 */
	public static Profiler current() {
		return STACK.get().peek();
	}

	/**
	 * Muss - passend zu jedem {@link #push()} - in einem finally-Block aufgerufen werden, damit bei einer Exception kein Profiler-Zustand auf dem (von
	 * Tomcat wiederverwendeten) Thread für den nächsten Request bestehen bleibt. Entfernt die zuletzt gepushte Messung.
	 */
	public static void pop() {
		Deque<Profiler> stack = STACK.get();
		if (!stack.isEmpty()) {
			stack.pop();
		}
	}

	public static void recordSqlNanos(long nanos) {
		if (Boolean.TRUE.equals(RECORDING_PRIVILEGE_CHECK.get())) {
			record(nanos, p -> p.privilegeCheckNanos += nanos);
		} else {
			record(nanos, p -> p.sqlNanos += nanos);
		}
	}

	public static void recordConnectionAcquisitionNanos(long nanos) {
		record(nanos, p -> p.connectionAcquisitionNanos += nanos);
	}

	public static void recordCommitNanos(long nanos) {
		record(nanos, p -> p.commitNanos += nanos);
	}

	public static void recordQueueDispatchNanos(long nanos) {
		record(nanos, p -> p.queueDispatchNanos += nanos);
	}

	public static void recordLoggingNanos(long nanos) {
		record(nanos, p -> p.loggingNanos += nanos);
	}

	public static void recordRowConversionNanos(long nanos) {
		record(nanos, p -> p.rowConversionNanos += nanos);
	}

	private static void record(long nanos, java.util.function.Consumer<Profiler> mutator) {
		for (Profiler profiler : STACK.get()) {
			mutator.accept(profiler);
		}
	}

	public static long startTimer() {
		return isActive() ? System.nanoTime() : -1;
	}

	public static void stopTimer(long start) {
		stopTimer(start, Profiler::recordSqlNanos);
	}

	public static void stopTimer(long start, LongConsumer recorder) {
		if (start >= 0) {
			recorder.accept(System.nanoTime() - start);
		}
	}

	public static <T> T timeSql(SqlAction<T> action) throws SQLException {
		long start = startTimer();
		try {
			return action.call();
		} finally {
			stopTimer(start);
		}
	}

	/**
	 * Wie {@link #timeSql(SqlAction)}, misst aber das Holen einer Connection aus dem Pool separat - kann bei ausgeschöpftem Pool erheblich zur Gesamtzeit
	 * einer Transaktion beitragen, ohne einer einzelnen Prozedur zurechenbar zu sein.
	 */
	public static <T> T timeConnectionAcquisition(SqlAction<T> action) throws SQLException {
		long start = startTimer();
		try {
			return action.call();
		} finally {
			stopTimer(start, Profiler::recordConnectionAcquisitionNanos);
		}
	}

	/**
	 * Misst eine Rechteprüfung (z.B. {@code getPrivilegePermissions}) separat von der eigentlichen Prozedur-/View-Ausführung. Jegliche SQL-Zeit, die
	 * innerhalb von {@code action} über die reguläre Instrumentierung anfällt, wird währenddessen nach {@link #privilegeCheckNanos} statt
	 * {@link #sqlNanos} umgeleitet, damit sie nicht doppelt gezählt wird.
	 */
	public static <T, E extends Exception> T timePrivilegeCheck(Action<T, E> action) throws E {
		if (!isActive()) {
			return action.call();
		}
		boolean previous = RECORDING_PRIVILEGE_CHECK.get();
		RECORDING_PRIVILEGE_CHECK.set(true);
		try {
			return action.call();
		} finally {
			RECORDING_PRIVILEGE_CHECK.set(previous);
		}
	}

	/**
	 * Misst synchrones Logging (jeder Logger in diesem Projekt schreibt synchron in Dateien/Konsole - kein Async-Appender), damit sichtbar wird, wie viel
	 * "Java-Zeit" tatsächlich I/O für Log-Dateien statt eigentlicher Anwendungslogik ist.
	 */
	public static void timeLogging(Runnable action) {
		long start = startTimer();
		try {
			action.run();
		} finally {
			stopTimer(start, Profiler::recordLoggingNanos);
		}
	}

	/**
	 * Misst die Java-seitige Umwandlung einer JDBC-ResultSet-Row in eine {@code Row} (Typkonvertierung), getrennt von der reinen JDBC-Abholzeit
	 * ({@code next()}, bereits in {@link #sqlNanos} enthalten).
	 */
	public static <T, E extends Exception> T timeRowConversion(Action<T, E> action) throws E {
		long start = startTimer();
		try {
			return action.call();
		} finally {
			stopTimer(start, Profiler::recordRowConversionNanos);
		}
	}

	public long getTotalMs() {
		return (System.nanoTime() - startNanos) / 1_000_000;
	}

	public long getSqlMs() {
		return sqlNanos / 1_000_000;
	}

	public long getPrivilegeCheckMs() {
		return privilegeCheckNanos / 1_000_000;
	}

	public long getConnectionAcquisitionMs() {
		return connectionAcquisitionNanos / 1_000_000;
	}

	public long getCommitMs() {
		return commitNanos / 1_000_000;
	}

	public long getQueueDispatchMs() {
		return queueDispatchNanos / 1_000_000;
	}

	public long getLoggingMs() {
		return loggingNanos / 1_000_000;
	}

	public long getRowConversionMs() {
		return rowConversionNanos / 1_000_000;
	}

	public long getJavaMs() {
		return getTotalMs() - getSqlMs() - getPrivilegeCheckMs() - getConnectionAcquisitionMs() - getCommitMs() - getQueueDispatchMs() - getLoggingMs()
				- getRowConversionMs();
	}

	public ProfilingResult toProfilingResult() {
		return new ProfilingResult(getSqlMs(), getJavaMs(), getPrivilegeCheckMs(), getLoggingMs(), getRowConversionMs());
	}

	public TransactionOverhead toTransactionOverhead() {
		return new TransactionOverhead(getConnectionAcquisitionMs(), getCommitMs(), getQueueDispatchMs());
	}
}
