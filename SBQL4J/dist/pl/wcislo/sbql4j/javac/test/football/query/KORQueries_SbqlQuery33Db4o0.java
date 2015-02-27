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


public class KORQueries_SbqlQuery33Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery33Db4o0() {
    }

    /**
     * query='dbConn.(Druzyna as druz).(druz.getMiasto() as city, druz.getNazwa() as name).city'
    '
     **/
    public java.util.Collection<java.lang.String> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _ident_Druzyna =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Druzyna>();
        ClassMetadata _classMeta46 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Druzyna");
        long[] _ids46 = _classMeta46.getIDs(transLocal);

        for (long _id46 : _ids46) {
            LazyObjectReference _ref46 = transLocal.lazyReferenceFor((int) _id46);
            _ident_Druzyna.add((pl.wcislo.sbql4j.javac.test.football.model.Druzyna) _ref46.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _asResult_druz =
            _ident_Druzyna;
        java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> _dotResult2 =
            new java.util.ArrayList<pl.wcislo.sbql4j.java.model.runtime.Struct>();
        int _dotIndex2 = 0;

        if (_asResult_druz != null) {
            for (pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl2 : _asResult_druz) {
                if (_dotEl2 == null) {
                    continue;
                }

                if (_dotEl2 != null) {
                    ocb.activate(_dotEl2, 2);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _ident_druz = _dotEl2;

                if (_ident_druz != null) {
                    ocb.activate(_ident_druz, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl = _ident_druz;

                if (_ident_druz != null) {
                    ocb.activate(_ident_druz, 2);
                }

                java.lang.String _mth_getMiastoResult = _dotEl.getMiasto();

                if (_mth_getMiastoResult != null) {
                    ocb.activate(_mth_getMiastoResult, 1);
                }

                java.lang.String _asResult_city = _mth_getMiastoResult;
                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _ident_druz1 = _dotEl2;

                if (_ident_druz1 != null) {
                    ocb.activate(_ident_druz1, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl1 = _ident_druz1;

                if (_ident_druz1 != null) {
                    ocb.activate(_ident_druz1, 2);
                }

                java.lang.String _mth_getNazwaResult = _dotEl1.getNazwa();

                if (_mth_getNazwaResult != null) {
                    ocb.activate(_mth_getNazwaResult, 1);
                }

                java.lang.String _asResult_name = _mth_getNazwaResult;
                pl.wcislo.sbql4j.java.model.runtime.Struct _commaResult = OperatorUtils.cartesianProductSS(_asResult_city,
                        _asResult_name, "city", "name");

                if (_commaResult != null) {
                    ocb.activate(_commaResult, 2);
                }

                _dotResult2.add(_commaResult);
                _dotIndex2++;
            }
        }

        java.util.Collection<java.lang.String> _dotResult3 = new java.util.ArrayList<java.lang.String>();
        int _dotIndex3 = 0;

        if (_dotResult2 != null) {
            for (pl.wcislo.sbql4j.java.model.runtime.Struct _dotEl3 : _dotResult2) {
                if (_dotEl3 == null) {
                    continue;
                }

                if (_dotEl3 != null) {
                    ocb.activate(_dotEl3, 1);
                }

                java.lang.String _ident_city = (java.lang.String) _dotEl3.get(
                        "city");

                if (_ident_city != null) {
                    ocb.activate(_ident_city, 1);
                }

                if (_ident_city != null) {
                    ocb.activate(_ident_city, 1);
                }

                _dotResult3.add(_ident_city);
                _dotIndex3++;
            }
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_dotResult3, ocb);

        return _dotResult3;
    }
}
