package pl.wcislo.sbql4j.java.model.compiletime;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import pl.wcislo.sbql4j.java.model.compiletime.Signature.ResultSource;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureFactory;
import pl.wcislo.sbql4j.lang.parser.expression.LiteralExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.TypeSymbol;
import pl.wcislo.sbql4j.tools.javac.comp.JavaNameResolver;
import sun.java2d.SunCompositeContext;

public class StaticENVS extends Stack<List<StaticEVNSType>> {
	
	private JavaNameResolver jnr;
	private JavaSignatureFactory jSigFac;
	
	private List<BindResult> varToIncludeToDb4oQuery = new ArrayList<BindResult>();
	public List<BindResult> getVarToIncludeToDb4oQuery() {
		return varToIncludeToDb4oQuery;
	}
	public void clearVarToIncludeToDb4oQuery() {
		varToIncludeToDb4oQuery = new ArrayList<BindResult>();
	}
	
	public StaticENVS(JavaNameResolver jnr) {
		this.jnr = jnr;
		this.jSigFac = JavaSignatureAbstractFactory.getJavaSignatureFactory();
	}
	
	public List<BindResult> bind(String name, boolean allowFindPackage, BindExpression bindExpression, boolean checkGetterMethods) {
//		for(List<? extends ENVSType> o : envs) {
		List<BindResult> res = new ArrayList<BindResult>();
//		int db4oStartFrame = -1;
		//level of envs where found db4o nested marker (where binders to db4o objects should be)
		int db4oLowestFrame = -1;
		for(int i=this.size()-1; i >= 0; i--) {
			List<StaticEVNSType> o = this.get(i);
			List<StaticEVNSType> section = (List<StaticEVNSType>) o;
			for(StaticEVNSType secEl : section) {
				if(secEl instanceof Db4oNestedMarker) {
//					db4oContext = true;
					db4oLowestFrame = i;
				}
				if(secEl instanceof BinderSignature) {
					BinderSignature b = (BinderSignature) secEl;
					if(name.equals(b.name)) {
						BindResult r = new BindResult(name, b.value.clone(), b.getNestedInfo(), b, i);
						bindExpression.setBoundSignature(b);
						res.add(r);
					}
				} 
			}
			if(res.isEmpty() && checkGetterMethods) {
				String mName = "get"+Character.toUpperCase(name.charAt(0))+name.substring(1);;
				if(bindExpression instanceof NameExpression) {
					if(((NameExpression)bindExpression).boundGetterMethodAsIdentifier == true) {
						mName = name;
					}
				}
				
				StructSignature noArgSig = new StructSignature();
				for(StaticEVNSType secEl : section) {
					if(secEl instanceof MethodSignature) {
						MethodSignature b = (MethodSignature) secEl;
						if(b.isApplicableTo(mName, noArgSig)) {
							jnr.addResolvedMethod(b.method);
							BindResult r = new BindResult(mName, b.clone(), b.getNestedInfo(), b, i);
							bindExpression.setBoundSignature(b);
							if(bindExpression instanceof NameExpression) {
								((NameExpression)bindExpression).boundGetterMethodAsIdentifier = true;
							}
							res.add(r);
						}
					} 
				}
				mName  = "is"+Character.toUpperCase(name.charAt(0))+name.substring(1);
				for(StaticEVNSType secEl : section) {
					if(secEl instanceof MethodSignature) {
						MethodSignature b = (MethodSignature) secEl;
						String methResTypeString = b.returnTypeSig.getTypeName();
						String boolanClassName = Boolean.class.getName();
						if(b.isApplicableTo(mName, noArgSig) && methResTypeString.equals(boolanClassName)) {
							jnr.addResolvedMethod(b.method);
							BindResult r = new BindResult(mName, b.clone(), b.getNestedInfo(), b, i);
							bindExpression.setBoundSignature(b);
							if(bindExpression instanceof NameExpression) {
								((NameExpression)bindExpression).boundGetterMethodAsIdentifier = true;
							}
							res.add(r);
						}
					} 
				}
			}
		}
		// na koncu szukamy w zakresie nazw javy
		//sprawdzamy, czy na stosie nie ma informacji o pakiecie
		if(res.isEmpty()) {
			TypeSymbol pckSymbol = null;
			for(int i=this.size()-1; i >= 0; i--) {
				List<? extends StaticEVNSType> o = this.get(i);
				if(!o.isEmpty()) {
					StaticEVNSType s = o.get(0);
					if(s instanceof PackageSignature) {
						pckSymbol = ((PackageSignature)s).compilerSymbol;
						break;
					}
				}
			}
			Signature sig = jnr.findIdent(name, pckSymbol);
			int foundOnStackLevel = -1;
			boolean classNameAsDb4oObjects = false;
			if(sig instanceof ClassSignature && db4oLowestFrame > -1) {
				ClassSignature cs = (ClassSignature)sig;
				sig = jSigFac.createValueSignature(cs.getType(), false);
				sig.setColType(SCollectionType.BAG);
				sig.setResultSource(ResultSource.DB4O);
//				name = cs.type.tsym.toString();
				NameExpression ns = (NameExpression)bindExpression;
				ns.fullName = cs.getType().tsym.toString();
				//db4o connection - stack level -1, object in db4o - stack level 0
				foundOnStackLevel = db4oLowestFrame;
				classNameAsDb4oObjects = true;
				
			}
			if(sig instanceof PackageSignature && !allowFindPackage) {
			} else if(sig != null) {
				BindResult r = new BindResult(name, sig, null, null, foundOnStackLevel);
				res.add(r);
				
			}
		}
		if(db4oLowestFrame > -1) {
			for(BindResult br : res) {
//				Signature sig = br.boundValue;
//				boolean originDb4o = sig.getResultSource() == ResultSource.DB4O;
//				boolean isLiteral = sig.getAssociatedExpression() instanceof LiteralExpression;
				if(br.envsStackLevel < db4oLowestFrame && !varToIncludeToDb4oQuery.contains(br)) {
					varToIncludeToDb4oQuery.add(br);
					if(br.binder != null) {
						br.binder.setPassedAsParameterToDiffContext(true);
					}
				}
//				if(!originDb4o && !(sig instanceof Db4oConnectionSignature) && !isLiteral) {
//					if(!varNamesToIncludeToDb4oQuery.contains(name)) {
//						varToIncludeToDb4oQuery.add(br);
//						varNamesToIncludeToDb4oQuery.add(name);
//					}
//				}
			}
		}
		
		return res;
	}
	
	public BindResult bindMethod(String mName, StructSignature paramsSig) {
//		ValueSignature res = null;
//		boolean found = false;
//		for(List<? extends ENVSType> o : envs) {
		for(int i=this.size()-1; i >= 0; i--) {
			List<? extends StaticEVNSType> o = this.get(i);
//			if(found) return res;
			List<? extends StaticEVNSType> section = (List<? extends StaticEVNSType>) o;
			for(StaticEVNSType secEl : section) {
				if(secEl instanceof MethodSignature) {
					MethodSignature b = (MethodSignature) secEl;
					if(b.isApplicableTo(mName, paramsSig)) {
						jnr.addResolvedMethod(b.method);
						BindResult r = new BindResult(mName, b.clone(), b.getNestedInfo(), b, i);
//						bindExpression.setBoundSignature(b);
						return r;
					}
				} 
			}
		}
		return null; //wyjatek
//		return res;
	}
	
	public BindResult bindConstructor(String className, StructSignature paramsSig) {
//		ValueSignature res = null;
//		boolean found = false;
//		for(List<? extends ENVSType> o : envs) {
		for(int i=this.size()-1; i >= 0; i--) {
			List<? extends StaticEVNSType> o = this.get(i);
//			if(found) return res;
			List<? extends Signature> section = (List<? extends Signature>) o;
			for(Signature secEl : section) {
				if(secEl instanceof ConstructorSignature) {
					ConstructorSignature b = (ConstructorSignature) secEl;
					String qName = b.ownerClass.getQualifiedName().toString();
					if(qName.equals(className) &&  b.isApplicableTo(paramsSig)) {
//						jnr.addResolvedMethod(b.method);
						return new BindResult(className, b.clone(), b.getNestedInfo(), b, i);
					}
				} 
			}
		}
		return null; //wyjatek
//		return res;
	}
}
