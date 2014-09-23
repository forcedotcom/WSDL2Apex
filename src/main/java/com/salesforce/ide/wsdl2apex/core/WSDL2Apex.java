/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.wsdl.*;

/**
 * Wsdl2Apex
 * 
 * @author cheenath
 * @version 1.0
 */
public class WSDL2Apex {
    private static final String CLIENT_NAME = "Wsdl2Apex";
    private static final double CLIENT_VERSION = 1.0;
    private ArrayList<String> allClassNames;
    private ArrayList<String> allClasses;
    private AnalysisResult result;
    private String wsdlString;
    private final static Logger LOGGER = Logger.getLogger(WSDL2Apex.class.getName());
    private static FileHandler f;

    /**
     * Gets the logger ready
     */
    static {
        try {
            f = new FileHandler("error.log", false);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        Logger l = Logger.getLogger("");
        f.setFormatter(new SimpleFormatter());
        l.addHandler(f);
        l.setLevel(Level.CONFIG);
    }

    public WSDL2Apex() {

    }

    public AnalysisResult analyze(String wsdl) {
        AnalysisResult result = new AnalysisResult();
        result.success = false;

        try {
            Definitions definitions;
            definitions = WsdlFactory.createFromString(wsdl);
            result.mapping.put(definitions.getTargetNamespace(), packageName(definitions.getTargetNamespace()));

            if (definitions.getTypes() != null) {
                for (Schema schema : definitions.getTypes().getSchemas()) {
                    result.mapping.put(schema.getTargetNamespace(), packageName(schema.getTargetNamespace()));
                }
            }

            result.messages.add("Parse successful: Warnings 0, Errors 0");
            result.success = true;
        } catch (WsdlParseException e) {
            result.messages.add("Failed to parse wsdl: " + e.getMessage());
        } catch (Throwable th) {
            result.messages.add("Failed to parse wsdl");
        }
        return result;
    }

    private static List<String> splitSimple(String str, String delimiter, int expectedSize, boolean shouldTrim,
            boolean ignoreTrailingEmpty) {
        if (str == null) {
            return null;
        }
        List<String> result = (expectedSize == 0) ? new LinkedList<String>() : new ArrayList<String>(expectedSize);

        if (delimiter.length() == 0) {
            if (!ignoreTrailingEmpty)
                throw new IllegalArgumentException();

            //Special case to match java's behavior
            char[] chars = new char[str.length()];
            str.getChars(0, str.length(), chars, 0);
            result.add("");
            for (char c : chars) {
                result.add(Character.toString(c));
            }
            return result;
        }

        //Special case to match java's behavior
        if (ignoreTrailingEmpty && "".equals(str)) {
            result.add("");
            return result;
        }

        int start = 0;
        int indexof;
        while ((indexof = str.indexOf(delimiter, start)) != -1) {
            String substring = str.substring(start, indexof);
            if (shouldTrim)
                substring = substring.trim();
            result.add(substring);
            start = indexof + delimiter.length();
            if (start >= str.length())
                break;
        }
        if (start == str.length()) {
            result.add("");
        } else if (start < str.length()) {
            String substring = str.substring(start);
            if (shouldTrim)
                substring = substring.trim();
            result.add(substring);
        }
        if (ignoreTrailingEmpty && result.size() > 0) {
            //Discard empty substrings at the end
            for (int i = result.size() - 1; i >= 0; i--) {
                if (result.get(i).equals("")) {
                    result.remove(i);
                } else {
                    break;
                }
            }
        }
        return result;
    }

    private String packageName(String targetNamespace) {
        if (targetNamespace.startsWith("http://")) {
            targetNamespace = targetNamespace.substring("http://".length());
        }
        if (targetNamespace.startsWith("https://")) {
            targetNamespace = targetNamespace.substring("https://".length());
        }
        if (targetNamespace.startsWith("urn:")) {
            targetNamespace = targetNamespace.substring("urn:".length());
        }

        targetNamespace = targetNamespace.replace(":", "_");
        targetNamespace = targetNamespace.replace("/", "_");
        targetNamespace = targetNamespace.replace(".", "_");
        targetNamespace = targetNamespace.replace("-", "_");

        if (targetNamespace.length() > 40) {
            targetNamespace = targetNamespace.substring(0, 40);
        }

        List<String> split = splitSimple(targetNamespace, "_", 4, false, true);
        StringBuilder sb = new StringBuilder();
        if (split.size() > 0) {
            sb.append(split.get(0).toLowerCase());
        }
        for (int i = 1; i < split.size(); i++) {
            sb.append(firstLetterCap(split.get(i).toLowerCase()));
        }
        return sb.toString();
    }

    private String firstLetterCap(String s) { //mainly just need the writing part
        if (s == null || s.length() == 0)
            return "";
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static ArrayList<String> generateApexClass(HashMap<String, APackage> packages) throws Exception {
        try {
            ArrayList<String> scripts = new ArrayList<String>();
            if (packages == null) {
                System.out.println("packages is null");
            }
            for (APackage pkg : packages.values()) {
                AWriter writer = new AWriter();
                pkg.write(writer);
                scripts.add(writer.toString());
            }
            return scripts;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage(), e);
        }
    }

    public static HashMap<String, APackage> generate(String wsdl, WSDL2ApexOptions options) throws CalloutException {

        try {
            ApexTypeMapper typeMapper = new ApexTypeMapper();
            for (Map.Entry<String, String> entry : options.getPackageNamespaceMap().entrySet()) {
                typeMapper.registerNamespace(entry.getKey(), entry.getValue());
            }
            return parseWsdl(wsdl, typeMapper, options).getPackageMap();
        } catch (CalloutException e) {
            throw e;
        } catch (Throwable th) {
            System.out.println("Failed to generate Apex code from WSDL");
            throw new RuntimeException(th);
        }
    }

    public static class AnalysisResult {
        private ArrayList<String> messages = new ArrayList<String>();
        private boolean success;
        private HashMap<String, String> mapping = new HashMap<String, String>();

        public ArrayList<String> getMessages() {
            return messages;
        }

        public boolean isSuccess() {
            return success;
        }

        public HashMap<String, String> getMapping() {
            return mapping;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (success) {
                sb.append("SUCCESS:");
            } else {
                sb.append("ERROR:");
            }
            for (String message : messages) {
                sb.append(message);
            }
            return sb.toString();
        }
    }

    private static Packages parseWsdl(String wsdlFile, ApexTypeMapper mapper, WSDL2ApexOptions options)
            throws CalloutException {
        Packages packages = null;
        try {
            Definitions definitions = WsdlFactory.createFromString(wsdlFile);
            Types types = definitions.getTypes();
            packages = new Packages(definitions, mapper);

            if (types != null) {
                for (Schema schema : types.getSchemas()) {
                    String packageName = mapper.getPackageName(schema.getTargetNamespace());
                    APackage pkg = packages.getPackage(packageName);
                    pkg.load(schema); //for each complex type, create a class.  
                }
            }

            String packageName = mapper.getPackageName(definitions.getTargetNamespace());
            APackage pkg = packages.getPackage(packageName);
            pkg.load(definitions.getBinding());

            if (options.shouldGenerateSyncStub()) {
                StringBuilder result = new StringBuilder(packageName.length()).append(packageName);
                result.setCharAt(0, Character.toUpperCase(packageName.charAt(0)));
                pkg = packages.getPackage("Async" + result.toString());
                pkg.loadAsync(packages, definitions.getBinding());
            }

        } catch (ConnectionException e) {
            throw new CalloutException("Failed to parse wsdl: " + e, e);
        } catch (WsdlParseException e) {
            throw new CalloutException("Failed to parse wsdl: " + e, e);
        }
        return packages;
    }

    /**
     * @return an arraylist of all of the class names generated by parsing
     */
    public ArrayList<String> getAllClassNames() {
        return allClassNames;
    }

    /**
     * @return an arraylist of all of the classes generated by parsing
     */
    public ArrayList<String> getAllClasses() {
        return allClasses;
    }

    /**
     * @return a mapping between the namespace and the suggested name of the apex class
     */
    public HashMap<String, String> getResultFromParse() {
        return result.getMapping();
    }

    /**
     * Parses the wsdl file and stores the result
     */
    private void doParse() {
        try {
            //parse the wsdl
            result = new WSDL2Apex().analyze(wsdlString);
            String allMessages = new String();
            if (!result.isSuccess()) {
                ArrayList<String> messages = new ArrayList<String>();
                messages = result.getMessages();
                for (String message : messages) {
                    allMessages = allMessages + message + '\n';
                }
                throw new RuntimeException(allMessages);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    /**
     * Converts the result from the parse into strings. It will generate a list of apex classes and a list of apex class
     * names
     * 
     * @param async
     * @throws CalloutException
     */
    private void doGenerate(Boolean async) throws CalloutException {
        HashMap<String, String> inputMap = new HashMap<String, String>();
        //get the targetnamesapces
        inputMap = result.getMapping();
        HashMap<String, APackage> packageMap;
        allClassNames = new ArrayList<String>();
        allClasses = new ArrayList<String>();
        try {
            //generates the class
            packageMap = generate(wsdlString, WSDL2ApexOptions.newDefault(async).setPackageNamespaceMap(inputMap));
            //creates the class as a string
            allClasses = generateApexClass(packageMap);
            Iterator<String> j = packageMap.keySet().iterator();
            while (j.hasNext()) {
                allClassNames.add(j.next().toString());
            }
        }

        catch (CalloutException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts the generated class strings into files
     * 
     * @param resultPath
     * @throws IOException
     */
    private void generateFiles(String resultPath) throws IOException {
        try {
            Iterator<String> i = allClasses.iterator();
            Iterator<String> j = allClassNames.iterator();
            while (i.hasNext() && j.hasNext()) {
                //writes the class into a file
                FileString.StringToFile(resultPath, i.next().toString(), j.next().toString());
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    /**
     * Generate apex classes from the given wsdl file
     * 
     * @param args
     *            : the path to the wsdl file, a boolean saying if you want async classes, and the path to the result
     *            files (optional)
     * @throws RuntimeException
     * @throws CalloutException
     * @throws IOException
     */
    public void parseAndGenerate(String[] args) throws RuntimeException, CalloutException, IOException {
        String filePath = args[0];
        Boolean async;
        String resultPath = null;
        if (args.length == 3) {
            resultPath = args[1];
            async = Boolean.parseBoolean(args[2]);
        } else {
            LOGGER.log(
                Level.SEVERE,
                "Must contain the  following 3 arguements: path to the wsdl file, path to where you want to save the file (with a slash at the end), and whether you want an asynchronous class");
            throw new RuntimeException();
        }

        try {
            //get the wsdl in a stirng
            wsdlString = FileString.getStringFromFile(filePath);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        doParse();
        doGenerate(async);
        generateFiles(resultPath);
    }

    /**
     * Parses the wsdl file.
     * 
     * @param args
     *            : the wsdl file path
     * @throws IOException
     * @throws CalloutException
     */
    public void parse(String[] args) throws IOException, CalloutException {
        String filePath = args[0];

        if (args.length != 1) {
            LOGGER.log(Level.SEVERE, "You must provide only the wsdl file's path");
            throw new RuntimeException();
        }

        try {
            //get the wsdl in a string
            wsdlString = FileString.getStringFromFile(filePath);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        doParse();
    }

    /**
     * Only use this class right after you use the parse() method. Generates the classes from the parse result.
     * 
     * @param args
     * @throws CalloutException
     * @throws IOException
     */
    public void generate(String[] args) throws CalloutException, IOException {
        Boolean async;
        String resultPath = null;
        if (args.length == 1)
            async = Boolean.parseBoolean(args[0]);
        else if (args.length == 2) {
            async = Boolean.parseBoolean(args[0]);
            resultPath = args[1];
        } else {
            LOGGER.log(
                Level.SEVERE,
                "Must supply the following inputs: all of the class names and whether you want an asynchronous class"
                        + "or must supply the following inputs: all of the class names, whether you want an asynchronous class, and the result path (with a slash at the end");
            throw new RuntimeException();
        }

        doGenerate(async);
        if (resultPath != null) {
            generateFiles(resultPath);
        }
    }

}
