package pl.wcislo.sbql4j.xml.parser;

/**
 * @author emil.wcislo 
 */
public class ParserException extends Exception {
	private static final long serialVersionUID = 5313404469259971642L;

	public ParserException() {
		super();
	}

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}

}
