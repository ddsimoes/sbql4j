package pl.wcislo.sbql4j.lang.db4o.indexes;

import java.util.List;

public interface IndexMetadataStore {
	public List<IndexMetadataEntry> loadData();
	public void storeData(List<IndexMetadataEntry> data);
	
}
