/*
 * Copyright 2005-2006 Sun Microsystems, Inc.  All Rights Reserved.
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

package pl.wcislo.sbql4j.source.util;

import java.io.IOException;

import pl.wcislo.sbql4j.source.tree.CompilationUnitTree;
import pl.wcislo.sbql4j.source.tree.Tree;
import sbql4jx.lang.model.element.Element;
import sbql4jx.lang.model.type.TypeMirror;
import sbql4jx.lang.model.util.Elements;
import sbql4jx.lang.model.util.Types;
import sbql4jx.tools.JavaFileObject;
import sbql4jx.tools.JavaCompiler.CompilationTask;

/**
 * Provides access to functionality specific to the Sun Java Compiler, javac.
 *
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @since 1.6
 */
public abstract class JavacTask implements CompilationTask {

    /**
     * Parse the specified files returning a list of abstract syntax trees.
     *
     * @return a list of abstract syntax trees
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends CompilationUnitTree> parse()
        throws IOException;

    /**
     * Complete all analysis.
     *
     * @return a list of elements that were analyzed
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends Element> analyze() throws IOException;

    /**
     * Generate code.
     *
     * @return a list of files that were generated
     * @throws IOException if an unhandled I/O error occurred in the compiler.
     */
    public abstract Iterable<? extends JavaFileObject> generate() throws IOException;

    /**
     * The specified listener will receive events describing the progress of
     * this compilation task.
     */
    public abstract void setTaskListener(TaskListener taskListener);

    /**
     * Get a type mirror of the tree node determined by the specified path.
     */
    public abstract TypeMirror getTypeMirror(Iterable<? extends Tree> path);
    /**
     * Get a utility object for dealing with program elements.
     */
    public abstract Elements getElements();

    /**
     * Get a utility object for dealing with type mirrors.
     */
    public abstract Types getTypes();
}
