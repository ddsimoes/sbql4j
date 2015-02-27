package pl.wcislo.sbql4j.lang.parser; 
  
import java_cup.runtime.Symbol;
import pl.wcislo.sbql4j.lang.parser.SBQLSymbolFactory;

import static pl.wcislo.sbql4j.lang.parser.ParserSym.*;
 


%%
%{ 
	private StringBuffer str;
//	private String identifier;
	private SBQLSymbolFactory sf = new SBQLSymbolFactory();
	
	private Token createToken(int id) {
		return createToken(id, yytext());
	}
	
	private Token createToken(int id, Object o) {
		return new Token(id, yyline, yycolumn, new SyntaxTreeNode(o, yychar));
	}
	
	public int getPos() {
		return zzMarkedPos;	
	}
%}
 
%public
%class Lexer 
%cup
%line 
%column
%char
%eofval{
	return createToken(ParserSym.EOF);
%eofval}

INTEGER = [0-9]+
BOOLEAN = true|false
IDENTIFIER = [_a-zA-Z][_0-9a-zA-Z]*
SPEC_IDENTIFIER = \$index

//JAVA_PARAM = :[_a-zA-Z][0-9a-zA-Z]*
DOUBLE = [0-9]+\.[0-9]+
STRING = [\"][^\"]*[\"]
CHAR = [\'][^\"][\']
//WHITESPACE = [ \t\r\n\v\f]+
LineTerminator = \r|\n|\r\n 
WHITESPACE = {LineTerminator} | [ \t\f]


//METHOD = [_a-zA-Z][_0-9a-zA-Z]*{WHITESPACE}*[\(]

%state IDENTIFIER
  
%% 
 
<YYINITIAL> {
	"+"						{ return createToken(PLUS				); }
	"-"						{ return createToken(MINUS				); }
	"*"						{ return createToken(MULTIPLY			); }
	"/"						{ return createToken(DIVIDE				); } 
	"%"						{ return createToken(MODULO				); } 
	"=="					{ return createToken(EQUALS				); }
	"!="					{ return createToken(NOT_EQUALS			); }
	"("						{ return createToken(LEFT_ROUND_BRACKET	); }
	")"						{ return createToken(RIGHT_ROUND_BRACKET); }  
	"{"						{ return createToken(LEFT_CURLY_BRACKET	); }
	"}"						{ return createToken(RIGHT_CURLY_BRACKET); }
	"["						{ return createToken(LEFT_BOX_BRACKET	); }
	"]"						{ return createToken(RIGHT_BOX_BRACKET	); }
	">"						{ return createToken(MORE				); }
	"OR"|"or"|"\|\|"		{ return createToken(OR					); }
	"AND"|"and"|"&&"		{ return createToken(AND				); }
	"SUM"|"sum"				{ return createToken(SUM				); } 
	"AVG"|"avg"				{ return createToken(AVG				); } 
	"UNIQUE"|"unique"		{ return createToken(UNIQUE				); } 
	"UNION"|"union"			{ return createToken(UNION				); } 
	"MIN"|"min"				{ return createToken(MIN				); }
	"MAX"|"max"				{ return createToken(MAX				); } 
	"COUNT"|"count"			{ return createToken(COUNT				); }
	"AS"|"as"				{ return createToken(AS					); }
	"GROUP AS"|"group as" 	{ return createToken(GROUP_AS			); }
	"BAG"|"bag"				{ return createToken(BAG				); }
	"SEQUENCE"|"sequence"	{ return createToken(SEQUENCE			); }
	".."  					{ return createToken(RANGE				); }
	"."						{ return createToken(DOT				); }
	"<"						{ return createToken(LESS				); }
	">="					{ return createToken(MORE_OR_EQUAL		); }
	"<="					{ return createToken(LESS_OR_EQUAL		); }
	","						{ return createToken(COMA				); }
	"IN"|"in"				{ return createToken(IN					); }
	"WHERE"|"where"			{ return createToken(WHERE				); } 
	"deref"|"DEREF"			{ return createToken(DEREF				); }
	"EXISTS"|"exists"		{ return createToken(EXISTS				); }
	"NOT"|"not"|"!"			{ return createToken(NOT				); }
	"MINUS"|"minus" 		{ return createToken(MINUS_FUNCTION		); }
	"STRUCT"|"struct" 		{ return createToken(STRUCT				); }
	"INTERSECT"|"intersect" { return createToken(INTERSECT			); }
	"JOIN"|"join" 	 		{ return createToken(JOIN				); }
	"ALL"|"all" 			{ return createToken(FORALL				); }
	"ANY"|"any"  			{ return createToken(FORANY				); }
	"ORDER BY"|"order by" 	{ return createToken(ORDER_BY			); }
	"ASC"|"asc" 		 	{ return createToken(GROUPBY_ASC		); }
	"DESC"|"desc" 		 	{ return createToken(GROUPBY_DESC		); }
	";"					 	{ return createToken(SEMICOLON			); }  
	"close by"|"CLOSE BY" 	{ return createToken(CLOSE_BY			); }
	"foreach"|"FOREACH"  	{ return createToken(FOREACH			); }
	"new"|"NEW"  			{ return createToken(NEW				); }
	":"  					{ return createToken(COLON				); }
	"?"  					{ return createToken(QUESTION_MARK		); }
	"INSTANCEOF"|"instanceof" { return createToken(INSTANCEOF		); }
	"USING"|"using" 		{ return createToken(USING		); }
	
	
	
	

	{WHITESPACE} { }
	{STRING} {return createToken(STRING_LITERAL, yytext().substring(1,yytext().length()-1)) ; }
	{CHAR}	 {return createToken(CHAR_LITERAL, yytext().charAt(1)) ; }
		
	{INTEGER} {
		int val;
		try {
			val = Integer.parseInt(yytext());
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return createToken(INTEGER_LITERAL, new Integer(val));
	}
	{DOUBLE} {
		double val;
		try {
			val = Double.parseDouble(yytext());
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return createToken(DOUBLE_LITERAL, new Double(val));
	}
	{BOOLEAN} {
		boolean val;
		try {
			val = Boolean.parseBoolean(yytext());
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return createToken(BOOLEAN_LITERAL, new Boolean(val));
	} 
	 

	{IDENTIFIER} {  
		Token t = createToken(ParserSym.IDENTIFIER);
		return t;
	}
	
	{SPEC_IDENTIFIER} {
		Token t = createToken(ParserSym.IDENTIFIER);
		return t;
	}

/*	 
	{JAVA_PARAM} { return createToken(yyline, yycolumn, zzCurrentPos, JAVA_PARAM, new SyntaxTreeNode(yytext(), zzCurrentPos)); }
*/	
//	{METHOD} { return createToken(yyline, yycolumn, zzCurrentPos, METHOD, yytext()); }
}

/*
<IDENTIFIER> {
	{WHITESPACE} { }
	"ASC"|"asc" {
		yybegin(YYINITIAL);
		return createToken(yyline, yycolumn, zzCurrentPos, GROUPBY_ASC, new SyntaxTreeNode(yytext(), zzCurrentPos));
	} 
	"DESC"|"desc" {
		yybegin(YYINITIAL);
//		System.out.println("DESC");
		return createToken(yyline, yycolumn, zzCurrentPos, GROUPBY_DESC, new SyntaxTreeNode(yytext(), zzCurrentPos));
	}
	
	.|\n {
		yypushback(yytext().length()); 
		yybegin(yyline, yycolumn, zzCurrentPos, YYINITIAL); 
	}
}
*/