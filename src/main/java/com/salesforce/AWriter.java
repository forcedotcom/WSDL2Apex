package com.salesforce.ide.wsdl2apex.core;


import java.io.CharArrayWriter;
import java.io.Writer;
import java.io.IOException;

/**
 * AWriter
 *
 * @author cheenath
 * @version 1.0
 * @since 148  Feb 7, 2007
 */
public class AWriter {
    
    private final Writer writer = new CharArrayWriter();
    private final String EOL = System.getProperty("line.separator");
    private int tabs = 0;

    public void writeComment(String s) throws CalloutException {
        writeLine("//", s);
    }

    public void writeLine(String... args) throws CalloutException {
        try {
            for (int i = 0; i < tabs; i++) {
                writer.write("    ");
            }
            if (args != null) {
                for (String arg : args) {
                    writer.write(arg);
                }
            }
            writer.write(EOL);
        } catch (IOException e) {
            throw new CalloutException("Failed to write", e);
        }
    }

    public void startBlock() {
        tabs++;
    }

    public void endBlock() {
        tabs--;
    }

    @Override
    public String toString() {
        return writer.toString();
    }
}
