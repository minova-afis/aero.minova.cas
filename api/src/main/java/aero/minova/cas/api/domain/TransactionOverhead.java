package aero.minova.cas.api.domain;

import java.io.Serializable;

/**
 * Zeitanteile einer XProcedure-Transaktion, die keiner einzelnen Prozedur zugeordnet werden können, weil sie nur einmal für die gesamte Transaktion anfallen
 * (Connection-Aufbau, Commit, Nachrichtenversand über den QueueService). Wird bei einer XProcedure auf {@link ProfilingResult#getTransactionOverhead()} jedes
 * einzelnen Ergebnisses mit demselben (gemeinsamen) Wert befüllt - darf daher NICHT über alle Ergebnisse aufsummiert werden.
 */
public class TransactionOverhead implements Serializable {
	private static final long serialVersionUID = 202608211300L;

	private long connectionAcquisitionMs;
	private long commitMs;
	private long queueDispatchMs;

	public TransactionOverhead() {
	}

	public TransactionOverhead(long connectionAcquisitionMs, long commitMs, long queueDispatchMs) {
		this.connectionAcquisitionMs = connectionAcquisitionMs;
		this.commitMs = commitMs;
		this.queueDispatchMs = queueDispatchMs;
	}

	public long getConnectionAcquisitionMs() {
		return connectionAcquisitionMs;
	}

	public void setConnectionAcquisitionMs(long connectionAcquisitionMs) {
		this.connectionAcquisitionMs = connectionAcquisitionMs;
	}

	public long getCommitMs() {
		return commitMs;
	}

	public void setCommitMs(long commitMs) {
		this.commitMs = commitMs;
	}

	public long getQueueDispatchMs() {
		return queueDispatchMs;
	}

	public void setQueueDispatchMs(long queueDispatchMs) {
		this.queueDispatchMs = queueDispatchMs;
	}

	public long getTotalMs() {
		return connectionAcquisitionMs + commitMs + queueDispatchMs;
	}
}
