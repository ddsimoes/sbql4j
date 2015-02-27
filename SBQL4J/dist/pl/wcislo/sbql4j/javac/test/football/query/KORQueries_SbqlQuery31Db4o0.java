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


public class KORQueries_SbqlQuery31Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery31Db4o0() {
    }

    /**
     * query='dbConn.(Druzyna as d where  avg(d.getLista_zawodnikow().getWiek()) > 20 and  avg(d.getLista_zawodnikow().getWiek()) < 40 and  exists d.getLista_zawodnikow().getKontuzja())'
    '
     **/
    public java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _ident_Druzyna =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Druzyna>();
        ClassMetadata _classMeta43 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Druzyna");
        long[] _ids43 = _classMeta43.getIDs(transLocal);

        for (long _id43 : _ids43) {
            LazyObjectReference _ref43 = transLocal.lazyReferenceFor((int) _id43);
            _ident_Druzyna.add((pl.wcislo.sbql4j.javac.test.football.model.Druzyna) _ref43.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _asResult_d =
            _ident_Druzyna;
        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _whereResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Druzyna>();
        int _whereLoopIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Druzyna _whereEl : _asResult_d) {
            if (_whereEl == null) {
                continue;
            }

            if (_whereEl != null) {
                ocb.activate(_whereEl, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _ident_d = _whereEl;

            if (_ident_d != null) {
                ocb.activate(_ident_d, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl = _ident_d;

            if (_ident_d != null) {
                ocb.activate(_ident_d, 2);
            }

            java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _mth_getLista_zawodnikowResult =
                _dotEl.getLista_zawodnikow();

            if (_mth_getLista_zawodnikowResult != null) {
                ocb.activate(_mth_getLista_zawodnikowResult, 2);
            }

            java.util.List<java.lang.Integer> _dotResult1 = new java.util.ArrayList<java.lang.Integer>();
            int _dotIndex1 = 0;

            if (_mth_getLista_zawodnikowResult != null) {
                for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl1 : _mth_getLista_zawodnikowResult) {
                    if (_dotEl1 == null) {
                        continue;
                    }

                    if (_dotEl1 != null) {
                        ocb.activate(_dotEl1, 1);
                    }

                    java.lang.Integer _mth_getWiekResult = _dotEl1.getWiek();

                    if (_mth_getWiekResult != null) {
                        ocb.activate(_mth_getWiekResult, 1);
                    }

                    if (_mth_getWiekResult != null) {
                        ocb.activate(_mth_getWiekResult, 1);
                    }

                    _dotResult1.add(_mth_getWiekResult);
                    _dotIndex1++;
                }
            }

            java.lang.Double _avgResult = 0d;

            if ((_dotResult1 != null) && !_dotResult1.isEmpty()) {
                Number _avgSum2 = null;

                for (Number _avgEl2 : _dotResult1) {
                    _avgSum2 = MathUtils.sum(_avgSum2, _avgEl2);
                }

                _avgResult = _avgSum2.doubleValue() / _dotResult1.size();
            }

            Boolean _moreResult = (_avgResult == null) ? false : (_avgResult > 20);
            java.lang.Boolean _andResult;

            if (!_moreResult) {
                _andResult = false;
            } else {
                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _ident_d1 = _whereEl;

                if (_ident_d1 != null) {
                    ocb.activate(_ident_d1, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl2 = _ident_d1;

                if (_ident_d1 != null) {
                    ocb.activate(_ident_d1, 2);
                }

                java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _mth_getLista_zawodnikowResult1 =
                    _dotEl2.getLista_zawodnikow();

                if (_mth_getLista_zawodnikowResult1 != null) {
                    ocb.activate(_mth_getLista_zawodnikowResult1, 2);
                }

                java.util.List<java.lang.Integer> _dotResult3 = new java.util.ArrayList<java.lang.Integer>();
                int _dotIndex3 = 0;

                if (_mth_getLista_zawodnikowResult1 != null) {
                    for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl3 : _mth_getLista_zawodnikowResult1) {
                        if (_dotEl3 == null) {
                            continue;
                        }

                        if (_dotEl3 != null) {
                            ocb.activate(_dotEl3, 1);
                        }

                        java.lang.Integer _mth_getWiekResult1 = _dotEl3.getWiek();

                        if (_mth_getWiekResult1 != null) {
                            ocb.activate(_mth_getWiekResult1, 1);
                        }

                        if (_mth_getWiekResult1 != null) {
                            ocb.activate(_mth_getWiekResult1, 1);
                        }

                        _dotResult3.add(_mth_getWiekResult1);
                        _dotIndex3++;
                    }
                }

                java.lang.Double _avgResult1 = 0d;

                if ((_dotResult3 != null) && !_dotResult3.isEmpty()) {
                    Number _avgSum3 = null;

                    for (Number _avgEl3 : _dotResult3) {
                        _avgSum3 = MathUtils.sum(_avgSum3, _avgEl3);
                    }

                    _avgResult1 = _avgSum3.doubleValue() / _dotResult3.size();
                }

                Boolean _lessResult = _avgResult1 < 40;
                _andResult = _lessResult;
            }

            java.lang.Boolean _andResult1;

            if (!_andResult) {
                _andResult1 = false;
            } else {
                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _ident_d2 = _whereEl;

                if (_ident_d2 != null) {
                    ocb.activate(_ident_d2, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl4 = _ident_d2;

                if (_ident_d2 != null) {
                    ocb.activate(_ident_d2, 2);
                }

                java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _mth_getLista_zawodnikowResult2 =
                    _dotEl4.getLista_zawodnikow();

                if (_mth_getLista_zawodnikowResult2 != null) {
                    ocb.activate(_mth_getLista_zawodnikowResult2, 2);
                }

                java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Kontuzja> _dotResult5 =
                    new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Kontuzja>();
                int _dotIndex5 = 0;

                if (_mth_getLista_zawodnikowResult2 != null) {
                    for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl5 : _mth_getLista_zawodnikowResult2) {
                        if (_dotEl5 == null) {
                            continue;
                        }

                        if (_dotEl5 != null) {
                            ocb.activate(_dotEl5, 1);
                        }

                        java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Kontuzja> _mth_getKontuzjaResult =
                            _dotEl5.getKontuzja();

                        if (_mth_getKontuzjaResult != null) {
                            ocb.activate(_mth_getKontuzjaResult, 2);
                        }

                        if (_mth_getKontuzjaResult != null) {
                            ocb.activate(_mth_getKontuzjaResult, 2);
                        }

                        _dotResult5.addAll(_mth_getKontuzjaResult);
                        _dotIndex5++;
                    }
                }

                java.lang.Boolean _existsResult = !_dotResult5.isEmpty();
                _andResult1 = _existsResult;
            }

            if (_andResult1) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_whereResult, ocb);

        return _whereResult;
    }
}
