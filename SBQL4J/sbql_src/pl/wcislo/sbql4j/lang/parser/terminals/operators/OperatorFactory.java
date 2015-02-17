package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pl.wcislo.sbql4j.lang.parser.SyntaxTreeNode;
import pl.wcislo.sbql4j.lang.parser.terminals.Operator;

public class OperatorFactory {
	private static final Map<OperatorType, Operator> tmp = new HashMap<OperatorType, Operator>(); 
	static {
		try {
			for(OperatorType operatorType : OperatorType.values()) {
				Operator op = operatorType.clazz.getConstructor(OperatorType.class).newInstance(operatorType);
				tmp.put(operatorType, op);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static final Map<OperatorType, Operator> OPERATOR_INSTANCES = Collections.unmodifiableMap(tmp);
	
	public static Operator getOperator(OperatorType operatorType) {
		return OPERATOR_INSTANCES.get(operatorType);
	}
	
	public static Operator getOperator(SyntaxTreeNode n) {
		String operatorLiteral = (String) n.value;
		return getOperator(operatorLiteral);
	}
	
	public static Operator getOperator(String operatorLiteral) {
//		System.out.println("OperatorFactory.getOperator() operatorLiteral:"+operatorLiteral );
//		for(OperatorType type : OPERATOR_INSTANCES.keySet()) {
//			Operator val = OPERATOR_INSTANCES.get(type);
////			System.out.println(type+" : "+val);
//		}
		for(OperatorType operatorType : OperatorType.values()) {
			if(operatorLiteral != null) {
//				System.out.println("OperatorFactory.getOperator() "+operatorLiteral+" "+operatorType.val);
				if(operatorLiteral.matches(operatorType.val)) {
//					Operator op = OPERATOR_INSTANCES.get(operatorType);
					Operator op;
					try {
						return op = operatorType.clazz.getConstructor(OperatorType.class).newInstance(operatorType);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					System.out.println("FOUND: type: "+operatorType+" val: " +op);
					return null;			
				}
			}
		
		}
		throw new RuntimeException("no operator found: "+operatorLiteral);
//		return null;
	}
	
	public static <E extends Operator> E getOperator(Class<E> clazz) {
		for(OperatorType operatorType : OperatorType.values()) {
			if(operatorType.clazz == clazz) {
				return (E) OPERATOR_INSTANCES.get(clazz);
			}
		}
		return null;
	}
}
