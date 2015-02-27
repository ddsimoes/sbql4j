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


public class KORQueries_SbqlQuery13Db4o0 implements Db4oSbqlQuery {
    private java.lang.String druzyna1;

    public KORQueries_SbqlQuery13Db4o0(java.lang.String druzyna1) {
        this.druzyna1 = druzyna1;
    }

    /**
     * query='dbConn.(Mecz where getDruzyna2().getNazwa() == druzyna1).getWynik()'
    '
     **/
    public java.util.Collection<java.lang.String> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _ident_Mecz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Mecz>();
        ClassMetadata _classMeta17 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Mecz");
        long[] _ids17 = _classMeta17.getIDs(transLocal);

        for (long _id17 : _ids17) {
            LazyObjectReference _ref17 = transLocal.lazyReferenceFor((int) _id17);
            _ident_Mecz.add((pl.wcislo.sbql4j.javac.test.football.model.Mecz) _ref17.getObject());
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

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _mth_getDruzyna2Result =
                _whereEl.getDruzyna2();

            if (_mth_getDruzyna2Result != null) {
                ocb.activate(_mth_getDruzyna2Result, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl = _mth_getDruzyna2Result;

            if (_mth_getDruzyna2Result != null) {
                ocb.activate(_mth_getDruzyna2Result, 2);
            }

            java.lang.String _mth_getNazwaResult = _dotEl.getNazwa();

            if (_mth_getNazwaResult != null) {
                ocb.activate(_mth_getNazwaResult, 1);
            }

            java.lang.String _ident_druzyna1 = druzyna1;
            java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_mth_getNazwaResult,
                    _ident_druzyna1);

            if (_equalsResult) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        java.util.Collection<java.lang.String> _dotResult1 = new java.util.ArrayList<java.lang.String>();
        int _dotIndex1 = 0;

        if (_whereResult != null) {
            for (pl.wcislo.sbql4j.javac.test.football.model.Mecz _dotEl1 : _whereResult) {
                if (_dotEl1 == null) {
                    continue;
                }

                if (_dotEl1 != null) {
                    ocb.activate(_dotEl1, 1);
                }

                java.lang.String _mth_getWynikResult = _dotEl1.getWynik();

                if (_mth_getWynikResult != null) {
                    ocb.activate(_mth_getWynikResult, 1);
                }

                if (_mth_getWynikResult != null) {
                    ocb.activate(_mth_getWynikResult, 1);
                }

                _dotResult1.add(_mth_getWynikResult);
                _dotIndex1++;
            }
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_dotResult1, ocb);

        return _dotResult1;
    }
}
