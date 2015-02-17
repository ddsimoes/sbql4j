package org;

import com.db4o.ObjectContainer;

import org.apache.commons.collections.CollectionUtils;

import pl.wcislo.sbql4j.exception.*;
import pl.wcislo.sbql4j.java.model.runtime.*;
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

import java.lang.Integer;

import java.util.*;


public class Test_SbqlQuery1 {
    private com.db4o.ObjectContainer db;

    public Test_SbqlQuery1(com.db4o.ObjectContainer db) {
        this.db = db;
    }

    /**
     * original query='db.(Integer[0])'
     *
     * query after optimization='db.(Integer[0])'
    */
    public java.lang.Integer executeQuery() {
        com.db4o.ObjectContainer _ident_db = db;
        java.lang.Integer _queryResult = _ident_db.query(new Test_SbqlQuery1Db4o0());

        return _queryResult;
    }
}
