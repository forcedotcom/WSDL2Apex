//package common.api.callout;

import java.sql.SQLException;
import java.util.*;

/**
 * Wsdl2Apex
 *
 * @author cheenath
 * @version 1.0
 * @since 148  Feb 7, 2007
 */
public class Wsdl2Apex {
    private static final String CLIENT_NAME = "Wsdl2Apex";
    private static final double CLIENT_VERSION = 1.0;

    public Wsdl2Apex() {
    }

    public AnalysisResult analyze(String wsdl) {
        AnalysisResult result = new AnalysisResult();
        result.success = false;

        try {
            Definitions definitions;
            definitions = WsdlFactory.createFromString(wsdl);
            result.mapping.put(definitions.getTargetNamespace(), packageName(definitions.getTargetNamespace()));

            if (definitions.getTypes() != null) { 
                for(Schema schema: definitions.getTypes().getSchemas()) { 
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

    private static List<String> splitSimple(String str, String delimiter, int expectedSize, boolean shouldTrim, boolean ignoreTrailingEmpty) {
        if (str == null) {
            return null;
        }
        List<String> result = (expectedSize == 0)? new LinkedList<String>(): new ArrayList<String>(expectedSize);

        if(delimiter.length() == 0) {
            if(!ignoreTrailingEmpty) throw new IllegalArgumentException();

            //Special case to match java's behavior
            char[] chars = new char[str.length()];
            str.getChars(0, str.length(), chars, 0);
            result.add("");
            for(char c : chars) {
                result.add(Character.toString(c));
            }
            return result;
        }

        //Special case to match java's behavior
        if(ignoreTrailingEmpty && "".equals(str)) {
            result.add("");
            return result;
        }

        int start = 0;
        int indexof;
        while ((indexof = str.indexOf(delimiter, start)) != -1) {
            String substring = str.substring(start, indexof);
            if (shouldTrim) substring = substring.trim();
            result.add(substring);
            start = indexof + delimiter.length();
            if (start >= str.length())
                break;
        }
        if (start == str.length()) {
            result.add("");
        } else if (start < str.length()) {
            String substring = str.substring(start);
            if (shouldTrim) substring = substring.trim();
            result.add(substring);
        }
        if(ignoreTrailingEmpty && result.size() > 0) {
            //Discard empty substrings at the end
            for(int i=result.size()-1; i>=0; i--) {
                if(result.get(i).equals("")) {
                    result.remove(i);
                }
                else {
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
        for (int i=1; i<split.size(); i++) {
            sb.append(firstLetterCap(split.get(i).toLowerCase()));
        }
        return sb.toString();
    }

    private String firstLetterCap(String s) { //mainly just need the writing part
        if (s == null || s.length() == 0) return "";
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public ArrayList<String> generateApexClass(HashMap<String, APackage> packages) throws Exception
    {
    	try {
            ArrayList<String> scripts = new ArrayList<String>();
            if(packages == null)
            {
            	System.out.println("packages is null");
            }
            for (APackage pkg : packages.values()) {
                AWriter writer = new AWriter();
                pkg.write(writer);
                scripts.add(writer.toString());
            }
            return scripts;
        }
        catch (Exception e)
        {
        	System.out.println(e.getMessage());
        	throw new Exception(e.getMessage(),e);
        }
    }
    
    public LinkedHashMap<String, APackage> generate(String wsdl, Wsdl2ApexOptions options) throws CalloutException {
        
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
            http://localhost:6109/
            for (String message : messages) {
                sb.append(message);
            }
            return sb.toString();
        }
    }

    private Packages parseWsdl(String wsdlFile, ApexTypeMapper mapper, Wsdl2ApexOptions options) throws CalloutException {
        Packages packages = null;
        try {
            Definitions definitions = WsdlFactory.createFromString(wsdlFile);
            Types types = definitions.getTypes();
            packages = new Packages(definitions, mapper);
            
            if (types != null) { 
                for(Schema schema: types.getSchemas()) {
                    String packageName = mapper.getPackageName(schema.getTargetNamespace());
                    APackage pkg = packages.getPackage(packageName);
                    pkg.load(schema);  //for each complex type, create a class.  
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
}
