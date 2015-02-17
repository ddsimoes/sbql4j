package pl.wcislo.sbql4j.lang.parser;

import java_cup.runtime.DefaultSymbolFactory;
import java_cup.runtime.Symbol;

public class SBQLSymbolFactory extends DefaultSymbolFactory {

    public Symbol newSymbol(String name ,int id, Symbol left, Symbol right, Object value){
    	
        return new Token(id,left,right,value);
    }
    
//    public Symbol newSymbol(String name, int id, Object value, int location) {
//    	return new Token(-1, -1, location, id, value);
//    }

}
