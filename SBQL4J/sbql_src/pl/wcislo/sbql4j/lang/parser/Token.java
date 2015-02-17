//
//  Token.java
//  Odra
//
//  Created by Michal Lentner on 05-05-01.
//  Copyright 2005 PJIIT. All rights reserved.
//

package pl.wcislo.sbql4j.lang.parser;

import java_cup.runtime.Symbol;

public class Token extends Symbol {
	
	/**
	 * Pozycja tokena od poczatku
	 */
	public int pos;
	
//	public Token(int l, int c, int p, int s) {
//		super(s, l, c);
//		line = l;
//		column = c;
//		pos = p;
//	}
		
    public Token(int id, Symbol left, Symbol right, Object o) {
    	super(id,left.left,right.right,o);
    	complete();
    }
    
    public Token(int id, int l, int r, Object o) {
    	super(id, l, r, o);
    	complete();
    }
    
    private void complete() {
    	Object o = this.value;
    	if(o instanceof SyntaxTreeNode) {
    		SyntaxTreeNode node = (SyntaxTreeNode)o;
    		this.pos = node.loc;
    		this.value = node.value;
    	}
    }
	
//	public Token(int l, int c, int p, int s, Object val) {
//		super(s, l, c, val);
//		if(s == ParserSym.IDENTIFIER) {
//			int x = 5;
//		}
//		
//		line = l;
//		column = c;
//		pos = p;
//	} 
}
