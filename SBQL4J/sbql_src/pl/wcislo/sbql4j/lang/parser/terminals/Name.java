package pl.wcislo.sbql4j.lang.parser.terminals;

public class Name extends Terminal {
	public String val;
	public final int pos;

	public Name(String val, int pos) {
		super();
		this.val = val;
		this.pos = pos;
	}
}
