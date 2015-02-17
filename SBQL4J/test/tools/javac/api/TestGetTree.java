/*
 * Copyright 2005-2006 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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

/*
 * @test
 * @bug     6344177 6392177
 * @summary Can't get tree from a top level class
 * @author  Peter von der Ah\u00e9
 * @compile TestGetTree.java
 * @compile -processor TestGetTree -proc:only TestGetTree.java
 */

import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.util.*;
import static javax.lang.model.util.ElementFilter.*;

import pl.wcislo.sbql4j.source.tree.*;
import pl.wcislo.sbql4j.source.util.Trees;

@SupportedAnnotationTypes("*")
public class TestGetTree extends AbstractProcessor {
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnvironment)
    {
        final Trees trees = Trees.instance(processingEnv);
        for (TypeElement e : typesIn(roundEnvironment.getRootElements())) {
            ClassTree node = trees.getTree(e);
            System.out.println(node.toString());
        }
        return true;
    }
}
