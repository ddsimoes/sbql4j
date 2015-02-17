package pl.wcislo.sbql4j.javac.test.advanced;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.javac.test.advanced.model.Address;
import pl.wcislo.sbql4j.javac.test.advanced.model.Department;
import pl.wcislo.sbql4j.javac.test.advanced.model.Employee;

import pl.wcislo.sbql4j.java.model.runtime.Struct;

public class AdvQueriesExampleData {

	private List<Department> depts;
	private List<Employee> emps;
	public List<Department> getDepts() {
		if(depts == null) {
			initData();
		}
		return depts;
	}
	
	public List<Employee> getEmps() {
		if(emps == null) {
			initData();
		}
		return emps;
	}
	
	private void initData() {
		depts = new ArrayList<Department>();
		emps = new ArrayList<Employee>();
		Department d = new Department(new ArrayList<Employee>(), null, "Production", 500000.0, new ArrayList<String>());
		depts.add(d);
		d.getLocation().add("Honolulu");
		Employee emp = new Employee("Poe", new Address("Honolulu", "5'th avenue", 4), 1500.0, "boss", d);
		emps.add(emp);
		d.getEmploys().add(emp);
		emp = new Employee("Smith", new Address("Makaha", "Park Avenue", 443), 2200.0, "boss", d);
		emps.add(emp);
		d.getEmploys().add(emp);
		d.setBoss(emp);
		d = new Department(new ArrayList<Employee>(), null, "HR", 200000.0, new ArrayList<String>());
		depts.add(d);
		d.getLocation().add("Honolulu");
		d.getLocation().add("Norwalk");
		emp = new Employee("Bert", new Address("Honolulu", "5'th avenue", 44), 2200.0, "boss", d);
		emps.add(emp);
		d.getEmploys().add(emp);
		d.setBoss(emp);
		emp = new Employee("Daniels", new Address("Norwalk", "4'th avenue", 24), 1200.0, "clerk", d);
		emps.add(emp);
		d.getEmploys().add(emp);
		emp = new Employee("Ripper", new Address("Norwalk", "Park avenue", 41), 3300.0, "clerk", d);
		emps.add(emp);
		d.getEmploys().add(emp);
	}
}
