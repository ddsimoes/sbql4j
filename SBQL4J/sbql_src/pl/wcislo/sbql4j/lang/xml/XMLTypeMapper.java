package pl.wcislo.sbql4j.lang.xml;

import java.util.Arrays;
import java.util.List;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.xs.TypeValidator;

import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;

public class XMLTypeMapper {
	private List<XMLTypeMapperEntry> entryList;
	
	public XMLTypeMapper(List<XMLTypeMapperEntry> entryList) {
		super();
		this.entryList = entryList;
	}
	
	public Object parseValue(String[] path, String value) {
		short type = -1;
		XMLTypeMapperEntry entry = getEntry(path);
		if(entry != null) {
			type = entry.type;
		}
//		for(XMLTypeMapperEntry e : entryList) {
//			if(Arrays.equals(path, e.path)) {
//				type = e.type;
//				break;
//			}
//		}
		if(type == -1) {
			return value;
		}
		TypeValidator validator = XMLTypes.getTypeValidator(type);
		try {
			return validator.getActualValue(value, null);
		} catch (InvalidDatatypeValueException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	public XMLTypeMapperEntry getEntry(String[] path) {
		for(XMLTypeMapperEntry e : entryList) {
			if(Arrays.equals(path, e.path)) {
				return e;
			}
		}
		return null;
	}
	
	public List<XMLTypeMapperEntry> getEntryList() {
		return entryList;
	}

	@Override
	public String toString() {
		return "XMLTypeMapper [entryList=" + entryList + "]";
	}

	public static class XMLTypeMapperEntry {
		public XMLTypeMapperEntry(String[] path, int type) {
			super();
			this.path = path;
			this.type = (short)type;
		}
		
		public XMLTypeMapperEntry(String[] path, int type, SCollectionType colType) {
			this(path, type);
			this.colType = colType;
		}
		
		public XMLTypeMapperEntry(String[] path, int type, SCollectionType colType, boolean simpleTypeWithAttributes) {
			this(path, type, colType);
			this.simpleTypeWithAttributes = simpleTypeWithAttributes;
		}
		
		private String[] path;
		private short type;
		private SCollectionType colType = SCollectionType.NO_COLLECTION;
		private boolean simpleTypeWithAttributes;
		
		public String[] getPath() {
			return path;
		}
		public void setPath(String[] path) {
			this.path = path;
		}
		public short getType() {
			return type;
		}
		public void setType(short type) {
			this.type = type;
		}
		public SCollectionType getColType() {
			return colType;
		}
		public void setColType(SCollectionType colType) {
			this.colType = colType;
		}
		
		public boolean isSimpleTypeWithAttributes() {
			return simpleTypeWithAttributes;
		}
		public void setSimpleTypeWithAttributes(boolean simpleTypeWithAttributes) {
			this.simpleTypeWithAttributes = simpleTypeWithAttributes;
		}

		@Override
		public String toString() {
			return "XMLTypeMapperEntry [path=" + Arrays.toString(path)
					+ ", type=" + type + ", colType=" + colType
					+ ", simpleTypeWithAttributes=" + simpleTypeWithAttributes
					+ "]";
		}
	}
}
