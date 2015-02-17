/*
 * Copyright 2004 Sun Microsystems, Inc.  All Rights Reserved.
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

package pl.wcislo.sbql4j.tools.apt.mirror.util;


import static pl.wcislo.sbql4j.tools.javac.code.Kinds.MTH;
import pl.wcislo.sbql4j.mirror.declaration.MemberDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.MethodDeclaration;
import pl.wcislo.sbql4j.mirror.util.Declarations;
import pl.wcislo.sbql4j.tools.apt.mirror.AptEnv;
import pl.wcislo.sbql4j.tools.apt.mirror.declaration.DeclarationImpl;
import pl.wcislo.sbql4j.tools.apt.mirror.declaration.MethodDeclarationImpl;
import pl.wcislo.sbql4j.tools.javac.code.Flags;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;
import pl.wcislo.sbql4j.tools.javac.util.Context;


/**
 * Implementation of Declarations utility methods for annotation processors
 */

public class DeclarationsImpl implements Declarations {

    private final AptEnv env;


    private static final Context.Key<Declarations> declarationsKey =
            new Context.Key<Declarations>();

    public static Declarations instance(Context context) {
        Declarations instance = context.get(declarationsKey);
        if (instance == null) {
            instance = new DeclarationsImpl(context);
        }
        return instance;
    }

    private DeclarationsImpl(Context context) {
        context.put(declarationsKey, this);
        env = AptEnv.instance(context);
    }


    /**
     * {@inheritDoc}
     * See JLS 2 sections 8.3 and 8.4.6.
     */
    public boolean hides(MemberDeclaration sub, MemberDeclaration sup) {
        Symbol hider = ((DeclarationImpl) sub).sym;
        Symbol hidee = ((DeclarationImpl) sup).sym;

        // Fields only hide fields; methods only methods; types only types.
        // Names must match.  Nothing hides itself (just try it).
        if (hider == hidee ||
                hider.kind != hidee.kind ||
                hider.name != hidee.name) {
            return false;
        }

        // Only static methods can hide other methods.
        // Methods only hide methods with matching signatures.
        if (hider.kind == MTH) {
            if ((hider.flags() & Flags.STATIC) == 0 ||
                        !env.jctypes.isSubSignature(hider.type, hidee.type)) {
                return false;
            }
        }

        // Hider must be in a subclass of hidee's class.
        // Note that if M1 hides M2, and M2 hides M3, and M3 is accessible
        // in M1's class, then M1 and M2 both hide M3.
        ClassSymbol hiderClass = hider.owner.enclClass();
        ClassSymbol hideeClass = hidee.owner.enclClass();
        if (hiderClass == null || hideeClass == null ||
                !hiderClass.isSubClass(hideeClass, env.jctypes)) {
            return false;
        }

        // Hidee must be accessible in hider's class.
        // The method isInheritedIn is poorly named:  it checks only access.
        return hidee.isInheritedIn(hiderClass, env.jctypes);
    }

    /**
     * {@inheritDoc}
     * See JLS 2 section 8.4.6.1.
     */
    public boolean overrides(MethodDeclaration sub, MethodDeclaration sup) {
        MethodSymbol overrider = ((MethodDeclarationImpl) sub).sym;
        MethodSymbol overridee = ((MethodDeclarationImpl) sup).sym;
        ClassSymbol origin = (ClassSymbol) overrider.owner;

        return overrider.name == overridee.name &&

               // not reflexive as per JLS
               overrider != overridee &&

               // we don't care if overridee is static, though that wouldn't
               // compile
               !overrider.isStatic() &&

               // overrider, whose declaring type is the origin, must be
               // in a subtype of overridee's type
               env.jctypes.asSuper(origin.type, overridee.owner) != null &&

               // check access and signatures; don't check return types
               overrider.overrides(overridee, origin, env.jctypes, false);
    }
}
