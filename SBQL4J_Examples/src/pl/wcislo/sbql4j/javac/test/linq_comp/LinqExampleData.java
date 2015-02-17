package pl.wcislo.sbql4j.javac.test.linq_comp;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import pl.wcislo.sbql4j.javac.test.linq_comp.model.Customer;
import pl.wcislo.sbql4j.javac.test.linq_comp.model.Order;
import pl.wcislo.sbql4j.javac.test.linq_comp.model.Product;

public class LinqExampleData {

	public static void main(String[] args) {
		LinqExampleData data = new LinqExampleData();
		data.getCustomerList();
		data.getProductList();
		System.out.println();
	}
	
	private List<Product> productList;
	public List<Product> getProductList() {
		if(productList == null) {
			productList = new ArrayList<Product>(); 
	        productList.add(new Product (1, "Chai", "Beverages", 18.0000,  39 ));
	        productList.add(new Product (2, "Chang", "Beverages", 19.0000, 17 ));
	        productList.add(new Product (3, "Aniseed Syrup", "Condiments", 10.0000, 13 ));
	        productList.add(new Product (4, "Chef Anton's Cajun Seasoning", "Condiments", 22.0000, 53 ));
	        productList.add(new Product (5, "Chef Anton's Gumbo Mix", "Condiments", 21.3500, 0 ));
	        productList.add(new Product (6, "Grandma's Boysenberry Spread", "Condiments", 25.0000, 120 ));
	        productList.add(new Product (7, "Uncle Bob's Organic Dried Pears", "Produce", 30.0000, 15 ));
	        productList.add(new Product (8, "Northwoods Cranberry Sauce", "Condiments", 40.0000, 6 ));
	        productList.add(new Product (9, "Mishi Kobe Niku", "Meat/Poultry", 97.0000, 29 ));
	        productList.add(new Product (10, "Ikura", "Seafood", 31.0000, 31 ));
	        productList.add(new Product (11, "Queso Cabrales", "Dairy Products", 21.0000, 22 ));
	        productList.add(new Product (12, "Queso Manchego La Pastora", "Dairy Products", 38.0000, 86 ));
	        productList.add(new Product (13, "Konbu", "Seafood", 6.0000, 24 ));
	        productList.add(new Product (14, "Tofu", "Produce", 23.2500, 35 ));
	        productList.add(new Product (15, "Genen Shouyu", "Condiments", 15.5000, 39 ));
	        productList.add(new Product (16, "Pavlova", "Confections", 17.4500, 29 ));
	        productList.add(new Product (17, "Alice Mutton", "Meat/Poultry", 39.0000, 0 ));
	        productList.add(new Product (18, "Carnarvon Tigers", "Seafood", 62.5000, 42 ));
	        productList.add(new Product (19, "Teatime Chocolate Biscuits", "Confections", 9.2000, 25 ));
	        productList.add(new Product (20, "Sir Rodney's Marmalade", "Confections", 81.0000, 40 ));
	        productList.add(new Product (21, "Sir Rodney's Scones", "Confections", 10.0000, 3 ));
	        productList.add(new Product (22, "Gustaf's Knäckebröd", "Grains/Cereals", 21.0000, 104 ));
	        productList.add(new Product (23, "Tunnbröd", "Grains/Cereals", 9.0000, 61 ));
	        productList.add(new Product (24, "Guaraná Fantástica", "Beverages", 4.5000, 20 ));
	        productList.add(new Product (25, "NuNuCa Nuß-Nougat-Creme", "Confections", 14.0000, 76 ));
	        productList.add(new Product (26, "Gumbär Gummibärchen", "Confections", 31.2300, 15 ));
	        productList.add(new Product (27, "Schoggi Schokolade", "Confections", 43.9000, 49 ));
	        productList.add(new Product (28, "Rössle Sauerkraut", "Produce", 45.6000, 26 ));
	        productList.add(new Product (29, "Thüringer Rostbratwurst", "Meat/Poultry", 123.7900, 0 ));
	        productList.add(new Product (30, "Nord-Ost Matjeshering", "Seafood", 25.8900, 10 ));
	        productList.add(new Product (31, "Gorgonzola Telino", "Dairy Products", 12.5000, 0 ));
	        productList.add(new Product (32, "Mascarpone Fabioli", "Dairy Products", 32.0000, 9 ));
	        productList.add(new Product (33, "Geitost", "Dairy Products", 2.5000, 112 ));
	        productList.add(new Product (34, "Sasquatch Ale", "Beverages", 14.0000, 111 ));
	        productList.add(new Product (35, "Steeleye Stout", "Beverages", 18.0000, 20 ));
	        productList.add(new Product (36, "Inlagd Sill", "Seafood", 19.0000, 112 ));
	        productList.add(new Product (37, "Gravad lax", "Seafood", 26.0000, 11 ));
	        productList.add(new Product (38, "Côte de Blaye", "Beverages", 263.5000, 17 ));
	        productList.add(new Product (39, "Chartreuse verte", "Beverages", 18.0000, 69 ));
	        productList.add(new Product (40, "Boston Crab Meat", "Seafood", 18.4000, 123 ));
	        productList.add(new Product (41, "Jack's New England Clam Chowder", "Seafood", 9.6500, 85 ));
	        productList.add(new Product (42, "Singaporean Hokkien Fried Mee", "Grains/Cereals", 14.0000, 26 ));
	        productList.add(new Product (43, "Ipoh Coffee", "Beverages", 46.0000, 17 ));
	        productList.add(new Product (44, "Gula Malacca", "Condiments", 19.4500, 27 ));
	        productList.add(new Product (45, "Rogede sild", "Seafood", 9.5000, 5 ));
	        productList.add(new Product (46, "Spegesild", "Seafood", 12.0000, 95 ));
	        productList.add(new Product (47, "Zaanse koeken", "Confections", 9.5000, 36 ));
	        productList.add(new Product (48, "Chocolade", "Confections", 12.7500, 15 ));
	        productList.add(new Product (49, "Maxilaku", "Confections", 20.0000, 10 ));
	        productList.add(new Product (50, "Valkoinen suklaa", "Confections", 16.2500, 65 ));
	        productList.add(new Product (51, "Manjimup Dried Apples", "Produce", 53.0000, 20 ));
	        productList.add(new Product (52, "Filo Mix", "Grains/Cereals", 7.0000, 38 ));
	        productList.add(new Product (53, "Perth Pasties", "Meat/Poultry", 32.8000, 0 ));
	        productList.add(new Product (54, "Tourtière", "Meat/Poultry", 7.4500, 21 ));
	        productList.add(new Product (55, "Pâté chinois", "Meat/Poultry", 24.0000, 115 ));
	        productList.add(new Product (56, "Gnocchi di nonna Alice", "Grains/Cereals", 38.0000, 21 ));
	        productList.add(new Product (57, "Ravioli Angelo", "Grains/Cereals", 19.5000, 36 ));
	        productList.add(new Product (58, "Escargots de Bourgogne", "Seafood", 13.2500, 62 ));
	        productList.add(new Product (59, "Raclette Courdavault", "Dairy Products", 55.0000, 79 ));
	        productList.add(new Product (60, "Camembert Pierrot", "Dairy Products", 34.0000, 19 ));
	        productList.add(new Product (61, "Sirop d'érable", "Condiments", 28.5000, 113 ));
	        productList.add(new Product (62, "Tarte au sucre", "Confections", 49.3000, 17 ));
	        productList.add(new Product (63, "Vegie-spread", "Condiments", 43.9000, 24 ));
	        productList.add(new Product (64, "Wimmers gute Semmelknödel", "Grains/Cereals", 33.2500, 22 ));
	        productList.add(new Product (65, "Louisiana Fiery Hot Pepper Sauce", "Condiments", 21.0500, 76 ));
	        productList.add(new Product (66, "Louisiana Hot Spiced Okra", "Condiments", 17.0000, 4 ));
	        productList.add(new Product (67, "Laughing Lumberjack Lager", "Beverages", 14.0000, 52 ));
	        productList.add(new Product (68, "Scottish Longbreads", "Confections", 12.5000, 6 ));
	        productList.add(new Product (69, "Gudbrandsdalsost", "Dairy Products", 36.0000, 26 ));
	        productList.add(new Product (70, "Outback Lager", "Beverages", 15.0000, 15 ));
	        productList.add(new Product (71, "Flotemysost", "Dairy Products", 21.5000, 26 ));
	        productList.add(new Product (72, "Mozzarella di Giovanni", "Dairy Products", 34.8000, 14 ));
	        productList.add(new Product (73, "Röd Kaviar", "Seafood", 15.0000, 101 ));
	        productList.add(new Product (74, "Longlife Tofu", "Produce", 10.0000, 4 ));
	        productList.add(new Product (75, "Rhönbräu Klosterbier", "Beverages", 7.7500, 125 ));
	        productList.add(new Product (76, "Lakkalikööri", "Beverages", 18.0000, 57 ));
	        productList.add(new Product (77, "Original Frankfurter grüne Soße", "Condiments", 13.0000, 32 ));
		}
		return productList;
	}
	
	private List<Customer> customerList;
	public List<Customer> getCustomerList() {
		if (customerList == null) {
			customerList = new ArrayList<Customer>();
			SAXBuilder builder = new SAXBuilder();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Document doc = builder.build(new File("sbql_test/Customers.xml"));
				Element customers = doc.getRootElement();
				List<Element> cElList = customers.getChildren();
				for(Element e : cElList) {
				    String customerID = e.getChildText("id");
				    String companyName = e.getChildText("name");
				    String address = e.getChildText("address");
				    String city = e.getChildText("city");
				    String region = e.getChildText("region");
				    String postalCode = e.getChildText("postalcode");
				    String country = e.getChildText("country");
				    String phone = e.getChildText("phone");
				    String fax = e.getChildText("fax");
				    List<Order> custOrders = new ArrayList<Order>();
				    Element ordersEl = e.getChild("orders");
				    if(ordersEl != null) {
				    	List<Element> orderElList = ordersEl.getChildren();
				    	for(Element orderEl : orderElList) {
				    		int orderID = Integer.parseInt(orderEl.getChildText("id"));
				    		Date orderDate = null;
							try {
								orderDate = df.parse(orderEl.getChildText("orderdate"));
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				    		double total = Double.parseDouble(orderEl.getChildText("total"));
				    		Order order = new Order(orderID, orderDate, total);
				    		custOrders.add(order);
				    	}
				    }
				    
				    Customer cust = new Customer(customerID, companyName, address, city, region, postalCode, country, phone, fax, custOrders);
					customerList.add(cust);
				}
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return customerList;
	}
}
