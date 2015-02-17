package pl.wcislo.sbql4j.lang.db4o.indexes;

public class IndexMetadataEntry {
	private String indexedClass;
	private String fieldName;
	
	public IndexMetadataEntry(String indexedClass, String fieldName) {
		super();
		this.indexedClass = indexedClass;
		this.fieldName = fieldName;
	}
	
	public String getIndexedClass() {
		return indexedClass;
	}
	public void setIndexedClass(String indexedClass) {
		this.indexedClass = indexedClass;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String toString() {
		return "IndexMetadataEntry [indexedClass=" + indexedClass
				+ ", fieldName=" + fieldName + "]";
	}
	
	
}
