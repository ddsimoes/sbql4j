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


public class KORQueries_SbqlQuery41Db4o0 implements Db4oSbqlQuery {
    private java.lang.String nazwadruzyny;

    public KORQueries_SbqlQuery41Db4o0(java.lang.String nazwadruzyny) {
        this.nazwadruzyny = nazwadruzyny;
    }

    /**
     * query='dbConn.( count((Mecz where getDruzyna().getNazwa() == nazwadruzyny)))'
    '
     **/
    public java.lang.Integer executeQuery(final ObjectContainerBase ocb,
        final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _ident_Mecz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Mecz>();
        ClassMetadata _classMeta53 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Mecz");
        long[] _ids53 = _classMeta53.getIDs(transLocal);

        for (long _id53 : _ids53) {
            LazyObjectReference _ref53 = transLocal.lazyReferenceFor((int) _id53);
            _ident_Mecz.add((pl.wcislo.sbql4j.javac.test.football.model.Mecz) _ref53.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _whereResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Mecz>();
        int _whereLoopIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Mecz _whereEl : _ident_Mecz) {
            if (_whereEl == null) {
                continue;
            }

            if (_whereEl != null) {
                ocb.activate(_whereEl, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _mth_getDruzynaResult =
                _whereEl.getDruzyna();

            if (_mth_getDruzynaResult != null) {
                ocb.activate(_mth_getDruzynaResult, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl = _mth_getDruzynaResult;

            if (_mth_getDruzynaResult != null) {
                ocb.activate(_mth_getDruzynaResult, 2);
            }

            java.lang.String _mth_getNazwaResult = _dotEl.getNazwa();

            if (_mth_getNazwaResult != null) {
                ocb.activate(_mth_getNazwaResult, 1);
            }

            java.lang.String _ident_nazwadruzyny = nazwadruzyny;
            java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_mth_getNazwaResult,
                    _ident_nazwadruzyny);

            if (_equalsResult) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        java.lang.Integer _countResult = _whereResult.size();
        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_countResult, ocb);

        return _countResult;
    }
}
