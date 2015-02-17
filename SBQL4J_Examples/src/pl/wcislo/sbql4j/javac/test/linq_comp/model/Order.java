package pl.wcislo.sbql4j.javac.test.linq_comp.model;

import java.util.Date;

public class Order {
    public int orderID;
    public Date orderDate;
    public double total;
    
    public Order(int orderID, Date orderDate, double total) {
		this.orderID = orderID;
		this.orderDate = orderDate;
		this.total = total;
	}
    
    @Override
    public String toString() {
    	return "Order[orderID="+orderID+", orderDate="+orderDate+", total="+total+"]";
    }
}
