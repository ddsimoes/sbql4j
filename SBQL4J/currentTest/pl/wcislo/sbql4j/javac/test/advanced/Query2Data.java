package pl.wcislo.sbql4j.javac.test.advanced;

public class Query2Data {
    public String ename;
    public Double salary;
    public String dname;
    
    public Query2Data(String ename, Double salary, String dname) {
        super();
        this.ename = ename;
        this.salary = salary;
        this.dname = dname;
    }
    
    @Override
    public String toString() {
    	return "Query2Data[emp_name="+ename+", salary="+salary+", dept_name="+dname+"]";
    }
}
