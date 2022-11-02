package SPEDFiscal;

public class SPEDDefinitions {
    private final String definitionsXmlFile;
    private final FieldsCreator fieldsCreator;

    public SPEDDefinitions(String definitionsXmlFile) {
        this.definitionsXmlFile = definitionsXmlFile;
        this.fieldsCreator = new FieldsCreator(definitionsXmlFile);
    }

    public String getDefinitionsXmlFile() {
        return definitionsXmlFile;
    }

    public FieldsCreator getFieldsCreator() {
        return this.fieldsCreator;
    }

    RegisterDefinitions getRegisterDefinitions(String registerName) {
        return DefinitionsLoader.getRegisterDefinitions(registerName, this.getDefinitionsXmlFile());
    }

    public FieldFormat getFieldFormatByFieldName(String fieldName) {
        return DefinitionsLoader.getFieldFormat(fieldName);
    }

    public Validation[] getValidations(String registerName, String fieldName) {
        return DefinitionsLoader.getValidations(registerName, fieldName);
    }

    public String getRequired(String registerName, String fieldName) {
        return DefinitionsLoader.getRequired(registerName, fieldName);
    }
}

class FieldDefinitions {
    public static final String FIELD_EMPTY_STRING = "";
    public static final String FIELD_SEPARATOR = "|";
    public static final String FIELD_DEF_NAME = "name";
    public static final String FIELD_DEF_POS = "pos";
    public static final String FIELD_DEF_TYPE = "type";
    public static final String FIELD_DEF_SIZE = "size";
    public static final String FIELD_DEF_DEC = "dec";
    public static final String FIELD_DEF_FORMAT = "format";
    public static final String FIELD_DEF_DESCRIPTION = "description";
    public static final String FIELD_DEF_REF = "ref";
    public static final String FIELD_DEF_VALIDATIONS = "validations";
    public static final String FIELD_DEF_REQUIRED = "required";

    String name;
    String pos;
    String type;
    String size;
    String dec;
    String format;
    String description;
    String ref;
    String validationNames;
    String required;
}

class RegisterDefinitions {
    public static final String REGISTER_DEF_NAME = "name";
    public static final String REGISTER_DEF_PARENT_TYPE = "parent_type";
    public static final String REGISTER_DEF_PARENT = "parent";
    public static final String REGISTER_DEF_KEY = "key";

    String name;
    String parentType;
    String parent;
    String key;
}


