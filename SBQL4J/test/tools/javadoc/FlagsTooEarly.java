/*
 * Copyright 2003 Sun Microsystems, Inc.  All Rights Reserved.
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
 * @bug 4904495
 * @summary Compilation may go awry if we ask a symbol for its flags during
 *          javac's Enter phase, before the flags are generally available.
 */

import pl.wcislo.sbql4j.javadoc.*;

public class FlagsTooEarly extends Doclet {

    public static void main(String[] args) {
        String thisFile = "" +
            new java.io.File(System.getProperty("test.src", "."),
                             "FlagsTooEarly.java");

        if (pl.wcislo.sbql4j.tools.javadoc.Main.execute(
                "javadoc",
                "FlagsTooEarly",
                FlagsTooEarly.class.getClassLoader(),
                new String[] {"-Xwerror", thisFile}) != 0)
            throw new Error("Javadoc encountered warnings or errors.");
    }

    /*
     * The world's simplest doclet.
     */
    public static boolean start(RootDoc root) {
        return true;
    }


    /*
     * The following sets up the scenario for triggering the (potential) bug.
     */
    C2 c;
    static class C1 { }
    static class C2 { }
}
