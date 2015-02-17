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

import pl.wcislo.sbql4j.javadoc.AnnotationTypeElementDoc;
import pl.wcislo.sbql4j.javadoc.AnnotationValue;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCMethodDecl;
import pl.wcislo.sbql4j.tools.javac.util.Position;


/**
 * Represents an element of an annotation type.
 *
 * @author Scott Seligman
 * @since 1.5
 */

public class AnnotationTypeElementDocImpl
        extends MethodDocImpl implements AnnotationTypeElementDoc {

    AnnotationTypeElementDocImpl(DocEnv env, MethodSymbol sym) {
        super(env, sym);
    }

    AnnotationTypeElementDocImpl(DocEnv env, MethodSymbol sym,
                                 String doc, JCMethodDecl tree, Position.LineMap lineMap) {
        super(env, sym, doc, tree, lineMap);
    }

    /**
     * Returns true, as this is an annotation type element.
     * (For legacy doclets, return false.)
     */
    public boolean isAnnotationTypeElement() {
        return !isMethod();
    }

    /**
     * Returns false.  Although this is technically a method, we don't
     * consider it one for this purpose.
     * (For legacy doclets, return true.)
     */
    public boolean isMethod() {
        return env.legacyDoclet;
    }

    /**
     * Returns false, even though this is indeed abstract.  See
     * MethodDocImpl.isAbstract() for the (il)logic behind this.
     */
    public boolean isAbstract() {
        return false;
    }

    /**
     * Returns the default value of this element.
     * Returns null if this element has no default.
     */
    public AnnotationValue defaultValue() {
        return (sym.defaultValue == null)
               ? null
               : new AnnotationValueImpl(env, sym.defaultValue);
    }
}
