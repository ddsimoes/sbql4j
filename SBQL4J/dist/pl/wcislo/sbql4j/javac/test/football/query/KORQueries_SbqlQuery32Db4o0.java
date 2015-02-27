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


public class KORQueries_SbqlQuery32Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery32Db4o0() {
    }

    /**
     * query='dbConn.( unique Mecz.getNazwa_stadionu() group as _aux0).(Druzyna as d where  avg(d.getLista_zawodnikow().getPremia()) > 20 and  avg(d.getLista_zawodnikow().getPremia()) < 40 and (d.getMecz().getNazwa_stadionu() in _aux0))'
    '
     **/
    public java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _ident_Mecz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Mecz>();
        ClassMetadata _classMeta44 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Mecz");
        long[] _ids44 = _classMeta44.getIDs(transLocal);

        for (long _id44 : _ids44) {
            LazyObjectReference _ref44 = transLocal.lazyReferenceFor((int) _id44);
            _ident_Mecz.add((pl.wcislo.sbql4j.javac.test.football.model.Mecz) _ref44.getObject());
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

                java.lang.String _mth_getNazwa_stadionuResult = _dotEl.getNazwa_stadionu();

                if (_mth_getNazwa_stadionuResult != null) {
                    ocb.activate(_mth_getNazwa_stadionuResult, 1);
                }

                if (_mth_getNazwa_stadionuResult != null) {
                    ocb.activate(_mth_getNazwa_stadionuResult, 1);
                }

                _dotResult.add(_mth_getNazwa_stadionuResult);
                _dotIndex++;
            }
        }

        java.util.Collection<java.lang.String> _uniqueResult = new java.util.ArrayList<java.lang.String>();
        Set<java.lang.String> _tmp2 = new LinkedHashSet<java.lang.String>();
        _tmp2.addAll(_dotResult);
        _uniqueResult.addAll(_tmp2);

        java.util.Collection<java.lang.String> _groupAsResult_aux0 = _uniqueResult;
        java.util.Collection<java.lang.String> _dotEl7 = _groupAsResult_aux0;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _ident_Druzyna =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Druzyna>();
        ClassMetadata _classMeta45 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Druzyna");
        long[] _ids45 = _classMeta45.getIDs(transLocal);

        for (long _id45 : _ids45) {
            LazyObjectReference _ref45 = transLocal.lazyReferenceFor((int) _id45);
            _ident_Druzyna.add((pl.wcislo.sbql4j.javac.test.football.model.Druzyna) _ref45.getObject());
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

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl1 = _ident_d;

            if (_ident_d != null) {
                ocb.activate(_ident_d, 2);
            }

            java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _mth_getLista_zawodnikowResult =
                _dotEl1.getLista_zawodnikow();

            if (_mth_getLista_zawodnikowResult != null) {
                ocb.activate(_mth_getLista_zawodnikowResult, 2);
            }

            java.util.List<java.lang.Integer> _dotResult2 = new java.util.ArrayList<java.lang.Integer>();
            int _dotIndex2 = 0;

            if (_mth_getLista_zawodnikowResult != null) {
                for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl2 : _mth_getLista_zawodnikowResult) {
                    if (_dotEl2 == null) {
                        continue;
                    }

                    if (_dotEl2 != null) {
                        ocb.activate(_dotEl2, 1);
                    }

                    java.lang.Integer _mth_getPremiaResult = _dotEl2.getPremia();

                    if (_mth_getPremiaResult != null) {
                        ocb.activate(_mth_getPremiaResult, 1);
                    }

                    if (_mth_getPremiaResult != null) {
                        ocb.activate(_mth_getPremiaResult, 1);
                    }

                    _dotResult2.add(_mth_getPremiaResult);
                    _dotIndex2++;
                }
            }

            java.lang.Double _avgResult = 0d;

            if ((_dotResult2 != null) && !_dotResult2.isEmpty()) {
                Number _avgSum4 = null;

                for (Number _avgEl4 : _dotResult2) {
                    _avgSum4 = MathUtils.sum(_avgSum4, _avgEl4);
                }

                _avgResult = _avgSum4.doubleValue() / _dotResult2.size();
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

                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl3 = _ident_d1;

                if (_ident_d1 != null) {
                    ocb.activate(_ident_d1, 2);
                }

                java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _mth_getLista_zawodnikowResult1 =
                    _dotEl3.getLista_zawodnikow();

                if (_mth_getLista_zawodnikowResult1 != null) {
                    ocb.activate(_mth_getLista_zawodnikowResult1, 2);
                }

                java.util.List<java.lang.Integer> _dotResult4 = new java.util.ArrayList<java.lang.Integer>();
                int _dotIndex4 = 0;

                if (_mth_getLista_zawodnikowResult1 != null) {
                    for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl4 : _mth_getLista_zawodnikowResult1) {
                        if (_dotEl4 == null) {
                            continue;
                        }

                        if (_dotEl4 != null) {
                            ocb.activate(_dotEl4, 1);
                        }

                        java.lang.Integer _mth_getPremiaResult1 = _dotEl4.getPremia();

                        if (_mth_getPremiaResult1 != null) {
                            ocb.activate(_mth_getPremiaResult1, 1);
                        }

                        if (_mth_getPremiaResult1 != null) {
                            ocb.activate(_mth_getPremiaResult1, 1);
                        }

                        _dotResult4.add(_mth_getPremiaResult1);
                        _dotIndex4++;
                    }
                }

                java.lang.Double _avgResult1 = 0d;

                if ((_dotResult4 != null) && !_dotResult4.isEmpty()) {
                    Number _avgSum5 = null;

                    for (Number _avgEl5 : _dotResult4) {
                        _avgSum5 = MathUtils.sum(_avgSum5, _avgEl5);
                    }

                    _avgResult1 = _avgSum5.doubleValue() / _dotResult4.size();
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

                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl5 = _ident_d2;

                if (_ident_d2 != null) {
                    ocb.activate(_ident_d2, 2);
                }

                java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _mth_getMeczResult =
                    _dotEl5.getMecz();

                if (_mth_getMeczResult != null) {
                    ocb.activate(_mth_getMeczResult, 2);
                }

                java.util.List<java.lang.String> _dotResult6 = new java.util.ArrayList<java.lang.String>();
                int _dotIndex6 = 0;

                if (_mth_getMeczResult != null) {
                    for (pl.wcislo.sbql4j.javac.test.football.model.Mecz _dotEl6 : _mth_getMeczResult) {
                        if (_dotEl6 == null) {
                            continue;
                        }

                        if (_dotEl6 != null) {
                            ocb.activate(_dotEl6, 1);
                        }

                        java.lang.String _mth_getNazwa_stadionuResult1 = _dotEl6.getNazwa_stadionu();

                        if (_mth_getNazwa_stadionuResult1 != null) {
                            ocb.activate(_mth_getNazwa_stadionuResult1, 1);
                        }

                        if (_mth_getNazwa_stadionuResult1 != null) {
                            ocb.activate(_mth_getNazwa_stadionuResult1, 1);
                        }

                        _dotResult6.add(_mth_getNazwa_stadionuResult1);
                        _dotIndex6++;
                    }
                }

                java.util.Collection<java.lang.String> _ident__aux0 = _dotEl7;

                if (_ident__aux0 != null) {
                    ocb.activate(_ident__aux0, 2);
                }

                java.lang.Boolean _inResult = _ident__aux0.containsAll(_dotResult6);
                _andResult1 = _inResult;
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
