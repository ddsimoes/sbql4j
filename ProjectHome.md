![http://a.imageshack.us/img339/3217/tytul.gif](http://a.imageshack.us/img339/3217/tytul.gif)

## This project is a result of research supported by [Polish-Japanese Institute of Information Technology](http://www.pjwstk.edu.pl) ##

An extension to Java language with an engine based on [Stack-Based Architecture (SBA)](http://sbql.pl/overview/)

It provides capabilities similar to Microsoft [LINQ](http://msdn.microsoft.com/en-us/library/bb308959.aspx) for Java language. Allows to process Java objects with queries.

SBQL4J follow software engineering principles such as orthogonality, modularity, minimality, universality, typing safety, and clean, precise semantics.

Code with queries are parsed by preprocessor integrated with [OpenJDK Compiler](http://openjdk.java.net/). SBQL4J builds AST query trees which are then analyzed and as output Java code is produced.

**It's 100% compatible with current Java Virtual Machines, and can be safely used in any Java project (compatible with Java 6).**

It integrates tightly with Java which means:
  * Java variables can be used in queries directly (no special syntax like _setParameterX_ is required).
  * Queries returns Java objects (collection or single object depending on query and used Java types).
  * Java methods and constructors can be invoked inside queries.
  * Query language is type-safe - queries are checked in compile time.
  * **Queries can be translated to pure Java code (with no reflection usage) so execution is very fast.** ([Description](http://code.google.com/p/sbql4j/wiki/WikiCodeGeneration))

SBQL4J gives Java developers full power of [SBQL query language](http://sbql.pl/overview/) . Multiply nested and complicated queries are well-supported, which can't be written in any other query language (including LINQ).

Available operators:
  * arithmetic and algebraic: _+, -. `*`, /, %, == , != , >, <, >=, <=, OR, AND, NOT, instanceof_
  * aggregating: _sum, count, avg, min, max_
  * set operators: _union, intersect, unique, minus, in, ','_ (comma - structure constructor)
  * quantifiers: _all, any_
  * non-algebraic operators _'.'_ (dot - navigation, projection, path expressions), _where, join, order by, close by_ (transitive closure - something like CONNECT BY in Oracle)
  * range operators: `[<range>]` (for example _collection_`[5]`, _collection_`[3..7]`, _collection_`[2..*]`)
  * auxiliary name operators: _as, group as_
  * constructor: _new_


Some examples:

_Find all products that are in stock and cost more than 3.00 per unit (example from LINQ page)_ ([Model description](http://code.google.com/p/sbql4j/wiki/LINQExamplesModel))
```
List<Product> products = getProductList();
List<Product> expensiveInStockProducts = #{
    products 
    where unitsInStock > 0 and unitPrice > 3.00
};
```

_Selects all orders, while referring to customers by the order in which they are returned from the query (example from LINQ page)._ ([Model description](http://code.google.com/p/sbql4j/wiki/LINQExamplesModel))
```
List<Customer> customers = getCustomerList();
List<String> customerOrders = #{
	(customers as c).
	($index as custIndex, c.orders as o).
	("Customer #"+(custIndex + 1)+" has an order with OrderID "+o.orderID)
};
```

_This sample uses an order by operator with a custom comparer to sort first by word length and then by a case-insensitive sort of the words in an array (example from LINQ page)._
```
String[] words = { "aPPLE", "AbAcUs", "bRaNcH", "BlUeBeRrY", "ClOvEr", "cHeRry" };
Comparator<String> comp = new Comparator<String>() {                              
	@Override                                                                     
	public int compare(String o1, String o2) {                                    
		return o1.toLowerCase().compareTo(o2.toLowerCase());                      
	}                                                                             
};                                                                                
List<String> sortedWords = #{                                                     
	words as w                                                                    
	order by w.length(); w using comp                                             
};                                                                                
```

_Is it true that each department employs an employee earning the same as his/her boss?_ ([Model description](http://code.google.com/p/sbql4j/wiki/AdvancedExamplesModel))
```
List<Department> dept = data.getDepts();
Boolean res = #{
	all (dept as d)
	any ((d.employs minus d.boss) as e)
	(e.salary == d.boss.salary)
};
```

_Get the minimal, average and maximal number of employees in departments._ ([Model description](http://code.google.com/p/sbql4j/wiki/AdvancedExamplesModel))
```
List<Department> dept = data.getDepts();
Struct res = #{
	min(dept.count(employs)) as minimum, 
	avg(dept.count(employs)) as average, 
	max(dept.count(employs)) as maximum
};
```

_For each employee get the message containing his/her name and the percent of the annual budget of his/her department that is consumed by his/her monthly salary_ ([Model description](http://code.google.com/p/sbql4j/wiki/AdvancedExamplesModel))
```
List<Employee> emp = data.getEmps();
List<String> res = #{
	emp.("Employee " + name + " consumes " + (salary * 12 * 100 / (worksIn.budget)) +
	     "% of the " + worksIn.name + " department budget.")
};
```


See more executable examples (about 90 queries) in download section.
There is a run-ready Eclipse project with build SBQL4J library and examples.