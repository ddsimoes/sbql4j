package pl.wcislo.sbql4j.javac.test.football.query;

import com.db4o.ObjectContainer;

import org.apache.commons.collections.CollectionUtils;

import pl.wcislo.sbql4j.exception.*;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.runtime.*;
import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.java.model.runtime.factory.*;
import pl.wcislo.sbql4j.java.utils.ArrayUtils;
import pl.wcislo.sbql4j.java.utils.OperatorUtils;
import pl.wcislo.sbql4j.java.utils.Pair;
import pl.wcislo.sbql4j.javac.test.football.data.*;
import pl.wcislo.sbql4j.javac.test.football.model.*;
import pl.wcislo.sbql4j.javac.test.football.model.Druzyna;
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


public class KORQueries_SbqlQuery7 {
    private com.db4o.ObjectContainer dbConn;
    private java.lang.String wiek;

    public KORQueries_SbqlQuery7(final com.db4o.ObjectContainer dbConn,
        final java.lang.String wiek) {
        this.dbConn = dbConn;
        this.wiek = wiek;
    }

    /**
     * original query='dbConn.(
                                    Druzyna where nazwa==wiek
                                    join min(lista_zawodnikow.premia) as minimumpilkarz,
                                    avg(lista_zawodnikow.premia) as averagepilkarz,
                                    max(lista_zawodnikow.premia) as maximumpilkarz,
                                     min(listalekarzy.premia) as minimumlekarz,
                                    avg(listalekarzy.premia) as averagelekarz,
                                    max(listalekarzy.premia) as maximumlekarz
                            )'
     *
     * query after optimization='dbConn.(Druzyna where getNazwa() == wiek join  min(getLista_zawodnikow().getPremia()) as minimumpilkarz,  avg(getLista_zawodnikow().getPremia()) as averagepilkarz,  max(getLista_zawodnikow().getPremia()) as maximumpilkarz,  min(getListalekarzy().getPremia()) as minimumlekarz,  avg(getListalekarzy().getPremia()) as averagelekarz,  max(getListalekarzy().getPremia()) as maximumlekarz)'
    */
    public java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> executeQuery() {
        com.db4o.ObjectContainer _ident_dbConn = dbConn;
        java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> _queryResult =
            _ident_dbConn.query(new KORQueries_SbqlQuery7Db4o0(wiek));

        return _queryResult;
    }
}
