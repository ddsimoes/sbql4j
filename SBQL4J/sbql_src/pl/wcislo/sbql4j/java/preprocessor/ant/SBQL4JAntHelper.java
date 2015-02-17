package pl.wcislo.sbql4j.java.preprocessor.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import net.sf.jse.JSE;

public class SBQL4JAntHelper {

    public static void expandFile(boolean lineup, boolean recursive, boolean verbose,
            String jseFilename, String javaFilename) {

        InputStream input = null;
        OutputStream output = null;
        try {
            File jseFile = new File(jseFilename);
            File javaFile = new File(javaFilename);
            File javaDirectory = javaFile.getParentFile();
            if (javaDirectory != null && !javaDirectory.exists()) {
                javaDirectory.mkdirs();
            }
            input = new FileInputStream(jseFile);
            output = new FileOutputStream(javaFile);
//            JSE.expandStreams(input, output, verbose, !lineup, recursive);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // ignore
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

    }

    public static void main(String[] args) {
        // Need to do more error checking.
        // Maybe follow arg line conventions more, too!
        boolean lineup = Boolean.valueOf(args[0]).booleanValue();
        boolean recursive = Boolean.valueOf(args[1]).booleanValue();
        boolean verbose = Boolean.valueOf(args[2]).booleanValue();
        for (int i = 0; i < (args.length - 3) / 2; i++) {
            expandFile(lineup, recursive, verbose, args[3 + 2 * i], args[3 + 2 * i + 1]);
        }
    }

}