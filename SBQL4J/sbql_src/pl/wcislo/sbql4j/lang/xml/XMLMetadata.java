package pl.wcislo.sbql4j.lang.xml;

public @interface XMLMetadata {
	public enum SourceType {
		NO_VALIDATION,
		XML_SCHEMA_FILE,
		DTD_FILE
	}
	
	SourceType type();
	String value() default "";
	boolean validateXML() default false;
}
