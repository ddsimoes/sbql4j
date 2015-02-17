/*
 * Copyright 2003-2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package pl.wcislo.sbql4j.tools.javadoc;

import pl.wcislo.sbql4j.tools.javac.code.Flags;
import pl.wcislo.sbql4j.tools.javac.code.Kinds;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.VarSymbol;
import pl.wcislo.sbql4j.tools.javac.comp.MemberEnter;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCMethodDecl;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCVariableDecl;
import pl.wcislo.sbql4j.tools.javac.util.Context;
import pl.wcislo.sbql4j.tools.javac.util.Position;

/**
 *  Javadoc's own memberEnter phase does a few things above and beyond that
 *  done by javac.
 *  @author Neal Gafter
 */
class JavadocMemberEnter extends MemberEnter {
    public static JavadocMemberEnter instance0(Context context) {
        MemberEnter instance = context.get(memberEnterKey);
        if (instance == null)
            instance = new JavadocMemberEnter(context);
        return (JavadocMemberEnter)instance;
    }

    public static void preRegister(final Context context) {
        context.put(memberEnterKey, new Context.Factory<MemberEnter>() {
               public MemberEnter make() {
                   return new JavadocMemberEnter(context);
               }
        });
    }

    final DocEnv docenv;

    protected JavadocMemberEnter(Context context) {
        super(context);
        docenv = DocEnv.instance(context);
    }

    public void visitMethodDef(JCMethodDecl tree) {
        super.visitMethodDef(tree);
        MethodSymbol meth = (MethodSymbol)tree.sym;
        if (meth == null || meth.kind != Kinds.MTH) return;
        String docComment = env.toplevel.docComments.get(tree);
        Position.LineMap lineMap = env.toplevel.lineMap;
        if (meth.isConstructor())
            docenv.makeConstructorDoc(meth, docComment, tree, lineMap);
        else if (isAnnotationTypeElement(meth))
            docenv.makeAnnotationTypeElementDoc(meth, docComment, tree, lineMap);
        else
            docenv.makeMethodDoc(meth, docComment, tree, lineMap);
    }

    public void visitVarDef(JCVariableDecl tree) {
        super.visitVarDef(tree);
        if (tree.sym != null &&
                tree.sym.kind == Kinds.VAR &&
                !isParameter(tree.sym)) {
            String docComment = env.toplevel.docComments.get(tree);
            Position.LineMap lineMap = env.toplevel.lineMap;
            docenv.makeFieldDoc((VarSymbol)tree.sym, docComment, tree, lineMap);
        }
    }

    private static boolean isAnnotationTypeElement(MethodSymbol meth) {
        return ClassDocImpl.isAnnotationType(meth.enclClass());
    }

    private static boolean isParameter(VarSymbol var) {
        return (var.flags() & Flags.PARAMETER) != 0;
    }
}
