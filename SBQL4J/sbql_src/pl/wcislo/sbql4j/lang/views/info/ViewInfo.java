package pl.wcislo.sbql4j.lang.views.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.lang.parser.expression.DotExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.views.annotatinos.View;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCClassDecl;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCMethodDecl;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSbqlExpression;


public class ViewInfo {
	public static final String VIEW_ANNOTATION_CLASS = View.class.getName();
	
//	/**
//	 * Indicates, if described view is standalone, ie. can be validated without information of view invocation.
//	 * Some kinds of views accepts parameters that cannot be validated in compile time (eg. structs, which are 
//	 * generic class without information of its stucture in compile-time). These view should be validated and
//	 * compiled together with its invocation, where all information about view parameters are available.
//	 */
//	private boolean standalone;

	/**
	 * Contains info of readable view (present only if the view is readable)
	 */
	private OnReadInfo onReadInfo;
	
	private SeedInfo seedInfo;
	
	private Map<String, ViewInfo> subViews = new HashMap<String, ViewInfo>();
	private ViewInfo parentViewInfo;
	private JCClassDecl viewJCDecl;
	
	public ViewInfo() {
		this.seedInfo = new SeedInfo();
	}
	
	public boolean isReadable() {
		return onReadInfo != null;
	}
	
//	public boolean isStandalone() {
//		return standalone;
//	}
//	public void setStandalone(boolean standalone) {
//		this.standalone = standalone;
//	}

	public List<BinderSignature> getVirtualObjectsSignaturesWithParentViews() {
		List<BinderSignature> res = new ArrayList<BinderSignature>();
		ViewInfo vi = this;
		while(vi != null) {
			res.add(0, vi.seedInfo.getSeedSignature());
			vi = vi.getParentViewInfo();
		}
		return res;
	}
	public void addSubViewInfo(String viewName, ViewInfo info) {
		this.subViews.put(viewName, info);
	}
	public ViewInfo getSubViewInfo(String viewName) {
		return this.subViews.get(viewName);
	}
	public Collection<ViewInfo> getAllSubViewInfo() {
		return this.subViews.values();
	}
	
	public void setReadableViewInfo(OnReadInfo onReadInfo) {
		this.onReadInfo = onReadInfo;
	}
	public OnReadInfo getOnReadInfo() {
		return onReadInfo;
	}
	
	
	public class SeedInfo {
		/**
		 * Query text of seed method
		 */
		private String seedQuery;
		/**
		 * Signature of result of the virtual objects method
		 */
		private BinderSignature seedSignature;
		/**
		 * Java compiler expression of query that is main expression of the virual objects method
		 */
		private JCSbqlExpression seedJCExpression;
		
		private JCMethodDecl seedMethodDecl;
		
		public JCSbqlExpression getSeedJCExpression() {
			return seedJCExpression;
		}
		public void setSeedJCExpression(JCSbqlExpression virtualObjectsJCExpression) {
			this.seedJCExpression = virtualObjectsJCExpression;
		}
		public String getSeedQuery() {
			return seedQuery;
		}
		public void setSeedQuery(String virtualObjectsQuery) {
			this.seedQuery = virtualObjectsQuery;
		}
		public BinderSignature getSeedSignature() {
			return seedSignature;
		}
		public void setSeedSignature(BinderSignature virtualObjectsSignature) {
			this.seedSignature = virtualObjectsSignature;
		}
		public void setSeedMethodDecl(JCMethodDecl seedMethodDecl) {
			this.seedMethodDecl = seedMethodDecl;
		}
		public JCMethodDecl getSeedMethodDecl() {
			return seedMethodDecl;
		}
	}
	
	public class OnReadInfo {
		private String onReadQuery;
		private BinderSignature onReadSignature;
		private JCSbqlExpression onReadJCExpression;
		private JCMethodDecl onReadMethodDecl;
		public String getOnReadQuery() {
			return onReadQuery;
		}
		public void setOnReadQuery(String onReadQuery) {
			this.onReadQuery = onReadQuery;
		}
		public BinderSignature getOnReadSignature() {
			return onReadSignature;
		}
		public void setOnReadSignature(BinderSignature onReadSignature) {
			this.onReadSignature = onReadSignature;
		}
		public JCSbqlExpression getOnReadJCExpression() {
			return onReadJCExpression;
		}
		public void setOnReadJCExpression(JCSbqlExpression onReadJCExpression) {
			this.onReadJCExpression = onReadJCExpression;
		}
		public void setOnReadMethodDecl(JCMethodDecl onReadMethodDecl) {
			this.onReadMethodDecl = onReadMethodDecl;
		}
		public JCMethodDecl getOnReadMethodDecl() {
			return onReadMethodDecl;
		}
	}
	
	public SeedInfo getSeedInfo() {
		return seedInfo;
	}

	public ViewInfo getParentViewInfo() {
		return parentViewInfo;
	}

	public void setParentViewInfo(ViewInfo parentViewInfo) {
		this.parentViewInfo = parentViewInfo;
	}

	public JCClassDecl getViewJCDecl() {
		return viewJCDecl;
	}

	public void setViewJCDecl(JCClassDecl viewJCDecl) {
		this.viewJCDecl = viewJCDecl;
	}
	
	public Expression getViewExpressionReadReplacement() {
		if(this.onReadInfo == null) {
			return seedInfo.seedJCExpression.exprTree;
		} else {
			Expression voExpr = seedInfo.seedJCExpression.exprTree;
			Expression onReadExpr = onReadInfo.onReadJCExpression.exprTree;
			DotExpression res = new DotExpression(-1, voExpr, onReadExpr);
			return res;
		}
	}


}