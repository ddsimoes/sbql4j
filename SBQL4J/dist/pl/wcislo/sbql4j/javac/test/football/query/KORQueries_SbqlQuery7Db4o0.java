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


public class KORQueries_SbqlQuery7Db4o0 implements Db4oSbqlQuery {
    private java.lang.String wiek;

    public KORQueries_SbqlQuery7Db4o0(java.lang.String wiek) {
        this.wiek = wiek;
    }

    /**
     * query='dbConn.(Druzyna where getNazwa() == wiek join  min(getLista_zawodnikow().getPremia()) as minimumpilkarz,  avg(getLista_zawodnikow().getPremia()) as averagepilkarz,  max(getLista_zawodnikow().getPremia()) as maximumpilkarz,  min(getListalekarzy().getPremia()) as minimumlekarz,  avg(getListalekarzy().getPremia()) as averagelekarz,  max(getListalekarzy().getPremia()) as maximumlekarz)'
    '
     **/
    public java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _ident_Druzyna =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Druzyna>();
        ClassMetadata _classMeta8 = ocb.classCollection()
                                       .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Druzyna");
        long[] _ids8 = _classMeta8.getIDs(transLocal);

        for (long _id8 : _ids8) {
            LazyObjectReference _ref8 = transLocal.lazyReferenceFor((int) _id8);
            _ident_Druzyna.add((pl.wcislo.sbql4j.javac.test.football.model.Druzyna) _ref8.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _whereResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Druzyna>();
        int _whereLoopIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Druzyna _whereEl : _ident_Druzyna) {
            if (_whereEl == null) {
                continue;
            }

            if (_whereEl != null) {
                ocb.activate(_whereEl, 1);
            }

            java.lang.String _mth_getNazwaResult = _whereEl.getNazwa();

            if (_mth_getNazwaResult != null) {
                ocb.activate(_mth_getNazwaResult, 1);
            }

            java.lang.String _ident_wiek = wiek;
            java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_mth_getNazwaResult,
                    _ident_wiek);

            if (_equalsResult) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> _joinResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.java.model.runtime.Struct>();
        int _joinIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Druzyna _joinEl : _whereResult) {
            if (_joinEl != null) {
                ocb.activate(_joinEl, 1);
            }

            java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _mth_getLista_zawodnikowResult =
                _joinEl.getLista_zawodnikow();

            if (_mth_getLista_zawodnikowResult != null) {
                ocb.activate(_mth_getLista_zawodnikowResult, 2);
            }

            java.util.List<java.lang.Integer> _dotResult = new java.util.ArrayList<java.lang.Integer>();
            int _dotIndex = 0;

            if (_mth_getLista_zawodnikowResult != null) {
                for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl : _mth_getLista_zawodnikowResult) {
                    if (_dotEl == null) {
                        continue;
                    }

                    if (_dotEl != null) {
                        ocb.activate(_dotEl, 1);
                    }

                    java.lang.Integer _mth_getPremiaResult = _dotEl.getPremia();

                    if (_mth_getPremiaResult != null) {
                        ocb.activate(_mth_getPremiaResult, 1);
                    }

                    if (_mth_getPremiaResult != null) {
                        ocb.activate(_mth_getPremiaResult, 1);
                    }

                    _dotResult.add(_mth_getPremiaResult);
                    _dotIndex++;
                }
            }

            Number _min0 = null;

            for (Number _minEl0 : _dotResult) {
                _min0 = MathUtils.min(_min0, _minEl0);
            }

            java.lang.Integer _minResult = (java.lang.Integer) _min0;
            java.lang.Integer _asResult_minimumpilkarz = _minResult;
            java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _mth_getLista_zawodnikowResult1 =
                _joinEl.getLista_zawodnikow();

            if (_mth_getLista_zawodnikowResult1 != null) {
                ocb.activate(_mth_getLista_zawodnikowResult1, 2);
            }

            java.util.List<java.lang.Integer> _dotResult1 = new java.util.ArrayList<java.lang.Integer>();
            int _dotIndex1 = 0;

            if (_mth_getLista_zawodnikowResult1 != null) {
                for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl1 : _mth_getLista_zawodnikowResult1) {
                    if (_dotEl1 == null) {
                        continue;
                    }

                    if (_dotEl1 != null) {
                        ocb.activate(_dotEl1, 1);
                    }

                    java.lang.Integer _mth_getPremiaResult1 = _dotEl1.getPremia();

                    if (_mth_getPremiaResult1 != null) {
                        ocb.activate(_mth_getPremiaResult1, 1);
                    }

                    if (_mth_getPremiaResult1 != null) {
                        ocb.activate(_mth_getPremiaResult1, 1);
                    }

                    _dotResult1.add(_mth_getPremiaResult1);
                    _dotIndex1++;
                }
            }

            java.lang.Double _avgResult = 0d;

            if ((_dotResult1 != null) && !_dotResult1.isEmpty()) {
                Number _avgSum0 = null;

                for (Number _avgEl0 : _dotResult1) {
                    _avgSum0 = MathUtils.sum(_avgSum0, _avgEl0);
                }

                _avgResult = _avgSum0.doubleValue() / _dotResult1.size();
            }

            java.lang.Double _asResult_averagepilkarz = _avgResult;
            pl.wcislo.sbql4j.java.model.runtime.Struct _commaResult = OperatorUtils.cartesianProductSS(_asResult_minimumpilkarz,
                    _asResult_averagepilkarz, "minimumpilkarz", "averagepilkarz");
            java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _mth_getLista_zawodnikowResult2 =
                _joinEl.getLista_zawodnikow();

            if (_mth_getLista_zawodnikowResult2 != null) {
                ocb.activate(_mth_getLista_zawodnikowResult2, 2);
            }

            java.util.List<java.lang.Integer> _dotResult2 = new java.util.ArrayList<java.lang.Integer>();
            int _dotIndex2 = 0;

            if (_mth_getLista_zawodnikowResult2 != null) {
                for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl2 : _mth_getLista_zawodnikowResult2) {
                    if (_dotEl2 == null) {
                        continue;
                    }

                    if (_dotEl2 != null) {
                        ocb.activate(_dotEl2, 1);
                    }

                    java.lang.Integer _mth_getPremiaResult2 = _dotEl2.getPremia();

                    if (_mth_getPremiaResult2 != null) {
                        ocb.activate(_mth_getPremiaResult2, 1);
                    }

                    if (_mth_getPremiaResult2 != null) {
                        ocb.activate(_mth_getPremiaResult2, 1);
                    }

                    _dotResult2.add(_mth_getPremiaResult2);
                    _dotIndex2++;
                }
            }

            Number _max0 = null;

            for (Number _maxEl0 : _dotResult2) {
                _max0 = MathUtils.max(_max0, _maxEl0);
            }

            java.lang.Integer _maxResult = (java.lang.Integer) _max0;
            java.lang.Integer _asResult_maximumpilkarz = _maxResult;
            pl.wcislo.sbql4j.java.model.runtime.Struct _commaResult1 = OperatorUtils.cartesianProductSS(_commaResult,
                    _asResult_maximumpilkarz, "", "maximumpilkarz");
            java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Lekarz> _mth_getListalekarzyResult =
                _joinEl.getListalekarzy();

            if (_mth_getListalekarzyResult != null) {
                ocb.activate(_mth_getListalekarzyResult, 2);
            }

            java.util.List<java.lang.Integer> _dotResult3 = new java.util.ArrayList<java.lang.Integer>();
            int _dotIndex3 = 0;

            if (_mth_getListalekarzyResult != null) {
                for (pl.wcislo.sbql4j.javac.test.football.model.Lekarz _dotEl3 : _mth_getListalekarzyResult) {
                    if (_dotEl3 == null) {
                        continue;
                    }

                    if (_dotEl3 != null) {
                        ocb.activate(_dotEl3, 1);
                    }

                    java.lang.Integer _mth_getPremiaResult3 = _dotEl3.getPremia();

                    if (_mth_getPremiaResult3 != null) {
                        ocb.activate(_mth_getPremiaResult3, 1);
                    }

                    if (_mth_getPremiaResult3 != null) {
                        ocb.activate(_mth_getPremiaResult3, 1);
                    }

                    _dotResult3.add(_mth_getPremiaResult3);
                    _dotIndex3++;
                }
            }

            Number _min1 = null;

            for (Number _minEl1 : _dotResult3) {
                _min1 = MathUtils.min(_min1, _minEl1);
            }

            java.lang.Integer _minResult1 = (java.lang.Integer) _min1;
            java.lang.Integer _asResult_minimumlekarz = _minResult1;
            pl.wcislo.sbql4j.java.model.runtime.Struct _commaResult2 = OperatorUtils.cartesianProductSS(_commaResult1,
                    _asResult_minimumlekarz, "", "minimumlekarz");
            java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Lekarz> _mth_getListalekarzyResult1 =
                _joinEl.getListalekarzy();

            if (_mth_getListalekarzyResult1 != null) {
                ocb.activate(_mth_getListalekarzyResult1, 2);
            }

            java.util.List<java.lang.Integer> _dotResult4 = new java.util.ArrayList<java.lang.Integer>();
            int _dotIndex4 = 0;

            if (_mth_getListalekarzyResult1 != null) {
                for (pl.wcislo.sbql4j.javac.test.football.model.Lekarz _dotEl4 : _mth_getListalekarzyResult1) {
                    if (_dotEl4 == null) {
                        continue;
                    }

                    if (_dotEl4 != null) {
                        ocb.activate(_dotEl4, 1);
                    }

                    java.lang.Integer _mth_getPremiaResult4 = _dotEl4.getPremia();

                    if (_mth_getPremiaResult4 != null) {
                        ocb.activate(_mth_getPremiaResult4, 1);
                    }

                    if (_mth_getPremiaResult4 != null) {
                        ocb.activate(_mth_getPremiaResult4, 1);
                    }

                    _dotResult4.add(_mth_getPremiaResult4);
                    _dotIndex4++;
                }
            }

            java.lang.Double _avgResult1 = 0d;

            if ((_dotResult4 != null) && !_dotResult4.isEmpty()) {
                Number _avgSum1 = null;

                for (Number _avgEl1 : _dotResult4) {
                    _avgSum1 = MathUtils.sum(_avgSum1, _avgEl1);
                }

                _avgResult1 = _avgSum1.doubleValue() / _dotResult4.size();
            }

            java.lang.Double _asResult_averagelekarz = _avgResult1;
            pl.wcislo.sbql4j.java.model.runtime.Struct _commaResult3 = OperatorUtils.cartesianProductSS(_commaResult2,
                    _asResult_averagelekarz, "", "averagelekarz");
            java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Lekarz> _mth_getListalekarzyResult2 =
                _joinEl.getListalekarzy();

            if (_mth_getListalekarzyResult2 != null) {
                ocb.activate(_mth_getListalekarzyResult2, 2);
            }

            java.util.List<java.lang.Integer> _dotResult5 = new java.util.ArrayList<java.lang.Integer>();
            int _dotIndex5 = 0;

            if (_mth_getListalekarzyResult2 != null) {
                for (pl.wcislo.sbql4j.javac.test.football.model.Lekarz _dotEl5 : _mth_getListalekarzyResult2) {
                    if (_dotEl5 == null) {
                        continue;
                    }

                    if (_dotEl5 != null) {
                        ocb.activate(_dotEl5, 1);
                    }

                    java.lang.Integer _mth_getPremiaResult5 = _dotEl5.getPremia();

                    if (_mth_getPremiaResult5 != null) {
                        ocb.activate(_mth_getPremiaResult5, 1);
                    }

                    if (_mth_getPremiaResult5 != null) {
                        ocb.activate(_mth_getPremiaResult5, 1);
                    }

                    _dotResult5.add(_mth_getPremiaResult5);
                    _dotIndex5++;
                }
            }

            Number _max1 = null;

            for (Number _maxEl1 : _dotResult5) {
                _max1 = MathUtils.max(_max1, _maxEl1);
            }

            java.lang.Integer _maxResult1 = (java.lang.Integer) _max1;
            java.lang.Integer _asResult_maximumlekarz = _maxResult1;
            pl.wcislo.sbql4j.java.model.runtime.Struct _commaResult4 = OperatorUtils.cartesianProductSS(_commaResult3,
                    _asResult_maximumlekarz, "", "maximumlekarz");
            _joinResult.add(OperatorUtils.cartesianProductSS(_joinEl,
                    _commaResult4, "", ""));
            _joinIndex++;
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_joinResult, ocb);

        return _joinResult;
    }
}
