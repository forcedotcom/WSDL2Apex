/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import java.io.*;
import java.util.*;

public class FileString {

    /**
     * Converts the file into a string
     * 
     * @param Path
     * @return
     * @throws FileNotFoundException
     */
    public static String getStringFromFile(String Path) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(Path);
        Scanner s = new Scanner(fis);
        s.useDelimiter("\\Z");
        String file = new String();
        while (s.hasNext()) {
            file += s.next();
        }
        s.close();
        return file;
    }

    /**
     * Spits the contents of the string into a file
     * 
     * @param path
     * @param content
     * @param fileName
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void StringToFile(String path, String content, String fileName) throws FileNotFoundException,
            UnsupportedEncodingException {
        PrintWriter writer;
        String totalName = new String(path + "/" + fileName + ".cls");
        try {
            writer = new PrintWriter(totalName, "UTF-8");
            writer.print(content.trim());
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.err.println("Unable to create the new apex class file");
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * Deletes the files in the directory and then the directory itself
     * 
     * @param file
     */
    public static void recursiveDelete(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
        file.delete();
    }
}
