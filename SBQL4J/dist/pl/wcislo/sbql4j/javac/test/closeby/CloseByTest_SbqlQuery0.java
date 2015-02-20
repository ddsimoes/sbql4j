package pl.wcislo.sbql4j.javac.test.closeby;

import org.apache.commons.collections.CollectionUtils;

import pl.wcislo.sbql4j.exception.*;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.runtime.*;
import pl.wcislo.sbql4j.java.model.runtime.factory.*;
import pl.wcislo.sbql4j.java.test.*;
import pl.wcislo.sbql4j.java.test.model.*;
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
import pl.wcislo.sbql4j.lang.xml.*;
import pl.wcislo.sbql4j.model.*;
import pl.wcislo.sbql4j.model.collections.*;
import pl.wcislo.sbql4j.util.*;
import pl.wcislo.sbql4j.util.Utils;
import pl.wcislo.sbql4j.xml.model.*;
import pl.wcislo.sbql4j.xml.parser.store.*;

import java.io.File;
import java.io.IOException;

import java.util.*;
import java.util.ArrayList;
import java.util.List;


public class CloseByTest_SbqlQuery0 {
    private java.util.List<pl.wcislo.sbql4j.java.test.model.Czesc> cList;

    public CloseByTest_SbqlQuery0(
        final java.util.List<pl.wcislo.sbql4j.java.test.model.Czesc> cList) {
        this.cList = cList;
    }

    /**
     * original query='cList close by skladniki.prowadziDo'
     *
     * query after optimization='cList close by getSkladniki().getProwadziDo()'
    */
    public java.util.List<pl.wcislo.sbql4j.java.test.model.Czesc> executeQuery() {
        java.util.List<pl.wcislo.sbql4j.java.test.model.Czesc> _ident_cList = cList;
        java.util.List<pl.wcislo.sbql4j.java.test.model.Czesc> _queryResult = new ArrayList<pl.wcislo.sbql4j.java.test.model.Czesc>();
        _queryResult.addAll(_ident_cList);

        int _i0 = 0;

        while (_i0 < _queryResult.size()) {
            pl.wcislo.sbql4j.java.test.model.Czesc _closeByEl = _queryResult.get(_i0);
            java.util.List<pl.wcislo.sbql4j.java.test.model.Czesc.Skladnik> _mth_getSkladnikiResult =
                _closeByEl.getSkladniki();
            java.util.List<pl.wcislo.sbql4j.java.test.model.Czesc> _dotResult = new java.util.ArrayList<pl.wcislo.sbql4j.java.test.model.Czesc>();
            int _dotIndex = 0;

            if (_mth_getSkladnikiResult != null) {
                for (pl.wcislo.sbql4j.java.test.model.Czesc.Skladnik _dotEl : _mth_getSkladnikiResult) {
                    if (_dotEl == null) {
                        continue;
                    }

                    pl.wcislo.sbql4j.java.test.model.Czesc _mth_getProwadziDoResult =
                        _dotEl.getProwadziDo();
                    _dotResult.add(_mth_getProwadziDoResult);
                    _dotIndex++;
                }
            }

            _queryResult.addAll(_dotResult);
            _i0++;
        }

        return _queryResult;
    }
}
