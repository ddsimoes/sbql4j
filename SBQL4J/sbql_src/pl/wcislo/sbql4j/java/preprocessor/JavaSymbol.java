package pl.wcislo.sbql4j.java.preprocessor;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;


public class JavaSymbol extends ComplexSymbol {
  private int line;
  private int column;

  public JavaSymbol(int type, int line, int column) {
    this(type, line, column, null, null, null);
  }

  public JavaSymbol(int type, int line, int column, Object value) {
    this(type, line, column, null, null, value);
  }

  public JavaSymbol(int type, int line, int column, Symbol left, Symbol right, Object value) {
    super("", type, left, right, value);
    this.line = line;
    this.column = column;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public String toString() {   
    return "line "+line+", column "+column+", sym: "+sym+(value == null ? "" : (", value: '"+value+"'"));
  }
  
}
