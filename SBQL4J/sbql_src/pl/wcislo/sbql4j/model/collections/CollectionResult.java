package pl.wcislo.sbql4j.model.collections;

import java.util.Collection;

import pl.wcislo.sbql4j.model.QueryResult;


public interface CollectionResult extends QueryResult, Collection<QueryResult> {
	public CollectionType getCollectionType();
	public Class<? extends Collection> getInnerCollectionType();
	public CollectionResult createSameType();
}

