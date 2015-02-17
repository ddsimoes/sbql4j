package pl.wcislo.sbql4j.exception;


public class ParserException extends RuntimeException {

	private int line;
	private int column;
	private int pos;
	private String token;


	public ParserException(String msg) {
    	super(msg);
    }

    public ParserException(String msg, int line, int column, int pos, String token) {
    	this(msg+" at line="+line+", column="+column+" position="+pos+" token="+token);
    	this.line = line;
    	this.column = column;
    	this.pos = pos;
    	this.token = token;
    }

    public ParserException(Throwable ex) {
		super(ex);
    }
	
    public int getLine() {
		return line;
	}
    public int getColumn() {
		return column;
	}
    public int getPos() {
		return pos;
	}
    public String getToken() {
		return token;
	}
}