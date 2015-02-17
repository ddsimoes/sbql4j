package pl.wcislo.sbql4j.exception;

public class SBQLException extends RuntimeException {

	public SBQLException() {
		super();
	}

	public SBQLException(String message, Throwable cause) {
		super("SBQL Error: "+message, cause);
	}

	public SBQLException(String message) {
		super("SBQL Error: "+message);
	}

	public SBQLException(Throwable cause) {
		super(cause);
	}
}
