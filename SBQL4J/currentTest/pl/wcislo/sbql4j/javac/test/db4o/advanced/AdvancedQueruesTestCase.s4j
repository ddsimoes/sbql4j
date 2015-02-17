package pl.wcislo.sbql4j.javac.test.db4o.advanced;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.javac.test.advanced.model.Department;
import pl.wcislo.sbql4j.javac.test.advanced.model.Employee;
import pl.wcislo.sbql4j.source.tree.AssertTree;

import com.db4o.ObjectSet;

public class AdvancedQueruesTestCase {
	private AdvancedQueriesTest test = new AdvancedQueriesTest();
	
	@Test
	public void testQuery1() {
		Collection<Struct> resQuery = test.query1();
		ObjectSet<Department> res = test.getConnection().query(Department.class);
		Iterator<Department> it2 = res.iterator();
		for(Iterator<Struct> it=resQuery.iterator(); it.hasNext(); ) {
			Struct s = it.next();
			Department dept = (Department)s.getValue(0);
			Double avgSal = (Double)s.getValue(1);			
			double sumSal = 0.0;
			Department d2 = it2.next();
			for(Employee e : d2.getEmploys()) {
				sumSal += e.getSalary();
			}
			double avgSal2 = sumSal / d2.getEmploys().size();
			Assert.assertTrue(dept.getName().equals(d2.getName()));
			Assert.assertTrue(avgSal == avgSal2);
		}
	}
	
	@Test
	public void testQuery2() {
		Collection<Struct> resQuery = test.query2();
		ObjectSet<Employee> res = test.getConnection().query(Employee.class);
		Iterator<Struct> it1 = resQuery.iterator();
		Iterator<Employee> it2 = res.iterator();
		for(; it2.hasNext(); ) {
			Employee e = it2.next();
			if(e.getSalary() < 2222) {
				Assert.assertTrue(it1.hasNext());
				Struct s = it1.next();
				String empName = (String)s.getValue(0);
				Double empSal = (Double)s.getValue(1);
				String workName = (String)s.getValue(2);
				Assert.assertTrue(empName.equals(e.getName()));
				Assert.assertTrue(empSal.equals(e.getSalary()));
				Assert.assertTrue(workName.equals(e.getWorksIn().getName()));
			}
		}
	}
	
	@Test
	public void testQuery3() {
		Collection<String> resQuery = test.query3();
		ObjectSet<Employee> res = test.getConnection().query(Employee.class);
		Iterator<String> it = resQuery.iterator();
		for(Employee e : res) {
			if(e.getWorksIn().getBoss().getName().equals("Bert")) {
				Assert.assertTrue(it.hasNext());
				Assert.assertTrue(e.getName().equals(it.next()));
			}
		}
	}
	
	@Test
	public void testQuery4() {
		String resQuery = test.query4();
		ObjectSet<Employee> res = test.getConnection().query(Employee.class);
		for(Employee e : res) {
			if(e.getName().equals("Poe")) {
				Assert.assertTrue(e.getWorksIn().getBoss().getName().equals(resQuery));
				return;
			}
		}
		Assert.assertTrue(false);
	}
	
	@Test
	public void testQuery5() {
		Collection<Struct> resQuery = test.query5();
		Iterator<Struct> qIt = resQuery.iterator();
		ObjectSet<Department> res = test.getConnection().query(Department.class);
		for(Department d : res) {
			if(d.getBoss().getName().equals("Bert")) {
				for(Employee emp : d.getEmploys()) {
					Assert.assertTrue(qIt.hasNext());
					Struct s = qIt.next();
					String qName = (String)s.getValue(0);
					String qCity = (String)s.getValue(1);
					Assert.assertTrue(qName.equals(emp.getName()));
					if(emp.getAddress() != null) {
						Assert.assertTrue(qCity.equals(emp.getAddress().getCity()));
					} else {
						Assert.assertTrue(qCity.equals("No address"));
					}
				}
			}
		}
	}
	
	@Test
	public void testQuery6() {
		Struct resQuery = test.query6();
		ObjectSet<Department> res = test.getConnection().query(Department.class);
		Double sumCount = 0d;
		Integer maxCount = 0;
		Integer minCount = Integer.MAX_VALUE;
		for(Department d : res) {
			int count = d.getEmploys().size();
			sumCount += count;
			if(count > maxCount) {
				maxCount = count;
			}
			if(count < minCount) {
				minCount = count;
			}
		}
		Double avgCount = sumCount / res.size();
		Assert.assertTrue(minCount.equals(resQuery.getValue(0)));
		Assert.assertTrue(avgCount.equals(resQuery.getValue(1)));
		Assert.assertTrue(maxCount.equals(resQuery.getValue(2)));
	}
	
	@Test
	public void testQuery7() {
		Collection<Struct> resQuery = test.query7();
		Iterator<Struct> resIt = resQuery.iterator();
		ObjectSet<Department> res = test.getConnection().query(Department.class);
		for(Department d : res) {
			String deptName = d.getName();
			Double salSum = 0d;
			for(Employee e : d.getEmploys()) {
				salSum += e.getSalary();
			}
			salSum -= d.getBoss().getSalary();
			Struct s = resIt.next();
			Assert.assertTrue(s.getValue(0).equals(deptName));
			Assert.assertTrue(s.getValue(1).equals(salSum));
		}
	}
	
	@Test
	public void testQuery8() {
		Boolean resQuery = test.query8();
		ObjectSet<Department> res = test.getConnection().query(Department.class);
		Boolean allRes = true;
		for(Department d : res) {
			Boolean anyRes = false;
			List<Employee> empList = new ArrayList<Employee>();
			empList.addAll(d.getEmploys());
			empList.remove(d.getBoss());
			for(Employee e : empList) {
				if(e.getSalary().equals(d.getBoss().getSalary())) {
					anyRes = true;
					break;
				}
			}
			if(anyRes == false) {
				allRes = false;
				break;
			}
		}
		Assert.assertTrue(allRes.equals(resQuery));
	}
	
	@Test
	public void testQuery9() {
		Collection<String> resQuery = test.query9();
		Iterator<String> itQ = resQuery.iterator();
		ObjectSet<Employee> res = test.getConnection().query(Employee.class);
		for(Employee e : res) {
			String m = "Employee " + e.getName() + " consumes " + (e.getSalary() * 12 * 100 / (e.getWorksIn().getBudget())) +
				"% of the " + e.getWorksIn().getName() + " department budget.";
			Assert.assertTrue(itQ.hasNext());
			Assert.assertTrue(m.equals(itQ.next()));
		}
	}
	
	@Test
	public void testQuery10() {
		Collection<String> resQuery = test.query10();
		Iterator<String> itQ = resQuery.iterator();
		ObjectSet<Department> res = test.getConnection().query(Department.class);
		Set<String> locSet = new HashSet<String>();
		for(Department d : res) {
			locSet.addAll(d.getLocation());
		}
		Collection<String> resLoc = new ArrayList<String>();
		for(String loc : locSet) {
			boolean all = true;
			for(Department d : res) {
				if(!d.getLocation().contains(loc)) {
					all = false;
					break;
				}
			}
			if(all) {
				resLoc.add(loc);
			}
		}
		Assert.assertTrue(resQuery.containsAll(resLoc));
		Assert.assertTrue(resLoc.containsAll(resQuery));
		
	}
	
	@After
	public void close() {
		test.getConnection().close();
	}
	
}
