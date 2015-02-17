package pl.wcislo.sbql4j.lang.db4o.codegen;

import pl.wcislo.sbql4j.lang.codegen.CodeGenerator;
import pl.wcislo.sbql4j.lang.db4o.codegen.interpreter.CodeGenDb4oInterpreter;
import pl.wcislo.sbql4j.lang.db4o.codegen.nostacks.CodeGenDb4oNoStacks;

public class CodeGeneratorDb4oFactory {
	
	public enum CodeGenTypeDB4O {
		INTERPRETER,
		NO_STACKS
	}
	
	public static CodeGeneratorDb4o createDefaultCodeGenerator() {
		return null;
	}
	
	/**
	 * 
	 * @param db4oType
	 * @param parent
	 * @param index number of db4o query in current sbql4j query
	 * @return
	 */
	public static CodeGeneratorDb4o createCodeGenerator(CodeGenTypeDB4O db4oType, CodeGenerator parent, int index) {
		if(db4oType == CodeGenTypeDB4O.INTERPRETER) {
			return new CodeGenDb4oInterpreter(parent);
		} else if(db4oType == CodeGenTypeDB4O.NO_STACKS) {
			return new CodeGenDb4oNoStacks(parent, index);
		}
		return null;
	}
}
