package pl.wcislo.sbql4j.tools.javac.comp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.PackageSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.StaticEVNSType;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureFactory;
import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.tools.javac.code.Attribute.Compound;
import pl.wcislo.sbql4j.tools.javac.code.Kinds;
import pl.wcislo.sbql4j.tools.javac.code.Scope;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.TypeTags;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.PackageSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.TypeSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.VarSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Type.ArrayType;
import pl.wcislo.sbql4j.tools.javac.code.Type.ClassType;
import pl.wcislo.sbql4j.tools.javac.util.Name;
import pl.wcislo.sbql4j.tools.javac.util.Name.Table;


/**
 * Klasa adaptera sluzaca do rozwiazywania nazw z kontekstu Javy na potrzeby zapytan SBQL
 * @author Emil
 *
 */
public class JavaNameResolver {
//	private static final Log log = LogFactory.getLog(JavaNameResolver.class);
	
	private Resolve rs;
	private Env<AttrContext> env;
	private int queryStartPos;
	private Name.Table names;
	
	private final ClassTypes cTypes;
	private JavaSignatureFactory jSigFac;
//	private List<BinderSignature> queryViewContext;
	
//	private static JavaNameResolver instance;
//	public static JavaNameResolver getInstance() {
//		return instance;
//	}
	
	/**
	 * Lista symboli java, ktore zostaly uzyte. Moze byc wykorzystane dla preprecesora
	 */
	private List<Symbol.VarSymbol> resolvedJavaVars = new ArrayList<Symbol.VarSymbol>();
	private List<Symbol.ClassSymbol> resolvedJavaTypes = new ArrayList<Symbol.ClassSymbol>();
	private List<Symbol.PackageSymbol> resolvedJavaPackages = new ArrayList<Symbol.PackageSymbol>();
	
	/**
	 * Lista uzytych metod, wykorzystane do zebrania listy wyjatkow w zapytaniu
	 */
	private List<Symbol.MethodSymbol> resolvedJavaMethods = new ArrayList<Symbol.MethodSymbol>();
	
	
	public List<Symbol.ClassSymbol> getResolvedJavaTypes() {
		return resolvedJavaTypes;
	}
	public List<Symbol.VarSymbol> getResolvedJavaVars() {
		return resolvedJavaVars;
	}

//	public JavaNameResolver(Resolve rs, Env<AttrContext> env,
//			int queryStartPos, Table names, List<BinderSignature> context) {
//		this(rs, env, queryStartPos, names);
//		this.queryViewContext = context;
//	}
	
	public JavaNameResolver(Resolve rs, Env<AttrContext> env,
			int queryStartPos, Table names) {
		super();
		this.rs = rs;
		this.env = env;
		this.queryStartPos = queryStartPos;
		this.names = names;
		this.cTypes = ClassTypes.getInstance();
//		this.jSigFac = JavaSignatureAbstractFactory.getJavaSignatureFactory();
//		instance = this;
	}
	
	private JavaSignatureFactory getJavaSignatureFactory() {
		if(jSigFac == null) {
			jSigFac = JavaSignatureAbstractFactory.getJavaSignatureFactory();
		}
		return jSigFac;
	}

	/**
	 * Znajduje sygnatury dla zmiennych i klas
	 * @param name
	 * @return
	 */
	public Signature findIdent(String name, TypeSymbol pckSymbol) {
//		Signature qSig = bindInQueryContext(name);
//		if(qSig != null) {
//			return qSig;
//		}
		Name n = names.fromString(name);
		Symbol s = rs.findVar(env, n);
//		SType stype = SType.VAR;
		if(! (s instanceof Resolve.ResolveError)) {
			if(!resolvedJavaVars.contains(s)) {
				resolvedJavaVars.add((VarSymbol) s);
			}
			Type t = s.type;
			if(t instanceof Type.ClassType || t.isPrimitive()) {
				return resolveVar(t, name, s.attributes_field);
			} else if(t instanceof Type.ArrayType) {
				return resolveVarArray((Type.ArrayType)s.type, name, s.attributes_field);
			} else {
				throw new RuntimeException("unsupported type of var "+name+": "+t.getClass());
			}
			
//			stype = SType.CLASS;
		} else {
			if(pckSymbol == null) {
				s = rs.findType(env, n);	
			} else {
				s = rs.findIdentInPackage(env, pckSymbol, n, Kinds.TYP);
			}
			if(!(s instanceof Resolve.ResolveError)) {
				if(!resolvedJavaTypes.contains(s)) {
					resolvedJavaTypes.add((ClassSymbol) s);
				}
				return resolveType((Type.ClassType)s.type, name);
			} else {
				if(pckSymbol == null) {
					s = rs.findIdent(env, n, Kinds.PCK);
				} else {
					s = rs.findIdentInPackage(env, pckSymbol, n, Kinds.PCK);
				}
				if(!(s instanceof Resolve.ResolveError)) {
					return resolvePackage((Type.PackageType)s.type, name);
				}
				
			}
		}
		return null;
	}
	
	private Signature resolveVarArray(ArrayType type, String name, List<Compound> annotationList) {
		Type elType = type.elemtype;
		if(elType instanceof ClassType || elType.isPrimitive()) {
//			ClassType ct = (ClassType) elType;
			Signature vs = resolveVar(elType, name, annotationList);
			vs.setColType(SCollectionType.SEQUENCE);
			return vs;
		} else {
			throw new RuntimeException("Unsupported array type: "+elType.getClass()+" in var: "+name);
		}
	}
	private Signature resolveVar(Type t, String name, List<Compound> annotationList) {
		Signature sig = getJavaSignatureFactory().createSignature(t, false, annotationList);
		return sig;
	}
	
	private ClassSignature resolveType(Type.ClassType t, String name) {
//		try {
//			Class type = compilerTypeToClass(t);
			//TODO EW OK 
			ClassSignature sig = getJavaSignatureFactory().createClassSignature(t);
			return sig;
//			return null;
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
	}
	
	public PackageSignature resolvePackage(Type.PackageType t, String name) {
		t.complete();
		PackageSymbol pckSym = (PackageSymbol)t.tsym;
		this.resolvedJavaPackages.add(pckSym);
		return new PackageSignature(name, pckSym);
	}
	
	public int getQueryStartPos() {
		return queryStartPos;
	}
	
	public Symbol findJavacType(String name) {
		if(name == null) return null;
		if(name.indexOf('.') > 0) {
			String[] fName = name.split("\\.");
			Symbol pckSymbol = null;
			for(int i=0; i<fName.length; i++) {
				Name n = names.fromString(fName[i]);
				if(pckSymbol == null) {
					pckSymbol = rs.findIdent(env, n, Kinds.PCK);
				} else {
					Symbol s = rs.findIdentInPackage(env, pckSymbol.type.tsym, n, Kinds.TYP);
					if(s == null || s instanceof Resolve.ResolveError) {
						pckSymbol = rs.findIdentInPackage(env, pckSymbol.type.tsym, n, Kinds.PCK);
					} else {
						return s;
					}
				}
			}
		} else {
			Name n = names.fromString(name);
			return rs.findIdent(env, n, Kinds.TYP);
		}
		return null;
	}
	public List<Symbol.PackageSymbol> getResolvedJavaPackages() {
		return resolvedJavaPackages;
	}
	
	
	private Class compilerTypeToClass(Type t) {
		if(t.isPrimitive()) {
			if(t.tag == TypeTags.BYTE) return Byte.class;
			if(t.tag == TypeTags.CHAR) return Character.class;
			if(t.tag == TypeTags.SHORT) return Short.class;
			if(t.tag == TypeTags.INT) return Integer.class;
			if(t.tag == TypeTags.LONG) return Long.class;
			if(t.tag == TypeTags.FLOAT) return Float.class;
			if(t.tag == TypeTags.DOUBLE) return Double.class;
			if(t.tag == TypeTags.BOOLEAN) return Boolean.class;
		} else {
			try {
				return Class.forName(t.tsym.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void addResolvedMethod(Symbol.MethodSymbol mth) {
		this.resolvedJavaMethods.add(mth);
	}
	
	public Collection<Type> getResolvedMethodDeclaredExecptions() {
		Set<Type> ex = new HashSet<Type>();
		for(MethodSymbol m : resolvedJavaMethods) {
			ex.addAll(m.getThrownTypes());
		}
		return ex;
	}
	
	private void dupClassSymbol(ClassSymbol cs) {
		Scope scope = cs.members_field;
//		log.debug("enter class "+cs);
		for(Symbol s : scope.getElements()) {
//			log.debug(s.getClass()+" "+s.toString());
			if(s instanceof VarSymbol) {
				VarSymbol vs = (VarSymbol)s;
//				log.debug("      "+vs.type);
			}
		}
	}
	
	public void clearResolvedNames() {
		this.resolvedJavaMethods.clear();
		this.resolvedJavaPackages.clear();
		this.resolvedJavaTypes.clear();
		this.resolvedJavaVars.clear();
	}
	
//	private Binder bindInQueryContext(String name) {
//		if(queryViewContext != null) {
//			for(StaticEVNSType t : queryViewContext) {
//				if(t.getName().equals(name)) {
//					return (Signature) t;
//				}
//			}
//		}
//		return null;
//	}
}
