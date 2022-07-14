package aero.minova.cas.exceptions;

public class StorageException extends RuntimeException {
	public StorageException(String message) {
		super(message);
	}

	public StorageException(Exception exception) {
		super(exception);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

}
