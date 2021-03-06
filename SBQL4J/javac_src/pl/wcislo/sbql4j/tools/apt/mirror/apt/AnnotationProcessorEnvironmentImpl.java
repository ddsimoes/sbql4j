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

package pl.wcislo.sbql4j.tools.apt.mirror.apt;




import static pl.wcislo.sbql4j.mirror.util.DeclarationVisitors.NO_OP;
import static pl.wcislo.sbql4j.mirror.util.DeclarationVisitors.getSourceOrderDeclarationScanner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import pl.wcislo.sbql4j.mirror.apt.AnnotationProcessor;
import pl.wcislo.sbql4j.mirror.apt.AnnotationProcessorEnvironment;
import pl.wcislo.sbql4j.mirror.apt.AnnotationProcessorListener;
import pl.wcislo.sbql4j.mirror.apt.Filer;
import pl.wcislo.sbql4j.mirror.apt.Messager;
import pl.wcislo.sbql4j.mirror.apt.RoundCompleteEvent;
import pl.wcislo.sbql4j.mirror.apt.RoundCompleteListener;
import pl.wcislo.sbql4j.mirror.apt.RoundState;
import pl.wcislo.sbql4j.mirror.declaration.AnnotationMirror;
import pl.wcislo.sbql4j.mirror.declaration.AnnotationTypeDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.Declaration;
import pl.wcislo.sbql4j.mirror.declaration.PackageDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.TypeDeclaration;
import pl.wcislo.sbql4j.mirror.util.Declarations;
import pl.wcislo.sbql4j.mirror.util.SimpleDeclarationVisitor;
import pl.wcislo.sbql4j.mirror.util.Types;
import pl.wcislo.sbql4j.tools.apt.mirror.declaration.DeclarationMaker;
import pl.wcislo.sbql4j.tools.apt.mirror.util.DeclarationsImpl;
import pl.wcislo.sbql4j.tools.apt.mirror.util.TypesImpl;
import pl.wcislo.sbql4j.tools.apt.util.Bark;
import pl.wcislo.sbql4j.tools.javac.util.Context;

/*
 * Annotation Processor Environment implementation.
 */
public class AnnotationProcessorEnvironmentImpl implements AnnotationProcessorEnvironment {

    Collection<TypeDeclaration> spectypedecls;
    Collection<TypeDeclaration> typedecls;
    Map<String, String> origOptions;
    DeclarationMaker declMaker;
    Declarations declUtils;
    Types typeUtils;
    Messager messager;
    FilerImpl filer;
    Bark bark;
    Set<RoundCompleteListener> roundCompleteListeners;

    public AnnotationProcessorEnvironmentImpl(Collection<TypeDeclaration> spectypedecls,
                                              Collection<TypeDeclaration> typedecls,
                                              Map<String, String> origOptions,
                                              Context context) {
        // Safer to copy collections before applying unmodifiable
        // wrapper.
        this.spectypedecls = Collections.unmodifiableCollection(spectypedecls);
        this.typedecls = Collections.unmodifiableCollection(typedecls);
        this.origOptions = Collections.unmodifiableMap(origOptions);

        declMaker = DeclarationMaker.instance(context);
        declUtils = DeclarationsImpl.instance(context);
        typeUtils = TypesImpl.instance(context);
        messager = MessagerImpl.instance(context);
        filer = FilerImpl.instance(context);
        bark = Bark.instance(context);
        roundCompleteListeners = new LinkedHashSet<RoundCompleteListener>();
    }

    public Map<String,String> getOptions() {
        return origOptions;
    }

    public Messager getMessager() {
        return messager;
    }

    public Filer getFiler() {
        return filer;
    }

    public Collection<TypeDeclaration> getSpecifiedTypeDeclarations() {
        return spectypedecls;
    }

    public PackageDeclaration getPackage(String name) {
        return declMaker.getPackageDeclaration(name);
    }

    public TypeDeclaration getTypeDeclaration(String name) {
        return declMaker.getTypeDeclaration(name);
    }

    public Collection<TypeDeclaration> getTypeDeclarations() {
        return typedecls;
    }

    public Collection<Declaration> getDeclarationsAnnotatedWith(
                                                AnnotationTypeDeclaration a) {
        /*
         * create collection of Declarations annotated with a given
         * annotation.
         */

        CollectingAP proc = new CollectingAP(this, a);
        proc.process();
        return proc.decls;
    }

    private static class CollectingAP implements AnnotationProcessor {
        AnnotationProcessorEnvironment env;
        Collection<Declaration> decls;
        AnnotationTypeDeclaration atd;
        CollectingAP(AnnotationProcessorEnvironment env,
                     AnnotationTypeDeclaration atd) {
            this.env = env;
            this.atd = atd;
            decls = new HashSet<Declaration>();
        }

        private class CollectingVisitor extends SimpleDeclarationVisitor {
            public void visitDeclaration(Declaration d) {
                for(AnnotationMirror am: d.getAnnotationMirrors()) {
                    if (am.getAnnotationType().getDeclaration().equals(CollectingAP.this.atd))
                        CollectingAP.this.decls.add(d);
                }
            }
        }

        public void process() {
            for(TypeDeclaration d: env.getSpecifiedTypeDeclarations())
                d.accept(getSourceOrderDeclarationScanner(new CollectingVisitor(),
                                                          NO_OP));
        }
    }

    public Declarations getDeclarationUtils() {
        return declUtils;
    }

    public Types getTypeUtils() {
        return typeUtils;
    }

    public void addListener(AnnotationProcessorListener listener) {
        if (listener == null)
            throw new NullPointerException();
        else {
            if (listener instanceof RoundCompleteListener)
                roundCompleteListeners.add((RoundCompleteListener)listener);
        }
    }

    public void removeListener(AnnotationProcessorListener listener) {
        if (listener == null)
            throw new NullPointerException();
        else
            roundCompleteListeners.remove(listener);
    }

    public void roundComplete() {
        RoundState roundState  = new RoundStateImpl(bark.nerrors > 0,
                                                    filer.getSourceFileNames().size() > 0,
                                                    filer.getClassFileNames().size() > 0,
                                                    origOptions);
        RoundCompleteEvent roundCompleteEvent = new RoundCompleteEventImpl(this, roundState);

        filer.roundOver();
        for(RoundCompleteListener rcl: roundCompleteListeners)
            rcl.roundComplete(roundCompleteEvent);
    }
}
