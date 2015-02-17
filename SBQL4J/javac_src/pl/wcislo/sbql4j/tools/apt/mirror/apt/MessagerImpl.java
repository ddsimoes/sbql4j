/*
 * Copyright 2004-2005 Sun Microsystems, Inc.  All Rights Reserved.
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

package pl.wcislo.sbql4j.tools.apt.mirror.apt;

import pl.wcislo.sbql4j.mirror.apt.Messager;
import pl.wcislo.sbql4j.mirror.util.SourcePosition;
import pl.wcislo.sbql4j.tools.apt.mirror.util.SourcePositionImpl;
import pl.wcislo.sbql4j.tools.apt.util.Bark;
import pl.wcislo.sbql4j.tools.javac.util.Context;
import sbql4jx.tools.JavaFileObject;



/**
 * Implementation of Messager.
 */

public class MessagerImpl implements Messager {
    private final Bark bark;

    private static final Context.Key<MessagerImpl> messagerKey =
            new Context.Key<MessagerImpl>();

    public static MessagerImpl instance(Context context) {
        MessagerImpl instance = context.get(messagerKey);
        if (instance == null) {
            instance = new MessagerImpl(context);
        }
        return instance;
    }

    private MessagerImpl(Context context) {
        context.put(messagerKey, this);
        bark = Bark.instance(context);
    }


    /**
     * {@inheritDoc}
     */
    public void printError(String msg) {
        bark.aptError("Messager", msg);
    }

    /**
     * {@inheritDoc}
     */
    public void printError(SourcePosition pos, String msg) {
        if (pos instanceof SourcePositionImpl) {
            SourcePositionImpl posImpl = (SourcePositionImpl) pos;
            JavaFileObject prev = bark.useSource(posImpl.getSource());
            bark.aptError(posImpl.getJavacPosition(), "Messager", msg);
            bark.useSource(prev);
        } else
            printError(msg);
    }

    /**
     * {@inheritDoc}
     */
    public void printWarning(String msg) {
        bark.aptWarning("Messager", msg);
    }

    /**
     * {@inheritDoc}
     */
    public void printWarning(SourcePosition pos, String msg) {
        if (pos instanceof SourcePositionImpl) {
            SourcePositionImpl posImpl = (SourcePositionImpl) pos;
            JavaFileObject prev = bark.useSource(posImpl.getSource());
            bark.aptWarning(posImpl.getJavacPosition(), "Messager", msg);
            bark.useSource(prev);
        } else
            printWarning(msg);
    }

    /**
     * {@inheritDoc}
     */
    public void printNotice(String msg) {
        bark.aptNote("Messager", msg);
    }

    /**
     * {@inheritDoc}
     */
    public void printNotice(SourcePosition pos, String msg) {
        if (pos instanceof SourcePositionImpl) {
            SourcePositionImpl posImpl = (SourcePositionImpl) pos;
            JavaFileObject prev = bark.useSource(posImpl.getSource());
            bark.aptNote(posImpl.getJavacPosition(), "Messager", msg);
            bark.useSource(prev);
        } else
            printNotice(msg);
    }
}
