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


public class KORQueries_SbqlQuery6Db4o0 implements Db4oSbqlQuery {
    private java.lang.Integer wiek2;

    public KORQueries_SbqlQuery6Db4o0(java.lang.Integer wiek2) {
        this.wiek2 = wiek2;
    }

    /**
     * query='dbConn.(Pilkarz where getWiek() == wiek2).getKontuzja()'
    '
     **/
    public java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Kontuzja> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _ident_Pilkarz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();
        ClassMetadata _classMeta7 = ocb.classCollection()
                                       .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Pilkarz");
        long[] _ids7 = _classMeta7.getIDs(transLocal);

        for (long _id7 : _ids7) {
            LazyObjectReference _ref7 = transLocal.lazyReferenceFor((int) _id7);
            _ident_Pilkarz.add((pl.wcislo.sbql4j.javac.test.football.model.Pilkarz) _ref7.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _whereResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();
        int _whereLoopIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _whereEl : _ident_Pilkarz) {
            if (_whereEl == null) {
                continue;
            }

            if (_whereEl != null) {
                ocb.activate(_whereEl, 1);
            }

            java.lang.Integer _mth_getWiekResult = _whereEl.getWiek();

            if (_mth_getWiekResult != null) {
                ocb.activate(_mth_getWiekResult, 1);
            }

            java.lang.Integer _ident_wiek2 = wiek2;
            java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_mth_getWiekResult,
                    _ident_wiek2);

            if (_equalsResult) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Kontuzja> _dotResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Kontuzja>();
        int _dotIndex = 0;

        if (_whereResult != null) {
            for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl : _whereResult) {
                if (_dotEl == null) {
                    continue;
                }

                if (_dotEl != null) {
                    ocb.activate(_dotEl, 1);
                }

                java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Kontuzja> _mth_getKontuzjaResult =
                    _dotEl.getKontuzja();

                if (_mth_getKontuzjaResult != null) {
                    ocb.activate(_mth_getKontuzjaResult, 2);
                }

                if (_mth_getKontuzjaResult != null) {
                    ocb.activate(_mth_getKontuzjaResult, 2);
                }

                _dotResult.addAll(_mth_getKontuzjaResult);
                _dotIndex++;
            }
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_dotResult, ocb);

        return _dotResult;
    }
}
