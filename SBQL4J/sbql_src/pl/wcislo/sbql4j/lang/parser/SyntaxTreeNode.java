package pl.wcislo.sbql4j.lang.parser;

/**
 * Przechowuje wartosci tworzonego tokena
 * @author Emil
 *
 */
public class SyntaxTreeNode {
	public final Object value;
	public final int loc;
	
	public SyntaxTreeNode(Object value, int loc) {
		super();
		this.value = value;
		this.loc = loc;
	}
}
