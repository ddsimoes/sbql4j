package pl.wcislo.sbql4j.java.test;

import java.io.File;

public class FileCloseByTest {

	public String pupa;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new FileCloseByTest();
	}
	
	public FileCloseByTest() {
	//	File f = new File("D:/delete/");
	//	Object res = # (((:f closeby listFiles()) where getName().toLowerCase().endsWith(".pdf")).getAbsolutePath() as plik) #;
		
		File f1 = new File("D:/delete/");
//		Object res = //GENERATED FROM: # (((:f1 closeby listFiles()) where getName().toLowerCase().endsWith(".pdf")).delete()) #
//new pl.wcislo.sbql4j.java.utils.JavaStatement(" (((:f1 closeby listFiles()) where getName().toLowerCase().endsWith(\".pdf\")).delete()) ", f1).execute();
		
//		try {
////			System.out.println(pl.wcislo.sbql4j.util.Utils.resultToString((ENVSType) res, false));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
