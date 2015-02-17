package pl.wcislo.sbql4j.model.collections;

import java.util.ArrayList;
import java.util.Collection;

import pl.wcislo.sbql4j.exception.SBQLException;
import pl.wcislo.sbql4j.model.QueryResult;


//implements Collection<QueryResult>
public class Bag<T extends Collection<QueryResult>> extends AbstractCollectionResult  {
	
	public Bag() {
		super(CollectionType.BAG, new ArrayList<QueryResult>());
	}
	
	public Bag(T bagType) {
		super(CollectionType.BAG, bagType);
	}
	
	
	@Override
	public boolean add(QueryResult e) {
		if(e instanceof CollectionResult) {
			return super.addAll((CollectionResult)e);
		} else {
			return super.add(e);
		}
	}
	
	@Override
	public Bag createSameType() {
		try {
			Bag b = new Bag(this.innerCol.getClass().newInstance());
			return b;
		} catch (Exception e) {
			throw new SBQLException();
		}
	}
}

