package pl.wcislo.sbql4j.lang.codegen;

import pl.wcislo.sbql4j.lang.codegen.interpreter.CodeGenInterpreter;
import pl.wcislo.sbql4j.lang.codegen.noqres.CodeGenNoQres;
import pl.wcislo.sbql4j.lang.codegen.nostacks.CodeGenNoStacks;
import pl.wcislo.sbql4j.lang.codegen.simple.CodeGenSimple;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4oFactory.CodeGenTypeDB4O;

public class CodeGeneratorFactory {
	public enum CodeGenType {
		INTERPRETER,
		SIMPLE,
		NO_QRES,
		NO_STACKS
	}
	
	public static CodeGenerator createDefaultCodeGenerator() {
		return null;
	}
	
	public static CodeGenerator createCodeGenerator(CodeGenType type, CodeGenTypeDB4O db4oType) {
		if(type == CodeGenType.INTERPRETER) {
			return new CodeGenInterpreter(db4oType);
		} else if(type == CodeGenType.SIMPLE) {
			return new CodeGenSimple(db4oType);
		} else if(type == CodeGenType.NO_QRES) {
			return new CodeGenNoQres(db4oType);
		} else if(type == CodeGenType.NO_STACKS) {
			return new CodeGenNoStacks(db4oType);
		}
		
		return null;
	}
}
