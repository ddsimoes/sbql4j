package pl.wcislo.sbql4j.lang.parser;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public abstract class TreeNode {

	public abstract <R,T> R accept(TreeVisitor<R,T> visitor, T object);

}
