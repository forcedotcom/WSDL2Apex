
class AField {
    
    private final String type;
    private final String name;
    private final String access;
    private final String value;

    AField(String access, String type, String name, String value) {
        this.access = access;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    boolean isPublic() {
        return "public".equals(access);
    }

    void write(AWriter writer) throws CalloutException {
        writer.startBlock();
        if (value == null) {
            writer.writeLine(access, " ", type, " ", name, ";");
        } else {
            writer.writeLine(access, " ", type, " ", name, " = ", value, ";");
        }
        writer.endBlock();
    }
}
