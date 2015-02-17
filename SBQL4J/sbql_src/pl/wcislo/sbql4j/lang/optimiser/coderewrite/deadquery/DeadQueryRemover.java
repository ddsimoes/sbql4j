package pl.wcislo.sbql4j.lang.optimiser.coderewrite.deadquery;

import pl.wcislo.sbql4j.lang.parser.expression.BinaryExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ComaExpression;
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.tree.visitors.TraversingASTAdapter;

public class DeadQueryRemover extends TraversingASTAdapter {

	private boolean removedSth = false;
	public boolean isRemovedSth() {
		return removedSth;
	}
	
	@Override
	public Object visitComaExpression(ComaExpression expr, Object object) {
		remove(expr, object);
		return null;
	}
	
	@Override
	public Object visitJoinExpression(JoinExpression expr, Object object) {
		remove(expr, object);
		return null;
	}
	
	private void remove(BinaryExpression expr, Object attr) {
		if(!expr.ex1.deadQData.isMarked()) {
			
//			if(expr.getLeftExpression().getSignature().getMinCard() == 1 && expr.getLeftExpression().getSignature().getMaxCard() == 1){
			if(!expr.ex1.getSignature().isCollectionResult() && !expr.ex2.getSignature().isCollectionResult() ){
				if(expr.parentExpression != null) {
					expr.parentExpression.replaceSubExpression(expr, expr.ex2);
					expr.parentExpression = null;
					removedSth = true;
				}
			}
		} else {
			expr.ex1.accept(this, attr);
		}
		if(!expr.ex2.deadQData.isMarked()) {
//			remove only when the cardinality == 1
//			if(expr.getRightExpression().getSignature().getMinCard() == 1 && expr.getRightExpression().getSignature().getMaxCard() == 1){
			if(!expr.ex1.getSignature().isCollectionResult() && !expr.ex2.getSignature().isCollectionResult() ){
				if(expr.parentExpression != null) {
					expr.parentExpression.replaceSubExpression(expr, expr.ex1);
					expr.parentExpression = null;
					removedSth = true;
				}
			}
		} else {
			expr.ex2.accept(this, attr);
		}
	}
}