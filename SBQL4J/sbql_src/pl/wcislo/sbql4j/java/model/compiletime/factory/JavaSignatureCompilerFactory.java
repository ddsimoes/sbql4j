package pl.wcislo.sbql4j.java.model.compiletime.factory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

import pl.wcislo.sbql4j.exception.NotSupportedException;
import pl.wcislo.sbql4j.java.model.compiletime.ClassSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.ConstructorSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Db4oConnectionSignature;
import pl.wcislo.sbql4j.java.model.compiletime.MethodSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.lang.xml.XMLMetadata.SourceType;
import pl.wcislo.sbql4j.lang.xml.signature.XMLSignatureProducer;
import pl.wcislo.sbql4j.tools.javac.code.Attribute;
import pl.wcislo.sbql4j.tools.javac.code.Attribute.Constant;
import pl.wcislo.sbql4j.tools.javac.code.Flags;
import pl.wcislo.sbql4j.tools.javac.code.Scope;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Attribute.Compound;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.VarSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Type.ArrayType;
import pl.wcislo.sbql4j.tools.javac.code.Type.ClassType;
import pl.wcislo.sbql4j.tools.javac.code.Type.TypeVar;
import pl.wcislo.sbql4j.tools.javac.code.Type.WildcardType;
import pl.wcislo.sbql4j.tools.javac.util.Pair;
import sbql4jx.lang.model.element.ElementKind;
import sbql4jx.lang.model.type.TypeKind;

public class JavaSignatureCompilerFactory extends JavaSignatureFactory {
//	private static final Log log = LogFactory.getLog(JavaSignatureCompilerFactory.class);
	
	private static JavaSignatureCompilerFactory instance;
	
	public ValueSignature VOID_VAL_SIG = new ValueSignature(ClassTypes.getInstance().voidType, SCollectionType.NO_COLLECTION, null, null);
	public ClassSignature VOID_CLASS_SIG = new ClassSignature(ClassTypes.getInstance().voidType, null, null, null);
	
	private JavaSignatureCompilerFactory() {}
	
	public static JavaSignatureCompilerFactory getInstance() {
		if(instance == null) {
			instance = new JavaSignatureCompilerFactory();
		}
		return instance;
	}
	
	@Override
	public ValueSignature createValueSignature(Type javaType,
			boolean staticMembersOnly)
			throws NotSupportedException {
		return (ValueSignature) createSignature(javaType, staticMembersOnly, new ArrayList<Attribute.Compound>());
	}
	
	@Override
	public Signature createSignature(Type javaType, 
			boolean staticMembersOnly, 
			List<Compound> annotationList) throws NotSupportedException {
		if(javaType == null) return null;
		ClassTypes cTypes = ClassTypes.getInstance();
		if(javaType == cTypes.voidType) return VOID_VAL_SIG;
		
		//zamieniamy typ prosty na wrapper
		
		boolean primitiveType = false;
		if(javaType.isPrimitive()) {
			javaType = cTypes.checkPrimitiveType(javaType);
			primitiveType = true;
		}
		SCollectionType colType = SCollectionType.NO_COLLECTION;
		if(javaType instanceof ClassType) {
			ClassType javaClassType = (ClassType)javaType;
			if(cTypes.isSubClass(javaClassType, cTypes.listType)) {
				colType = SCollectionType.SEQUENCE;
			} else if(cTypes.isSubClass(javaClassType, cTypes.collectionType)) {
				colType = SCollectionType.BAG;
			}
			if(colType != SCollectionType.NO_COLLECTION) {
				Type elementType = cTypes.symtab.objectType;
				if(!javaClassType.typarams_field.isEmpty()) {
					elementType = javaClassType.typarams_field.get(0);
				}
				if(elementType.toString().equals("com.db4o.ObjectContainer")) {
					Db4oConnectionSignature vs = new Db4oConnectionSignature(elementType, colType);
					return vs;
				
				} else {
					ValueSignature vs = createValueSignature(elementType, staticMembersOnly);
					if(vs.isCollectionResult()) {
						//error, we support only one-level collecions
						throw new NotSupportedException("Type: "+javaType+" is nested collection, which is not supported", javaType);
					}
					vs.setColType(colType);
					vs.setPrimitiveType(primitiveType);
					return vs;	
				}
				
				
			} else {
				if(javaClassType.toString().equals("com.db4o.ObjectContainer")) {
					Db4oConnectionSignature vs = new Db4oConnectionSignature(javaClassType, colType);
					return vs;
				} else if(javaClassType.toString().equals("pl.wcislo.sbql4j.lang.xml.XMLDataSource")) {
//					File xmlSchema;
					for(Compound ann : annotationList) {
						if(ann.type.toString().equals("pl.wcislo.sbql4j.lang.xml.XMLMetadata")) {
							List<Pair<MethodSymbol,Attribute>> valList = ann.values;
							SourceType metaSourceType = null;
							String metaFileName = null;
							Boolean validateXML = null;
							
							for(Pair<MethodSymbol,Attribute> val : valList) {
								if(val.fst.toString().equals("value()")) {
									if(val.snd instanceof Constant) {
										Constant schemaFileConstant = (Constant) val.snd;
										metaFileName = schemaFileConstant.value.toString();
									}
								}
								if(val.fst.toString().equals("type()")) {
									if(val.snd instanceof pl.wcislo.sbql4j.tools.javac.code.Attribute.Enum) {
										pl.wcislo.sbql4j.tools.javac.code.Attribute.Enum sourceEnum = 
											(pl.wcislo.sbql4j.tools.javac.code.Attribute.Enum) val.snd;
										metaSourceType = SourceType.valueOf(sourceEnum.value.name.toString());
									}
								}
								if(val.fst.toString().equals("validateXML()")) {
									if(val.snd instanceof Constant) {
										Constant schemaFileConstant = (Constant) val.snd;
										Object constVal = schemaFileConstant.value;
										if(constVal instanceof Integer) {
											Integer constInteger = (Integer) constVal;
											if(constInteger == 1) {
												validateXML = true;
											} else {
												validateXML = false;
											}
										}
									}
								}
							}
							if(metaSourceType != null && metaFileName != null && validateXML != null) {
								File schemaFile = new File(metaFileName);
								try {
									XMLSignatureProducer xmlSigProd = new XMLSignatureProducer(metaSourceType, schemaFile, validateXML);
									Signature sig = xmlSigProd.parseSchemas();
									return sig;
								} catch (SAXException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
					XMLSignatureProducer xmlSigProducer;
					return null;
					
				} else {
					List<VarSymbol> fields = getFields(javaClassType, staticMembersOnly);
					List<MethodSymbol> methods = getMethods(javaClassType, staticMembersOnly);
					ValueSignature vs = new ValueSignature(javaClassType, colType, fields, methods);
					vs.setPrimitiveType(primitiveType);
					return vs;	
				}
				
			}
		} else if(javaType instanceof ArrayType) {
			ArrayType javaArrayType = (ArrayType) javaType;
			Type elementType = javaArrayType.elemtype;
			ValueSignature vs = createValueSignature(elementType, staticMembersOnly);
			vs.setArrayType(true);
			if(vs.isCollectionResult()) {
				//error, we support only one-level collecions
				throw new NotSupportedException("Type: "+javaType+" is nested collection, which is not supported", javaType);
			}
			vs.setColType(SCollectionType.SEQUENCE);
			vs.setPrimitiveType(primitiveType);
			return vs;
		} else if(javaType instanceof TypeVar || javaType instanceof WildcardType) {
			//generics not supported - cast to Object
			Type t = cTypes.symtab.objectType;
			ValueSignature vs = createValueSignature(t, staticMembersOnly);
			vs.setPrimitiveType(primitiveType);
			return vs;
		} else {
			throw new NotSupportedException("Type "+javaType.getClass()+": "+javaType+" is not supported", javaType);
		}
	}
	
	
	@Override
	public ClassSignature createClassSignature(ClassType javaType) {
		if(javaType == null) return null;
		ClassTypes cTypes = ClassTypes.getInstance();
//		if(javaType == cTypes.voidType) return VOID_CLASS_SIG;
		javaType = cTypes.checkPrimitiveType(javaType);
		List<VarSymbol> fields = getFields(javaType, true);
		List<MethodSymbol> methods = getMethods(javaType, true);
		List<MethodSymbol> constructors = getConstructors(javaType, true);
		return new ClassSignature(javaType, fields, methods, constructors);	
	}

	@Override
	public MethodSignature createJavaMethodSignature(MethodSymbol method) {
		if(method == null) return null;
		ClassTypes ct = ClassTypes.getInstance();
		Type returnType = method.getReturnType();
		ValueSignature returnTypeSig = createValueSignature(returnType, false);
		ClassSymbol ownerClass = (ClassSymbol)method.owner;
		StructSignature paramsSig = new StructSignature();
		if(method.getParameters() != null) {
			List<VarSymbol> params = method.getParameters();
			for(VarSymbol vs : params) {
//				Type paramT = ct.checkPrimitiveType(vs.type);
				ClassType paramType = ct.checkPrimitiveType(vs.type);
				paramsSig.addField(createValueSignature(paramType, false));
			}
		}
		return new MethodSignature(method, ownerClass, returnTypeSig, paramsSig);
	}
	
	@Override
	public ConstructorSignature createJavaConstructorSignature(MethodSymbol constr) {
		if(constr == null) return null;
		ClassTypes ct = ClassTypes.getInstance();
		Type returnType = constr.getReturnType();
		ValueSignature returnTypeSig = createValueSignature(returnType, false);
		ClassSymbol ownerClass = (ClassSymbol)constr.owner;
		StructSignature paramsSig = new StructSignature();
		if(constr.getParameters() != null) {
			List<VarSymbol> params = constr.getParameters();
			for(VarSymbol vs : params) {
//				Type paramT = ct.checkPrimitiveType(vs.type);
				ClassType paramType = ct.checkPrimitiveType(vs.type);
				paramsSig.addField(createValueSignature(paramType, false));
			}
		}
		return new ConstructorSignature(constr, ownerClass, returnTypeSig, paramsSig);
	}

	@Override
	public ValueSignature createJavaSignature(Type javaType)
			throws NotSupportedException {
		if(javaType == null) return null;
		ClassTypes cTypes = ClassTypes.getInstance();
		return createValueSignature(javaType, false);
//		if(cTypes.isSubClass(javaType, Class.class)) {
//			return createClassSignature(javaType);
//		}
		
//		return null;
	}
	
	private List<Symbol.VarSymbol> getFields(ClassType ct, boolean staticOnly) {
		List<Symbol.VarSymbol> result = new ArrayList<VarSymbol>();
		ClassSymbol cs = (ClassSymbol)ct.tsym;
		Scope scope = cs.members();
		for(Symbol s : scope.getElements()) {
//			log.debug(s.getClass()+" "+s.toString());
			if(s instanceof VarSymbol) {
				VarSymbol vs = (VarSymbol)s;
				if(vs.getKind() != ElementKind.FIELD) continue;
//				log.debug("      "+vs.type);
				boolean isPublic = 0 != (vs.flags_field & Flags.PUBLIC);
				boolean isStatic = 0 != (vs.flags_field & Flags.STATIC);
				if(!isPublic) continue;
				if(staticOnly && !isStatic) continue;
				result.add(vs);
			}
		}
		return result;
	}

	private List<Symbol.MethodSymbol> getMethods(ClassType ct, boolean staticOnly) {
		List<Symbol.MethodSymbol> result = new ArrayList<MethodSymbol>();
		Type t = ct;
		while(t instanceof ClassType) {
			ct = (ClassType)t;
			ClassSymbol cs = (ClassSymbol)ct.tsym;
			Scope scope = cs.members();
			for(Symbol s : scope.getElements()) {
	//			log.debug(s.getClass()+" "+s.toString());
				if(s instanceof MethodSymbol) {
					MethodSymbol ms = (MethodSymbol)s;
					if(ms.getKind() != ElementKind.METHOD) continue;
	//				log.debug("      "+vs.type);
					boolean isPublic = 0 != (ms.flags_field & Flags.PUBLIC);
					boolean isStatic = 0 != (ms.flags_field & Flags.STATIC);
					if(!isPublic) continue;
					if(staticOnly && !isStatic) continue;
					result.add(ms);
				}
			}
			t = ct.supertype_field;
		}
		return result;
	}

	private List<Symbol.MethodSymbol> getConstructors(ClassType ct, boolean staticOnly) {
		List<Symbol.MethodSymbol> result = new ArrayList<MethodSymbol>();
		ClassSymbol cs = (ClassSymbol)ct.tsym;
		Scope scope = cs.members();
		for(Symbol s : scope.getElements()) {
//			log.debug(s.getClass()+" "+s.toString());
			if(s instanceof MethodSymbol) {
				MethodSymbol ms = (MethodSymbol)s;
				if(ms.getKind() != ElementKind.CONSTRUCTOR) continue;
//				log.debug("      "+vs.type);
				long publicMod = (ms.flags_field & Flags.PUBLIC);
				boolean isPublic = 0l != publicMod;
//				long staticMod = (ms.flags_field & Flags.STATIC);
//				boolean isStatic = 0l != staticMod;
				if(!isPublic) continue;
//				if(staticOnly && !isStatic) continue;
				result.add(ms);
			}
		}
		return result;
	}
	

}
