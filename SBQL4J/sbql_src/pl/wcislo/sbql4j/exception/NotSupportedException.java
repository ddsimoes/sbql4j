package pl.wcislo.sbql4j.exception;

import pl.wcislo.sbql4j.tools.javac.code.Type;

public class NotSupportedException extends RuntimeException {

	private Type notSupportedType;
	
	public Type getNotSupportedType() {
		return notSupportedType;
	}

	public NotSupportedException() {
	}

	public NotSupportedException(String message, Type notSupportedType) {
		super(message);
		this.notSupportedType = notSupportedType;

	}

	public NotSupportedException(Throwable cause) {
		super(cause);
	}

	public NotSupportedException(String message, Throwable cause) {
		super(message, cause);
	}

}
