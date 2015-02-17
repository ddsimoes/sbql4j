package pl.wcislo.sbql4j.lang.views.info;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.lang.views.annotatinos.OnRead;
import pl.wcislo.sbql4j.lang.views.annotatinos.Seed;
import pl.wcislo.sbql4j.lang.views.annotatinos.View;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Type.ClassType;
import pl.wcislo.sbql4j.tools.javac.comp.Attr;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCAnnotation;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCBlock;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCClassDecl;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCMethodDecl;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCNewClass;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCReturn;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSbqlExpression;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCTypeApply;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCVariableDecl;

/**
 * Analyses declaration of class in scope of view declaration
 * @author Emil
 *
 */
public class JCViewInfoCollector {
//	public static final String MTH_NAME_VIRTUAL_OBJECTS = "getVirtualObjects";
//	public static final String MTH_NAME_ON_READ = "onRead";
	private JCClassDecl viewClass;
	private ClassType viewType;
	private ViewInfo viewInfo;
//	private Attr attr;
	
	public JCViewInfoCollector(JCClassDecl viewClass, ClassType viewType, Attr attr) {
		this.viewClass = viewClass;
		this.viewType = viewType;
//		this.attr = attr;
	}
	
	public ViewInfo analyseViewDeclaration() {
		ViewInfo res = new ViewInfo();
		//check if view was already analysed in type enclosing view (if its subview)
		//if yes, view info should be taken from parent view (appropriate for this subview) 
    	if(viewClass.type != null && viewClass.type.getEnclosingType() != null) {
    		Type encType = viewClass.type.getEnclosingType();
    		if(encType instanceof ClassType) {
    			ClassType ct = (ClassType)encType;
    			if(ct.viewInfo != null) {
    				for(ViewInfo vi : ct.viewInfo.getAllSubViewInfo()) {
    					if(vi.getViewJCDecl() == viewClass) {
    						return vi;
    					}
    				}
    			}
    		}
    	}
    	res.setViewJCDecl(viewClass);
		//first check if the view is standalone
//		boolean standalone = true;
//		for(JCAnnotation ann : viewClass.mods.annotations) {
//			if(ann.annotationType.toString().equals(NonStandaloneView.class.getName())) {
//				standalone = false;
//			}
//		}
//		res.setStandalone(standalone);

		//check if view is readable
    	//if it is so, it should have method with OnRead annotation
//    	boolean readable = false;
//    	for(JCTree def : viewClass.defs) {
//    		if(def instanceof JCMethodDecl) {
//    			JCMethodDecl meth = (JCMethodDecl) def;
//    			for(JCAnnotation ann : meth.mods.annotations) {
//    				if(ann.annotationType.toString().equals(OnRead.class.getName())) {
//    					readable = true;
//    				}
//    			}
//    		}
//    	}
    	
//		ClassTypes ct = ClassTypes.getInstance();
//		Type rViewCompilerType =  ct.getCompilerType(ReadableView.class);
//		boolean readableView = ct.isSubClass(viewType, rViewCompilerType);
		
		//get queries of view
		for(JCTree tree : viewClass.defs) {
			if(tree instanceof JCMethodDecl) {
				JCMethodDecl meth = (JCMethodDecl) tree;
				for(JCAnnotation ann : meth.mods.annotations) {
    				if(ann.annotationType.toString().equals(OnRead.class.getName())) {
    					JCSbqlExpression qexpr = getMethodReturnQuery(meth);
    					ViewInfo.OnReadInfo rInfo = res.new OnReadInfo();
    					rInfo.setOnReadJCExpression(qexpr);
    					if(qexpr != null) {
    						rInfo.setOnReadQuery(qexpr.expr);	
    					}
    					res.setReadableViewInfo(rInfo);
    				} else if(ann.annotationType.toString().equals(Seed.class.getName())) {
    					JCSbqlExpression qexpr = getMethodReturnQuery(meth);
    					res.getSeedInfo().setSeedJCExpression(qexpr);
    					if(qexpr != null) {
    						res.getSeedInfo().setSeedQuery(qexpr.expr);	
    					}
    				}
//    				else if(!"<init>".equals(meth.name.toString())) {
////    					meth.
//    					//check if method is sub-view declaration
//    					ViewInfo vi = checkMethodSubView(meth, res);
//    					if(vi != null) {
//    						String subViewName;
//    						String methName = meth.getName().toString(); 
//    						if(methName.startsWith("get")) {
//    							subViewName = Character.toLowerCase(methName.charAt(3)) +  methName.substring(4);
//    						} else {
//    							subViewName = methName;
//    						}
//    						res.addSubViewInfo(subViewName, vi);
//    					}
//    				}
    			}
				
				
			} else if(tree instanceof JCVariableDecl) {
				JCVariableDecl varDecl = (JCVariableDecl) tree;
				ViewInfo vi = checkFieldSubView(varDecl, res);
				if(vi != null) {
					String subViewName = varDecl.name.toString();
					res.addSubViewInfo(subViewName, vi);
				}
			}
			
		}
		return res;
	}
	
	public ViewInfo getViewInfo() {
		return viewInfo;
	}
	private JCSbqlExpression getMethodReturnQuery(JCMethodDecl mth) {
		JCBlock mBody = mth.body;
		if(mBody.stats.size() > 0 && mBody.stats.get(0) instanceof JCReturn) {
			JCReturn ret = (JCReturn) mBody.stats.get(0);
			if(ret.expr instanceof JCSbqlExpression) {
				JCSbqlExpression sbqlQuery = (JCSbqlExpression)ret.expr;
				if(sbqlQuery.exprTree == null) {
					sbqlQuery.enclosingClass = viewType;
//					attr.visitSbqlExpression(sbqlQuery);
				}
//				attr.visitSbqlExpression(sbqlQuery);
//				String query = sbqlQuery.expr;
				return sbqlQuery;
			} else {
				//exception - return should be sbql query
			}
		} else {
			//exception - the only statement of method should be return statement
		}
		return null;
	}
	
	private ViewInfo checkMethodSubView(JCMethodDecl mth, ViewInfo parentView) {
		ClassTypes ct = ClassTypes.getInstance();
		Type cTypeView = ct.getCompilerType(View.class);
		Type mthType = mth.getReturnType().type;
		boolean isView = ct.isSubClass(mthType, cTypeView);
		if(isView) {			
			JCBlock mBody = mth.body;
			if(mBody.stats.size() > 0 && mBody.stats.get(0) instanceof JCReturn) {
				JCReturn ret = (JCReturn) mBody.stats.get(0);
				if(ret.expr instanceof JCNewClass) {
					JCNewClass newC = (JCNewClass)ret.expr;
					JCClassDecl viewDecl = newC.def;
					JCViewInfoCollector col = new JCViewInfoCollector(viewDecl, (ClassType) mthType, null);
					ViewInfo subViewInfo = col.analyseViewDeclaration();
					subViewInfo.setParentViewInfo(parentView);
					return subViewInfo;
				} else {
					//exception - return should be sbql query
				}
			} else {
				//exception - the only statement of method should be return statement
			}	
		}
		return null;
	}
	
	private ViewInfo checkFieldSubView(JCVariableDecl field, ViewInfo parentView) {
		if(field.vartype instanceof JCTypeApply) {
			JCTypeApply tApply = (JCTypeApply) field.vartype;
			ClassTypes ct = ClassTypes.getInstance();
			Type fieldType = tApply.type;
			Type cTypeView = ct.getCompilerType(View.class);
			boolean isView = ct.isSubClass(fieldType, cTypeView);
			if(isView) {
				if(field.init instanceof JCNewClass) {
//					ClassType viewType = (ClassType)((JCTypeApply) field.vartype).type;
					JCNewClass newC = (JCNewClass)field.init;
					JCClassDecl viewDecl = newC.def;
					JCViewInfoCollector col = new JCViewInfoCollector(viewDecl, (ClassType) fieldType, null);
					ViewInfo subViewInfo = col.analyseViewDeclaration();
					subViewInfo.setParentViewInfo(parentView);
					return subViewInfo;
				}
			}
		}
		return null;
		
	}
}
