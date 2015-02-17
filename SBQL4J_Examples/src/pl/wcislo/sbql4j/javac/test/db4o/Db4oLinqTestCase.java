package pl.wcislo.sbql4j.javac.test.db4o;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.javac.test.linq_comp.model.Customer;
import pl.wcislo.sbql4j.javac.test.linq_comp.model.Order;
import pl.wcislo.sbql4j.javac.test.linq_comp.model.Product;

import com.db4o.ObjectSet;

public class Db4oLinqTestCase {
	private Db4oLinqTest test = new Db4oLinqTest();
	
	@Test
	public void testLinq2() {
		Collection<Product> resQuery = test.linq2();
		ObjectSet<Product> res = test.getConnection().query(Product.class);
		Collection<Product> resTest = new ArrayList<Product>(res);
		for(Iterator<Product> it=resTest.iterator(); it.hasNext(); ) {
			Product p = it.next();
			if(p.unitsInStock == 0) {
				continue;
			} else {
				it.remove();
			}
		}
		Assert.assertTrue(resQuery.containsAll(resTest) && resTest.containsAll(resQuery));
	}
	
	@Test
	public void testLinq3() {
		Collection<Product> resQuery = test.linq3();
		ObjectSet<Product> res = test.getConnection().query(Product.class);
		Collection<Product> resTest = new ArrayList<Product>(res);
		for(Iterator<Product> it=resTest.iterator(); it.hasNext(); ) {
			Product p = it.next();
			if(p.unitsInStock > 0 && p.unitPrice > 3.00) {
				continue;
			} else {
				it.remove();
			}
		}
		Assert.assertTrue(resQuery.containsAll(resTest) && resTest.containsAll(resQuery));
	}
	
	@Test
	public void testLinq4() {
		Collection<Customer> resQuery = test.linq4();
		ObjectSet<Customer> res = test.getConnection().query(Customer.class);
		Collection<Customer> resTest = new ArrayList<Customer>(res);
		for(Iterator<Customer> it=resTest.iterator(); it.hasNext(); ) {
			Customer c = it.next();
			if("WA".equals(c.region)) {
				continue;
			} else {
				it.remove();
			}
		}
		Assert.assertTrue(resQuery.containsAll(resTest) && resTest.containsAll(resQuery));
	}
	
	@Test
	public void testLinq7() {
		Collection<String> resQuery = test.linq7();
		ObjectSet<Product> res = test.getConnection().query(Product.class);
		Collection<String> resTest = new ArrayList<String>();
		for(Iterator<Product> it=res.iterator(); it.hasNext(); ) {
			Product p = it.next();
			resTest.add(p.productName);
		}
		Assert.assertTrue(resQuery.containsAll(resTest) && resTest.containsAll(resQuery));
	}
	
	@Test
	public void testLinq11() {
		Collection<Struct> resQuery = test.linq11();
		ObjectSet<Product> res = test.getConnection().query(Product.class);
		Collection<Struct> resTest = new ArrayList<Struct>();
		for(Iterator<Product> it=res.iterator(); it.hasNext(); ) {
			Product p = it.next();
			Struct s = new Struct();
			s.put(0, "productName", p.productName);
			s.put(1, "category", p.category);
			s.put(2, "price", p.unitPrice);
			resTest.add(s);
		}
		Assert.assertTrue(resQuery.containsAll(resTest) && resTest.containsAll(resQuery));
	}
	
	@Test
	public void testLinq15() {
		Collection<Struct> resQuery = test.linq15();
		ObjectSet<Customer> res = test.getConnection().query(Customer.class);
		Collection<Struct> resTest = new ArrayList<Struct>();
		for(Iterator<Customer> it=res.iterator(); it.hasNext(); ) {
			Customer p = it.next();
			if(p.orders != null) {
				for(Order o : p.orders) {
					if(o.total < 500.00) {
						Struct s = new Struct();
						s.put(0, "customerId", p.customerID);
						s.put(1, "orderID", o.orderID);
						s.put(2, "total", o.total);
						resTest.add(s);
					}
				}
			}
		}
//		List l1 = new ArrayList();
//		l1.addAll(resQuery);
//		l1.removeAll(resTest);
//		
//		List l2 = new ArrayList();
//		l2.addAll(resTest);
//		l2.removeAll(resQuery);
		
		Assert.assertTrue(resQuery.containsAll(resTest) && resTest.containsAll(resQuery));
	}

	@Test
	public void testLinq16() {
		Collection<Struct> resQuery = test.linq16();
		ObjectSet<Customer> res = test.getConnection().query(Customer.class);
		Collection<Struct> resTest = new ArrayList<Struct>();
		Calendar c = Calendar.getInstance();
    	c.set(1998, Calendar.JANUARY, 1);
    	Date d = c.getTime();
		for(Iterator<Customer> it=res.iterator(); it.hasNext(); ) {
			Customer p = it.next();
			if(p.orders != null) {
				for(Order o : p.orders) {
					if(o.orderDate.after(d)) {
						Struct s = new Struct();
						s.put(0, "customerID", p.customerID);
						s.put(1, "orderID", o.orderID);
						s.put(2, "orderDate", o.orderDate);
						resTest.add(s);
					}
				}
			}
		}
//		List l1 = new ArrayList();
//		l1.addAll(resQuery);
//		l1.removeAll(resTest);
//		
//		List l2 = new ArrayList();
//		l2.addAll(resTest);
//		l2.removeAll(resQuery);
		
		Assert.assertTrue(resQuery.containsAll(resTest) && resTest.containsAll(resQuery));
	}
	
	@After
	public void close() {
		test.getConnection().close();
	}
	
}
