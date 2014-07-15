

import java.util.ArrayList;

abstract class AClass extends ABase {
    
    protected final String name;
    protected final String extendsClass;
    protected final ArrayList<AField> fields = new ArrayList<AField>();
    protected final ArrayList<AMethod> methods = new ArrayList<AMethod>();

    protected AClass(Definitions definitions, ApexTypeMapper typeMapper, String name, String extendsClass) {
        super(definitions, typeMapper);
        this.name = name;
        this.extendsClass = extendsClass;
    }
    
    String getName() {
        return name;
    }

    void write(AWriter writer) throws CalloutException {
        writer.startBlock();
        if (extendsClass == null)
            writer.writeLine("public class ", name, " {");
        else
            writer.writeLine("public class ", name, " extends ", extendsClass, " {");
        
        for (AField field : fields) {
            if (field.isPublic()) {
                field.write(writer);
            }
        }
        for (AField field : fields) {
            if (!field.isPublic()) {
                field.write(writer);
            }
        }
        for (AMethod method : methods) {
            method.write(writer);
        }
        writer.writeLine("}");
        writer.endBlock();
    }
}
