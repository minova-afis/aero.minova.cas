package domain;

import java.util.List;

public class ErrorMessage {
	private String detailsMessage;
	private String cause;
	private List<String> trace;

	public void setErrorMessage(Exception e) {
		this.detailsMessage = e.getMessage();
		this.cause = e.getCause().toString();
	}

	public String getDetailsMessage() {
		return detailsMessage;
	}

	public String getCause() {
		return cause;
	}

	public List<String> getTrace() {
		return trace;
	}

	public void setTrace(List<String> trace) {
		this.trace = trace;
	}
}
