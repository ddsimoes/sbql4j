package pl.wcislo.sbql4j.javac.test.football.query;

import java.lang.String;
import com.db4o.ObjectContainer;
import java.util.*;
import pl.wcislo.sbql4j.javac.test.football.model.*;
import pl.wcislo.sbql4j.javac.test.football.data.*;
import java.text.ParseException;
import java.lang.Integer;
import pl.wcislo.sbql4j.java.model.runtime.Struct;

public class KORQueries {
    private ExampleData data;
    private ObjectContainer dbConn;
    
    public static void main(String[] args) {
        KORQueries q = new KORQueries();
    }
    
    public KORQueries() {
        super();
        data = new ExampleData();
        dbConn = data.getConnection();
    }
    
    /**
     * QUERY 1 : Wyszukaj pilkarzy po imieniu, nazwisku lub po imieniu lub po nazwisku i posortuj po nazwie druzyny 
     */
    public Collection QueryImiePilkarza(String u, String ut) {
        Collection<Pilkarz> allCars = null;
        if (u != "" || ut != "") {
            if (ut != "" && u != "") {
                allCars = new KORQueries_SbqlQuery0(dbConn,u,ut).executeQuery();
            }
            if (ut != "" && u == "") {
                allCars = new KORQueries_SbqlQuery1(dbConn,ut).executeQuery();
            }
            if (u != "" && ut == "") {
                allCars = new KORQueries_SbqlQuery2(dbConn,u).executeQuery();
            }
        } else {
            allCars = new KORQueries_SbqlQuery3(dbConn).executeQuery();
        }
        return allCars;
    }
    
    /**
     * QUERY 2 : Wyszukaj druzyne  po nazwie druzyny 
     */
    public Collection QueryDruzyna(String u) {
        Collection<Druzyna> allCars = new KORQueries_SbqlQuery4(dbConn,u).executeQuery();
        return allCars;
    }
    
    /**
     * QUERY 3 : Zlicz mecze dwoch druzyn jako gospodarze i porownaj ich ilosc i zwroc czy ilosc zgadza sie
     */
    public boolean QueryDruzynaGospodarzeCount(String druzyna1, String druzyna2) {
        boolean b = new KORQueries_SbqlQuery5(dbConn,druzyna1).executeQuery();
        return b;
    }
    
    /**
     *  QUERY 4 : Wyszukaj kontuzji u pilkarza w danym wieku i wyswietl je
     */
    public Collection QueryKontuzja(int wiek2) {
        Collection<Kontuzja> allCars = new KORQueries_SbqlQuery6(dbConn,wiek2).executeQuery();
        return allCars;
    }
    
    /**
     *  QUERY 5 : Wylicz srednia,maksymalna i minimalna premie    pilkarzy , i lekarzy z druzyny.
     */
    public Collection<Struct> QuerySrednia(String wiek) {
        Collection<Struct> deptAvgSal = new KORQueries_SbqlQuery7(dbConn,wiek).executeQuery();
        return deptAvgSal;
    }
    
    /**
     *  QUERY 6 : Wylicz maksymalna pensje wraz z premia wsrod pilkarzy dostepnych w bazie.
     */
    public double QueryPensja() {
        double pi = new KORQueries_SbqlQuery8(dbConn).executeQuery();
        return pi;
    }
    
    /**
     * QUERY 7 : Wylicz srednia ilosc kartek zoltych i czerwonych w meczach .
     */
    public Struct QueryKartkiNew() {
        Struct deptAvgSal = new KORQueries_SbqlQuery9(dbConn).executeQuery();
        return deptAvgSal;
    }
    
    /**
     *  QUERY 8 : Wylicz liczb\u0119 meczów a nast\u0119pnie wylicz procentowy udzia\u0142 stadionow do rozgrywanych wszystkich meczow.
     */
    public float QueryMeczStadion() {
        int mecze = new KORQueries_SbqlQuery10(dbConn).executeQuery();
        int stadiony = new KORQueries_SbqlQuery11(dbConn).executeQuery();
        float percentage = (stadiony * 100 / mecze);
        return percentage;
    }
    
    /**
     * QUERY 9 : Wylicz liczbe bramek druzyny jako gosci i jako gospodarzy a nastepnie wylicz procent zdobytych bramek do wszystkich zdobytych bramek w wszystkich meczach.
     */
    public float QueryBramkiDruzyny(String druzyna1) {
        Collection<String> m = new KORQueries_SbqlQuery12(dbConn,druzyna1).executeQuery();
        ArrayList<String> meczejakogosp = (ArrayList<String>)m;
        Collection<String> me = new KORQueries_SbqlQuery13(dbConn,druzyna1).executeQuery();
        ArrayList<String> meczejakogosc = (ArrayList<String>)me;
        int golejakogosp = 0;
        int golejakogosc = 0;
        String wynikmeczu;
        int wynikzmeczu;
        for (String p : meczejakogosp) {
            wynikmeczu = p;
            int pos = wynikmeczu.indexOf(":");
            wynikzmeczu = Integer.parseInt(wynikmeczu.substring(0, pos));
            golejakogosp = golejakogosp + wynikzmeczu;
        }
        for (String p : meczejakogosc) {
            wynikmeczu = p;
            int pos = wynikmeczu.indexOf(":");
            wynikzmeczu = Integer.parseInt(wynikmeczu.substring(pos + 1, wynikmeczu.length()));
            golejakogosc = golejakogosc + wynikzmeczu;
        }
        int gole = golejakogosc + golejakogosp;
        Collection<String> c = new KORQueries_SbqlQuery14(dbConn).executeQuery();
        ArrayList<String> mecze = (ArrayList<String>)c;
        int goleall = 0;
        for (String p : mecze) {
            wynikmeczu = p;
            int pos = wynikmeczu.indexOf(":");
            goleall = goleall + Integer.parseInt(wynikmeczu.substring(pos + 1, wynikmeczu.length()));
            int pos2 = wynikmeczu.indexOf(":");
            goleall = goleall + Integer.parseInt(wynikmeczu.substring(0, pos2));
        }
        float percentage = (gole * 100 / (goleall));
        return percentage;
    }
    
    /**
     * QUERY 10 : Sprawdz ile razy druzyna posiadala czyste konto w meczach.
     */
    public int DruzynaCzysteKonto(String nazwadruzyny) {
        Collection<String> meczejakogospc = new KORQueries_SbqlQuery15(dbConn,nazwadruzyny).executeQuery();
        ArrayList<String> meczejakogosp = (ArrayList<String>)meczejakogospc;
        Collection<String> meczejakogosc2 = new KORQueries_SbqlQuery16(dbConn,nazwadruzyny).executeQuery();
        ArrayList<String> meczejakogosc = (ArrayList<String>)meczejakogosc2;
        int iloscczystegokonta = 0;
        for (String p : meczejakogosp) {
            String wynikmeczu = p;
            int pos2 = wynikmeczu.indexOf(":");
            int gol = Integer.parseInt(wynikmeczu.substring(0, pos2));
            if (gol == 0) {
                iloscczystegokonta = iloscczystegokonta + 1;
            }
        }
        for (String p : meczejakogosc) {
            String wynikmeczu2 = p;
            int pos = wynikmeczu2.indexOf(":");
            int gol2 = Integer.parseInt(wynikmeczu2.substring(pos + 1, wynikmeczu2.length()));
            if (gol2 == 0) {
                iloscczystegokonta = iloscczystegokonta + 1;
            }
        }
        return iloscczystegokonta;
    }
    
    /**
     * QUERY 11 : Sprawdz na którym stadionie pada najwiecej bramek
     */
    public String QueryStadionBramki() {
        String nazwastadion = "";
        Collection<String> stadiony2 = new KORQueries_SbqlQuery17(dbConn).executeQuery();
        ArrayList<String> stadiony = (ArrayList<String>)stadiony2;
        int golemax = 0;
        for (String nazwast : stadiony) {
            Collection<String> wynikizstadionu2 = new KORQueries_SbqlQuery18(dbConn,nazwast).executeQuery();
            ArrayList<String> wynikizstadionu = (ArrayList<String>)wynikizstadionu2;
            int goleall = 0;
            for (String wynikmeczu2 : wynikizstadionu) {
                String wynikmeczu = wynikmeczu2;
                int pos = wynikmeczu.indexOf(":");
                goleall = goleall + Integer.parseInt(wynikmeczu.substring(pos + 1, wynikmeczu.length()));
                int pos2 = wynikmeczu.indexOf(":");
                goleall = goleall + Integer.parseInt(wynikmeczu.substring(0, pos2));
                if (goleall > golemax) {
                    nazwastadion = nazwast;
                    goleall = golemax;
                }
            }
        }
        return nazwastadion;
    }
    
    /**
     * QUERY 12 : Wyswietl druzyny i ich  liczbe zoltych i czerwonych kartek zsumowana
     */
    public Collection<Struct> DruzynaNajbrutalniejsza() {
        Collection<Struct> dr = new KORQueries_SbqlQuery19(dbConn).executeQuery();
        return dr;
    }
    
    /**
     * QUERY 13 : Wyswietl przedzial pensji u  pilkarzy 
     */
    public Collection<String> PilkarzePensjeprzedzial() {
        Collection<String> res = new KORQueries_SbqlQuery20(dbConn).executeQuery();
        return res;
    }
    
    /**
     * QUERY 14 : Wyswietl przedzial golii u  pilkarzy 
     */
    public Collection<String> PilkarzeIloscGoliPrzedzial() {
        Collection<String> res = new KORQueries_SbqlQuery21(dbConn).executeQuery();
        return res;
    }
    
    /**
     * QUERY 14 : Wyswietl przedzial czerwonych kartek u  pilkarzy 
     */
    public Collection<String> PilkarzCzerwoneKartkiPrzedzial() {
        Collection<String> res = new KORQueries_SbqlQuery22(dbConn).executeQuery();
        return res;
    }
    
    /**
     * QUERY 15 : Wyswietl przedzial premii u trenerow  
     */
    public Collection<String> TrenerPremia() {
        Collection<String> res = new KORQueries_SbqlQuery23(dbConn).executeQuery();
        return res;
    }
    
    /**
     * QUERY 16 :Wyswietl druzyny
     */
    public Collection<String> DruzynyAll() {
        Collection<String> allCars = new KORQueries_SbqlQuery24(dbConn).executeQuery();
        return allCars;
    }
    
    /**
     * QUERY 17 : Wyswietl obozy
     */
    public Collection<Oboz> ObozAll() {
        Collection<Oboz> allCars = new KORQueries_SbqlQuery25(dbConn).executeQuery();
        return allCars;
    }
    
    /**
     * QUERY 18 : Sprawdz czy druzyna miala oboz 
     */
    public Collection<Struct> CheckObozDruzyna(String nazwadruzyny) {
        Collection<Struct> b = new KORQueries_SbqlQuery26(dbConn,nazwadruzyny).executeQuery();
        return b;
    }
    
    /**
     * QUERY 19 : Wyswietl stroj druzyny 
     */
    public Collection<Struct> StrojDruzyny(String nazwadruzyny) {
        Collection<Struct> allCars = new KORQueries_SbqlQuery27(dbConn,nazwadruzyny).executeQuery();
        return allCars;
    }
    
    /**
     * QUERY 20 : Wyswietl zabiegi pilkarzy  
     */
    public Collection<Struct> ZabiegiPilkarzy() {
        Collection<Struct> allCars = new KORQueries_SbqlQuery28(dbConn).executeQuery();
        return allCars;
    }
    
    /**
     * QUERY 21 : Sprawdz czy prawda jest ze w druzynach nie ma zawodnika ktory ma premie 0
     */
    public Boolean SprawdzPilkarzePremiaZero() {
        Boolean res = new KORQueries_SbqlQuery29(dbConn).executeQuery();
        return res;
    }
    
    /**
     * QUERY 22 : Sprawdz czy prawda jest ze jest druzyna która ma pilkarza w wieku wiekszego niz podany
     * 
     */
    public Boolean SprawdzDruzynaPilkarzeWiek(int wiek2) {
        Boolean res = new KORQueries_SbqlQuery30(dbConn,wiek2).executeQuery();
        return res;
    }
    
    /**
     * QUERY 23: Sprawdz czy istnieja druzyny ktorego srednia wieku pilkarzy w druzynie nie przekracza podanego przedzialu i  istnieje
     *  pilkarz ktory ma kontuzje
     */
    public Collection<Druzyna> query22() {
        Collection<Druzyna> res = new KORQueries_SbqlQuery31(dbConn).executeQuery();
        return res;
    }
    
    /**
     * QUERY 24: Sprawdz czy istnieja druzyny ktorego srednia premia  pilkarzy w druzynie nie przekracza podanego przedzialu i
     *  druzyna zagrala na wszystkich stadionach
     */
    public Collection<Druzyna> query23() {
        Collection<Druzyna> res = new KORQueries_SbqlQuery32(dbConn).executeQuery();
        return res;
    }
    
    /**
     * QUERY 25 : Query na optymalizacje : (Wyswietl miasto druzyn) - z martwe podzapytanie
     */
    public Collection<String> Optymalizacja1(String nazwadruzyny) {
        Collection<String> allCars = new KORQueries_SbqlQuery33(dbConn).executeQuery();
        return allCars;
    }
    
    /**
     * QUERY 26 : Query na optymalizacje2 - indeks ( Wyszukaj pilkarza ktory w meczu strzelil 5 goli)
     */
    public Collection<PilkarzMecz> Optymalizacja2(String nazwadruzyny) {
        Collection<PilkarzMecz> allCars = new KORQueries_SbqlQuery34(dbConn).executeQuery();
        return allCars;
    }
    
    /**
     * QUERY 27 : Query na optymalizacje  niezalezne zapytania (Wyciagniecie nazw druzyny gdzie premia > sredniej premi lekarzy 
     */
    public Collection<Lekarz> optymalizacje3(String nazwadruzyny) {
        Collection<Lekarz> res = new KORQueries_SbqlQuery35(dbConn).executeQuery();
        return res;
    }
    
    /**
     * QUERY 28 : blad  (Gdy w klasie jest lista z elementami na przyklad
     * listazawodnikow to nie mozna sie dostac do pola ktore jest w klasie ktora rozszerza u mnie na przyklad :
     * Klasa Osoba rozszerza Pilkarza i nie mozna sie dostac do pola pensja ktore jest dziedziczone z osoboy
     */
    public boolean queryEquals(String nazwadruzyny, String nazwadruzyny2, boolean equals) {
        boolean eq;
        double count1 = new KORQueries_SbqlQuery36(dbConn,nazwadruzyny).executeQuery();
        double count2 = new KORQueries_SbqlQuery37(dbConn,nazwadruzyny2).executeQuery();
        if (equals == true) {
            eq = new KORQueries_SbqlQuery38(count1,count2).executeQuery();
        } else {
            eq = new KORQueries_SbqlQuery39(count1,count2).executeQuery();
        }
        return eq;
    }
    
    public ArrayList QueryPom1ForQuery3() {
        Collection<Druzyna> allCars2 = new KORQueries_SbqlQuery40(dbConn).executeQuery();
        ArrayList<Druzyna> allCars = (ArrayList<Druzyna>)allCars2;
        return allCars;
    }
    
    public double QueryPom2ForQuery3(String nazwadruzyny) {
        double allCars = new KORQueries_SbqlQuery41(dbConn,nazwadruzyny).executeQuery();
        return allCars;
    }
}