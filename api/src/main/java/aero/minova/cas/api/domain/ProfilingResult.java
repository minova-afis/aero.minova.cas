package aero.minova.cas.api.domain;

import java.io.Serializable;

/**
 * Detaillierte Laufzeit-Aufschlüsselung einer Anfrage, welche nur befüllt wird, falls Profiling aktiv war (siehe {@link Table#isProfile()}).
 */
public class ProfilingResult implements Serializable {
	private static final long serialVersionUID = 202608211200L;

	private long sqlTimeMs;
	private long javaTimeMs;
	/**
	 * Zeit für die Rechteprüfung dieses Items (z.B. {@code getPrivilegePermissions}), separat von der eigentlichen Prozedur-/View-Ausführung.
	 */
	private long privilegeCheckMs;
	/**
	 * Zeit für (synchrones) Logging innerhalb der Anfrage-Verarbeitung - jeder Logger in diesem Projekt schreibt synchron in Dateien/Konsole.
	 */
	private long loggingMs;
	/**
	 * Java-seitige Umwandlung der JDBC-ResultSet-Rows in {@code Row}-Objekte (Typkonvertierung), getrennt von der reinen JDBC-Abholzeit in sqlTimeMs.
	 */
	private long rowConversionMs;
	private long totalTimeMs;
	/**
	 * Nur bei XProcedures befüllt: Zeitanteile, die der gesamten Transaktion statt dieser einzelnen Prozedur zuzurechnen sind. Derselbe (gemeinsame) Wert
	 * steht auf jedem Ergebnis der Transaktion - nicht über alle Ergebnisse aufsummieren.
	 */
	private TransactionOverhead transactionOverhead;

	public ProfilingResult() {}

	public ProfilingResult(long sqlTimeMs, long javaTimeMs, long privilegeCheckMs, long loggingMs, long rowConversionMs) {
		this.sqlTimeMs = sqlTimeMs;
		this.javaTimeMs = javaTimeMs;
		this.privilegeCheckMs = privilegeCheckMs;
		this.loggingMs = loggingMs;
		this.rowConversionMs = rowConversionMs;
		recalculateTotal();
	}

	private void recalculateTotal() {
		this.totalTimeMs = sqlTimeMs + javaTimeMs + privilegeCheckMs + loggingMs + rowConversionMs;
	}

	public long getSqlTimeMs() {
		return sqlTimeMs;
	}

	public void setSqlTimeMs(long sqlTimeMs) {
		this.sqlTimeMs = sqlTimeMs;
		recalculateTotal();
	}

	public long getJavaTimeMs() {
		return javaTimeMs;
	}

	public void setJavaTimeMs(long javaTimeMs) {
		this.javaTimeMs = javaTimeMs;
		recalculateTotal();
	}

	public long getPrivilegeCheckMs() {
		return privilegeCheckMs;
	}

	public void setPrivilegeCheckMs(long privilegeCheckMs) {
		this.privilegeCheckMs = privilegeCheckMs;
		recalculateTotal();
	}

	public long getLoggingMs() {
		return loggingMs;
	}

	public void setLoggingMs(long loggingMs) {
		this.loggingMs = loggingMs;
		recalculateTotal();
	}

	public long getRowConversionMs() {
		return rowConversionMs;
	}

	public void setRowConversionMs(long rowConversionMs) {
		this.rowConversionMs = rowConversionMs;
		recalculateTotal();
	}

	public long getTotalTimeMs() {
		return totalTimeMs;
	}

	public TransactionOverhead getTransactionOverhead() {
		return transactionOverhead;
	}

	public void setTransactionOverhead(TransactionOverhead transactionOverhead) {
		this.transactionOverhead = transactionOverhead;
	}
}
