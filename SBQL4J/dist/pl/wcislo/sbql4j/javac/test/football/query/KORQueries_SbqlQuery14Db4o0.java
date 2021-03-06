package pl.wcislo.sbql4j.javac.test.football.query;

import com.db4o.*;
import com.db4o.ObjectContainer;

import com.db4o.foundation.*;

import com.db4o.internal.*;
import com.db4o.internal.btree.*;

import org.apache.commons.collections.CollectionUtils;

import pl.wcislo.sbql4j.db4o.*;
import pl.wcislo.sbql4j.exception.*;
import pl.wcislo.sbql4j.java.model.runtime.*;
import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.java.model.runtime.factory.*;
import pl.wcislo.sbql4j.java.utils.ArrayUtils;
import pl.wcislo.sbql4j.java.utils.OperatorUtils;
import pl.wcislo.sbql4j.java.utils.Pair;
import pl.wcislo.sbql4j.javac.test.football.data.*;
import pl.wcislo.sbql4j.javac.test.football.model.*;
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
import pl.wcislo.sbql4j.lang.xml.*;
import pl.wcislo.sbql4j.model.*;
import pl.wcislo.sbql4j.model.collections.*;
import pl.wcislo.sbql4j.util.*;
import pl.wcislo.sbql4j.util.Utils;
import pl.wcislo.sbql4j.xml.model.*;
import pl.wcislo.sbql4j.xml.parser.store.*;

import java.lang.Integer;
import java.lang.String;

import java.text.ParseException;

import java.util.*;


public class KORQueries_SbqlQuery14Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery14Db4o0() {
    }

    /**
     * query='dbConn.Mecz.getWynik()'
    '
     **/
    public java.util.Collection<java.lang.String> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _ident_Mecz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Mecz>();
        ClassMetadata _classMeta18 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Mecz");
        long[] _ids18 = _classMeta18.getIDs(transLocal);

        for (long _id18 : _ids18) {
            LazyObjectReference _ref18 = transLocal.lazyReferenceFor((int) _id18);
            _ident_Mecz.add((pl.wcislo.sbql4j.javac.test.football.model.Mecz) _ref18.getObject());
        }

        java.util.Collection<java.lang.String> _dotResult = new java.util.ArrayList<java.lang.String>();
        int _dotIndex = 0;

        if (_ident_Mecz != null) {
            for (pl.wcislo.sbql4j.javac.test.football.model.Mecz _dotEl : _ident_Mecz) {
                if (_dotEl == null) {
                    continue;
                }

                if (_dotEl != null) {
                    ocb.activate(_dotEl, 1);
                }

                java.lang.String _mth_getWynikResult = _dotEl.getWynik();

                if (_mth_getWynikResult != null) {
                    ocb.activate(_mth_getWynikResult, 1);
                }

                if (_mth_getWynikResult != null) {
                    ocb.activate(_mth_getWynikResult, 1);
                }

                _dotResult.add(_mth_getWynikResult);
                _dotIndex++;
            }
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_dotResult, ocb);

        return _dotResult;
    }
}
