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

import pl.wcislo.sbql4j.javadoc.AnnotationTypeDoc;
import pl.wcislo.sbql4j.javadoc.AnnotationTypeElementDoc;
import pl.wcislo.sbql4j.javadoc.MethodDoc;
import pl.wcislo.sbql4j.tools.javac.code.Kinds;
import pl.wcislo.sbql4j.tools.javac.code.Scope;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCClassDecl;
import pl.wcislo.sbql4j.tools.javac.util.List;
import pl.wcislo.sbql4j.tools.javac.util.Name;
import pl.wcislo.sbql4j.tools.javac.util.Position;


/**
 * Represents an annotation type.
 *
 * @author Scott Seligman
 * @since 1.5
 */

public class AnnotationTypeDocImpl
        extends ClassDocImpl implements AnnotationTypeDoc {

    AnnotationTypeDocImpl(DocEnv env, ClassSymbol sym) {
        this(env, sym, null, null, null);
    }

    AnnotationTypeDocImpl(DocEnv env, ClassSymbol sym,
                          String doc, JCClassDecl tree, Position.LineMap lineMap) {
        super(env, sym, doc, tree, lineMap);
    }

    /**
     * Returns true, as this is an annotation type.
     * (For legacy doclets, return false.)
     */
    public boolean isAnnotationType() {
        return !isInterface();
    }

    /**
     * Returns false.  Though technically an interface, an annotation
     * type is not considered an interface for this purpose.
     * (For legacy doclets, returns true.)
     */
    public boolean isInterface() {
        return env.legacyDoclet;
    }

    /**
     * Returns an empty array, as all methods are annotation type elements.
     * (For legacy doclets, returns the elements.)
     * @see #elements()
     */
    public MethodDoc[] methods(boolean filter) {
        return env.legacyDoclet
                ? (MethodDoc[])elements()
                : new MethodDoc[0];
    }

    /**
     * Returns the elements of this annotation type.
     * Returns an empty array if there are none.
     * Elements are always public, so no need to filter them.
     */
    public AnnotationTypeElementDoc[] elements() {
        Name.Table names = tsym.name.table;
        List<AnnotationTypeElementDoc> elements = List.nil();
        for (Scope.Entry e = tsym.members().elems; e != null; e = e.sibling) {
            if (e.sym != null && e.sym.kind == Kinds.MTH) {
                MethodSymbol s = (MethodSymbol)e.sym;
                elements = elements.prepend(env.getAnnotationTypeElementDoc(s));
            }
        }
        return
            elements.toArray(new AnnotationTypeElementDoc[elements.length()]);
    }
}
