/*
 * Copyright 2001-2007 Sun Microsystems, Inc.  All Rights Reserved.
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

package testtaglets;

import pl.wcislo.sbql4j.tools.doclets.internal.toolkit.*;
import pl.wcislo.sbql4j.tools.doclets.internal.toolkit.taglets.*;
import pl.wcislo.sbql4j.tools.doclets.internal.toolkit.util.*;

import pl.wcislo.sbql4j.javadoc.*;
import java.util.*;


/**
 * An inline Taglet representing {@green}
 *
 * @author Jamie Ho
 * @since 1.4
 */

public class GreenTaglet extends BaseInlineTaglet {


    public GreenTaglet() {
        name = "green";
    }

    public static void register(Map tagletMap) {
       GreenTaglet tag = new GreenTaglet();
       Taglet t = (Taglet) tagletMap.get(tag.getName());
       if (t != null) {
           tagletMap.remove(tag.getName());
       }
       tagletMap.put(tag.getName(), tag);
    }

    /**
     * {@inheritDoc}
     */
    public TagletOutput getTagletOutput(Tag tag, TagletWriter writer) {
        ArrayList inlineTags = new ArrayList();
        inlineTags.add(new TextTag(tag.holder(), "<font color=\"green\">"));
        inlineTags.addAll(Arrays.asList(tag.inlineTags()));
        inlineTags.add(new TextTag(tag.holder(), "</font>"));
        return writer.commentTagsToOutput(tag, (Tag[]) inlineTags.toArray(new Tag[] {}));
    }
}
