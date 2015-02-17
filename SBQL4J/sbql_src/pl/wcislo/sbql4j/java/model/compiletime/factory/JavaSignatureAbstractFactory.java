package pl.wcislo.sbql4j.java.model.compiletime.factory;

public class JavaSignatureAbstractFactory {
	public static JavaSignatureFactory getJavaSignatureFactory() {
		return JavaSignatureCompilerFactory.getInstance();
	}
}
