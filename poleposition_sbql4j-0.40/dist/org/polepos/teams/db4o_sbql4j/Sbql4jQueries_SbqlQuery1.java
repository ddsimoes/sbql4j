package org.polepos.teams.db4o_sbql4j;

import com.db4o.*;

import com.db4o.config.*;

import com.db4o.query.*;

import org.apache.commons.collections.CollectionUtils;

import org.polepos.circuits.flatobject.*;

import org.polepos.data.*;
import org.polepos.data.IndexedObject;

import pl.wcislo.sbql4j.exception.*;
import pl.wcislo.sbql4j.java.model.runtime.*;
import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.java.model.runtime.factory.*;
import pl.wcislo.sbql4j.java.utils.ArrayUtils;
import pl.wcislo.sbql4j.java.utils.OperatorUtils;
import pl.wcislo.sbql4j.java.utils.Pair;
import pl.wcislo.sbql4j.lang.codegen.nostacks.*;
import pl.wcislo.sbql4j.lang.codegen.simple.*;
import pl.wcislo.sbql4j.lang.db4o.*;
import pl.wcislo.sbql4j.lang.db4o.codegen.*;
import pl.wcislo.sbql4j.lang.db4o.codegen.interpreter.*;
import pl.wcislo.sbql4j.lang.db4o.codegen.nostacks.*;
import pl.wcislo.sbql4j.lang.parser.expression.*;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression.SortType;
import pl.wcislo.sbql4j.lang.parser.terminals.*;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.*;
import pl.wcislo.sbql4j.lang.types.*;
import pl.wcislo.sbql4j.model.*;
import pl.wcislo.sbql4j.model.collections.*;
import pl.wcislo.sbql4j.util.*;
import pl.wcislo.sbql4j.util.Utils;
import pl.wcislo.sbql4j.xml.model.*;
import pl.wcislo.sbql4j.xml.parser.store.*;

import java.util.*;


public class Sbql4jQueries_SbqlQuery1 {
    private com.db4o.ObjectContainer db;
    private java.lang.Integer intId;

    public Sbql4jQueries_SbqlQuery1(com.db4o.ObjectContainer db,
        java.lang.Integer intId) {
        this.db = db;
        this.intId = intId;
    }

    /**
     * original query='db.(IndexedObject where _int == intId)'
     *
     * query after optimization='db.IndexedObject_ByIndex[_int](intId)'
    */
    public java.util.Collection<org.polepos.data.IndexedObject> executeQuery() {
        com.db4o.ObjectContainer _ident_db = db;
        java.util.Collection<org.polepos.data.IndexedObject> _queryResult = _ident_db.query(new Sbql4jQueries_SbqlQuery1Db4o0(
                    intId));

        return _queryResult;
    }
}
