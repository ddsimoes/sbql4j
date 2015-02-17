/*
 * Copyright 2004-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

package pl.wcislo.sbql4j.tools.apt.mirror.declaration;


import java.util.ArrayList;
import java.util.Collection;

import pl.wcislo.sbql4j.mirror.declaration.AnnotationTypeDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.ClassDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.EnumDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.InterfaceDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.PackageDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.TypeDeclaration;
import pl.wcislo.sbql4j.mirror.util.DeclarationVisitor;
import pl.wcislo.sbql4j.tools.apt.mirror.AptEnv;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.PackageSymbol;



/**
 * Implementation of PackageDeclaration.
 */

public class PackageDeclarationImpl extends DeclarationImpl
                                    implements PackageDeclaration {

    private PackageSymbol sym;


    public PackageDeclarationImpl(AptEnv env, PackageSymbol sym) {
        super(env, sym);
        this.sym = sym;
    }


    /**
     * Returns the qualified name.
     */
    public String toString() {
        return getQualifiedName();
    }

    /**
     * {@inheritDoc}
     */
    public String getQualifiedName() {
        return sym.getQualifiedName().toString();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<ClassDeclaration> getClasses() {
        return identityFilter.filter(getAllTypes(),
                                     ClassDeclaration.class);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<EnumDeclaration> getEnums() {
        return identityFilter.filter(getAllTypes(),
                                     EnumDeclaration.class);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<InterfaceDeclaration> getInterfaces() {
        return identityFilter.filter(getAllTypes(),
                                     InterfaceDeclaration.class);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<AnnotationTypeDeclaration> getAnnotationTypes() {
        return identityFilter.filter(getAllTypes(),
                                     AnnotationTypeDeclaration.class);
    }

    /**
     * {@inheritDoc}
     */
    public void accept(DeclarationVisitor v) {
        v.visitPackageDeclaration(this);
    }


    // Cache of all top-level type declarations in this package.
    private Collection<TypeDeclaration> allTypes = null;

    /**
     * Caches and returns all top-level type declarations in this package.
     * Omits synthetic types.
     */
    private Collection<TypeDeclaration> getAllTypes() {
        if (allTypes != null) {
            return allTypes;
        }
        allTypes = new ArrayList<TypeDeclaration>();
        for (Symbol s : getMembers(false)) {
            allTypes.add(env.declMaker.getTypeDeclaration((ClassSymbol) s));
        }
        return allTypes;
    }
}
