package pl.wcislo.sbql4j.lang.xml.signature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.ResultSource;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.XMLSimpleTypeWithAttributesSignature;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureCompilerFactory;
import pl.wcislo.sbql4j.javac.test.RunCurrentTest;
import pl.wcislo.sbql4j.lang.xml.XMLMetadata.SourceType;
import pl.wcislo.sbql4j.lang.xml.XMLTypeMapper;
import pl.wcislo.sbql4j.lang.xml.XMLTypeMapper.XMLTypeMapperEntry;
import pl.wcislo.sbql4j.lang.xml.XMLTypes;

import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.parser.XSOMParser;

public class XMLSignatureProducer {
	private SourceType metadataSourceType;
	private Boolean validateXML;
	private XSSchemaSet schemaSet;
	private JavaSignatureCompilerFactory fac = JavaSignatureCompilerFactory.getInstance(); 
	private List<String> currentPath = new ArrayList<String>();
	private List<XMLTypeMapperEntry> mappingEntries = new ArrayList<XMLTypeMapper.XMLTypeMapperEntry>();
	
	
	public static void main(String[] args) {
		RunCurrentTest.main(null);
		File f = new File("books.xsd");
		try {
			XMLSignatureProducer pr = new XMLSignatureProducer(SourceType.XML_SCHEMA_FILE, f, false);
			Signature sig = pr.parseSchemas();
			System.out.println(sig);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public XMLSignatureProducer(SourceType metadataSourceType, File metadataFile, boolean validateXML) throws SAXException, IOException {
		this.metadataSourceType = metadataSourceType;
		this.validateXML = validateXML;
		XSOMParser parser = new XSOMParser();
		parser.parse(metadataFile);
		this.schemaSet = parser.getResult();
	}
	
	public Signature parseSchemas() {
		XMLDataSourceSignature res = new XMLDataSourceSignature();
		res.setResultSource(ResultSource.XML);
		Iterator jtr = schemaSet.iterateElementDecls();
		while (jtr.hasNext()) {
			XSElementDecl e = (XSElementDecl) jtr.next();
			res.addField(resolveElement(e, false));
		}
		if(!this.mappingEntries.isEmpty()) {
			res.setTypeMapper(new XMLTypeMapper(this.mappingEntries));	
		}
		return res;
//		if(res.getFields().length == 1) {
//			return res.getFields()[0];
//		} else {
//			return res;
//		}
	}
	
	private BinderSignature resolveElement(XSElementDecl elDelc, boolean repeated) {
		currentPath.add(elDelc.getName());
		Signature resVal;
		XSType type = elDelc.getType();
//		SCollectionType scolType;
		if(type.isComplexType()) {
			Collection<? extends XSAttributeUse> attrList = type.asComplexType().getAttributeUses();
			XSContentType contentType = type.asComplexType().getContentType();
			if(contentType.asParticle() != null) {
				XSParticle particle = contentType.asParticle();
				StructSignature resStruct = new StructSignature();
				resStruct.setResultSource(ResultSource.XML);
				XSParticle[] children = particle.getTerm().asModelGroup().getChildren();
				for(XSParticle child : children) {
					Signature childSig = resolveParticle(child);
					resStruct.addField(childSig);
				}
				for(XSAttributeUse attr : attrList) {
					BinderSignature attrBinderSig = createAttributeSignature(attr);
//					String attrName = attr.getDecl().getName();
//					currentPath.add(attrName);
//					XSSimpleType attrType = attr.getDecl().getType();
//					Signature attrValSig = createSignature(attrType, currentPath.toArray(new String[] {}), false);
//					BinderSignature attrBinderSig = new BinderSignature(attrName, attrValSig, false, ResultSource.XML);
					resStruct.addField(attrBinderSig);
//					currentPath.remove(currentPath.size()-1);
				}
				resVal = resStruct;
			} else if(contentType.asSimpleType() != null) {
				XSSimpleType elSimpleType = contentType.asSimpleType();
//				if(sType.asRestriction() != null) {
//					XSRestrictionSimpleType restr =  sType.asRestriction();
//				}
//				resVal = fac.createJavaSignature(ClassTypes.getInstance().getCompilerType(String.class));
				resVal = createSignature(elSimpleType, currentPath.toArray(new String[] {}), repeated, attrList);
			} else {
				throw new RuntimeException("other type not supported");
			}
		} else if(type.isSimpleType()) {
			XSSimpleType elSimpleType = type.asSimpleType();
//			resVal = fac.createJavaSignature(ClassTypes.getInstance().getCompilerType(String.class));
			resVal = createSignature(elSimpleType, currentPath.toArray(new String[] {}), repeated, null);
		} else {
			throw new RuntimeException("not supported");
		}
		String elName = elDelc.getName();
		resVal.setResultSource(ResultSource.XML);
		BinderSignature res = new BinderSignature(elName, resVal, false, ResultSource.XML);
		//ustawiamy licznosc binderow na liczbe elementow
		res.setColType(resVal.getColType());
		resVal.setColType(SCollectionType.NO_COLLECTION);
		currentPath.remove(currentPath.size()-1);
		return res;
	}
	
	private BinderSignature createAttributeSignature(XSAttributeUse attr) {
		String attrName = attr.getDecl().getName();
		currentPath.add(attrName);
		XSSimpleType attrType = attr.getDecl().getType();
		Signature attrValSig = createSignature(attrType, currentPath.toArray(new String[] {}), false, null);
		BinderSignature attrBinderSig = new BinderSignature(attrName, attrValSig, false, ResultSource.XML);
		currentPath.remove(currentPath.size()-1);
		return attrBinderSig;
	}
	
	private Signature createSignature(XSSimpleType type, String[] xmlPath, boolean repeated, Collection<? extends XSAttributeUse> attrList) {
		short typeCode = -1;
		if(type != null && !type.isLocal()) {
			String typeName = type.getName();
			typeCode = XMLTypes.getTypeCodeForTypeName(typeName);
		}
		Class typeClass = String.class;
		XMLTypeMapperEntry tmEntry = null;
		SCollectionType colType;
		if(repeated) {
			colType = SCollectionType.SEQUENCE;	
		} else {
			colType = SCollectionType.NO_COLLECTION;
		}
		boolean simpleTypeWithAttributes = attrList != null && !attrList.isEmpty();
		if(typeCode > -1) {
			tmEntry = new XMLTypeMapperEntry(xmlPath, typeCode, colType, simpleTypeWithAttributes);
			this.mappingEntries.add(tmEntry);
			typeClass = XMLTypes.getClassType(typeCode);
		}
		Signature valSig = fac.createJavaSignature(ClassTypes.getInstance().getCompilerType(typeClass));
		if(!simpleTypeWithAttributes) {
			return valSig;
		} else {
			List<BinderSignature> attributes = new ArrayList<BinderSignature>();
			for(XSAttributeUse attr : attrList) {
				BinderSignature attrSig = createAttributeSignature(attr);
				attributes.add(attrSig);
			}
			XMLSimpleTypeWithAttributesSignature resSig = new XMLSimpleTypeWithAttributesSignature((ValueSignature) valSig, attributes);
			resSig.setResultSource(ResultSource.XML);
			return resSig;
		}
//		BinderSignature binderSig = new BinderSignature(name, valSig, false, ResultSource.XML);
//		return valSig;
	}
	
	private Signature resolveParticle(XSParticle particle) {
		XSElementDecl element = particle.getTerm().asElementDecl();
		boolean repeated = particle.isRepeated();
		Signature res = resolveElement(element, repeated);
		if(particle.isRepeated()) {
			res.setColType(SCollectionType.SEQUENCE);	
		} else {
			res.setColType(SCollectionType.NO_COLLECTION);
		}
		return res;
	}
	
	public Boolean getValidateXML() {
		return validateXML;
	}
	public SourceType getMetadataSourceType() {
		return metadataSourceType;
	}
}
