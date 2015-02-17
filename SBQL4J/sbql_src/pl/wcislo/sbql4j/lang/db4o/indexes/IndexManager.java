package pl.wcislo.sbql4j.lang.db4o.indexes;

import java.util.ArrayList;
import java.util.List;

import com.db4o.ext.ExtObjectContainer;
import com.db4o.ext.StoredClass;
import com.db4o.ext.StoredField;

public class IndexManager {
	private final List<IndexMetadataEntry> metabase;
	
	public IndexManager(ExtObjectContainer db, IndexMetadataStore store) {
		metabase = loadFromDatabase(db);
		if(store != null) {
			store.storeData(metabase);	
		}
	}
	
	public IndexManager(IndexMetadataStore store) {
		metabase = store.loadData();
	}
	
	public List<IndexMetadataEntry> loadFromDatabase(ExtObjectContainer db) {
		List<IndexMetadataEntry> res = new ArrayList<IndexMetadataEntry>();
		StoredClass[] storedClasses = db.storedClasses();
		for(StoredClass sc : storedClasses) {
//			System.out.println(rc.toString());
			for(StoredField sf : sc.getStoredFields()) {
//				System.out.println(rf.toString());
				if(sf.hasIndex()) {
//					System.out.println(rf.toString());
					String clazzName = sc.getName();
					String fieldName = sf.getName();
					IndexMetadataEntry im = new IndexMetadataEntry(clazzName, fieldName);
					res.add(im);
				}
//				rf.indexType()
			}
		}
		return res;
	}
	
	public List<IndexMetadataEntry> getMetabase() {
		return metabase;
	}
	
	public List<IndexMetadataEntry> getIndexesForClass(String className) {
		List<IndexMetadataEntry> res = new ArrayList<IndexMetadataEntry>();
		for(IndexMetadataEntry entry : metabase) {
			if(entry.getIndexedClass().equals(className)) {
				res.add(entry);
			}
		}
		return res;
	}
	
}
