package pl.wcislo.sbql4j.javac.test.db4o;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.javac.test.linq_comp.model.Product;

import com.db4o.ObjectSet;

public class Db4oOperatorTestCase {
	private Db4oOperatorTest test = new Db4oOperatorTest();
	
	@Test
	public void testAll1() {
		Boolean resQuery = test.testAll1();
		Assert.assertTrue(resQuery.equals(Boolean.TRUE));
	}
	
	@Test
	public void testAll2() {
		Boolean resQuery = test.testAll2();
		Assert.assertTrue(resQuery.equals(Boolean.TRUE));
	}
	@Test
	public void testAnd1() {
		Boolean resQuery = test.testAnd1();
		Assert.assertTrue(resQuery.equals(Boolean.FALSE));
	}
	@Test
	public void testAny1() {
		Boolean resQuery = test.testAny1();
		Assert.assertTrue(resQuery.equals(Boolean.TRUE));
	}
	@Test
	public void testAny2() {
		Boolean resQuery = test.testAny2();
		Assert.assertTrue(resQuery.equals(Boolean.TRUE));
	}
	@Test
	public void testBag1() {
		Collection<Integer> res = test.testBag1();
		Assert.assertTrue(res.size() == 2);
		Iterator<Integer> it = res.iterator();
		Assert.assertTrue(it.next() == 1);
		Assert.assertTrue(it.next() == 2);
	}
	@Test
	public void testBag2() {
		Collection<Integer> res = test.testBag2();
		Assert.assertTrue(res.size() == 1);
		Iterator<Integer> it = res.iterator();
		Assert.assertTrue(it.next() == 1);
	}
	@Test
	public void testBag3() {
		Collection<Integer> res = test.testBag3();
		Assert.assertTrue(res.size() == 2);
		Iterator<Integer> it = res.iterator();
		Assert.assertTrue(it.next() == 3);
		Assert.assertTrue(it.next() == 3);
	}
	@Test
	public void testBag4() {
		Collection<Integer> res = test.testBag4();
		Assert.assertTrue(res.size() == 3);
		Iterator<Integer> it = res.iterator();
		Assert.assertTrue(it.next() == 1);
		Assert.assertTrue(it.next() == 2);
		Assert.assertTrue(it.next() == 3);
	}
	@Test
	public void testComma1() {
		Struct res = test.testComma1();
		Assert.assertTrue(res.size() == 2);
		Iterator<Integer> it = res.values().iterator();
		Assert.assertTrue(it.next() == 1);
		Assert.assertTrue(it.next() == 2);
	}
	@Test
	public void testComma2() {
		Collection<Struct> res = test.testComma2();
		Assert.assertTrue(res.size() == 2);
		Iterator<Struct> it = res.iterator();
		Struct s1 = it.next();
		Assert.assertTrue(s1.size() == 2);
		Iterator<Integer> si = s1.valueList().iterator();
		Assert.assertTrue(si.next() == 1);
		Assert.assertTrue(si.next() == 3);
		s1 = it.next();
		Assert.assertTrue(s1.size() == 2);
		si = s1.valueList().iterator();
		Assert.assertTrue(si.next() == 2);
		Assert.assertTrue(si.next() == 3);
	}
	@Test
	public void testComma3() {
		Collection<Struct> res = test.testComma3();
		Assert.assertTrue(res.size() == 4);
		Iterator<Struct> it = res.iterator();
		Struct s1 = it.next();
		Assert.assertTrue(s1.size() == 2);
		Iterator<Integer> si = s1.valueList().iterator();
		Assert.assertTrue(si.next() == 1);
		Assert.assertTrue(si.next() == 3);
		s1 = it.next();
		Assert.assertTrue(s1.size() == 2);
		si = s1.valueList().iterator();
		Assert.assertTrue(si.next() == 1);
		Assert.assertTrue(si.next() == 4);
		s1 = it.next();
		Assert.assertTrue(s1.size() == 2);
		si = s1.valueList().iterator();
		Assert.assertTrue(si.next() == 2);
		Assert.assertTrue(si.next() == 3);
		s1 = it.next();
		Assert.assertTrue(s1.size() == 2);
		si = s1.valueList().iterator();
		Assert.assertTrue(si.next() == 2);
		Assert.assertTrue(si.next() == 4);
	}
	@Test
	public void testDivide1() {
		Integer res = test.testDivide1();
		Assert.assertTrue(res == 2);
	}
	
	@Test
	public void testDivide2() {
		Double res = test.testDivide2();
		Assert.assertTrue(res == (5/3.5));
	}
	@Test
	public void testDivide3() {
		Double res = test.testDivide3();
		Assert.assertTrue(res == (3.5/5));
	}
	@Test
	public void testDivide4() {
		Double res = test.testDivide4();
		Assert.assertTrue(res == (3.5/5.5));
	}
	@Test
	public void testDot1() {
		Collection<String> res = test.testDot1();
		Assert.assertTrue(res.size() == 2);
		Iterator<String> it = res.iterator();
		Assert.assertTrue(it.next().equals("al. Jerozolimskie"));
		Assert.assertTrue(it.next().equals("Koszykowa"));
	}
	@Test
	public void testDot2() {
		Collection<String> res = test.testDot2();
		Assert.assertTrue(res.size() == 2);
		Iterator<String> it = res.iterator();
		Assert.assertTrue(it.next().equals("Ala"));
		Assert.assertTrue(it.next().equals("Ala"));
	}
	
	@Test
	public void testDot4() {
		Collection<String> res = test.testDot4();
		Assert.assertTrue(res.size() == 2);
		Iterator<String> it = res.iterator();
		Assert.assertTrue(it.next().equals("Ala"));
		Assert.assertTrue(it.next().equals("Ala"));
	}
	@Test
	public void testDot5() {
		Collection<String> res = test.testDot5();
		Assert.assertTrue(res.size() == 2);
		Iterator<String> it = res.iterator();
		Assert.assertTrue(it.next().equals("Ala"));
		Assert.assertTrue(it.next().equals("Ala"));
	}
	
	@After
	public void close() {
		test.getConnection().close();
	}
}
