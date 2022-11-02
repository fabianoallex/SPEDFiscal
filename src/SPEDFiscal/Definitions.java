package SPEDFiscal;

import java.util.HashMap;

public class Definitions {
    public static final String DEF_TAG_DEFINITIONS = "definitions";
    public static final String DEF_TAG_REGISTER = "register";
    public static final String DEF_TAG_FIELD = "field";
    public static final String DEF_TAG_FIELDS = "fields";
    public static final String DEF_TAG_REGEXS = "regexs";
    public static final String DEF_TAG_REGEX = "regex";
    public static final String DEF_TAG_SCRIPT = "script";
    public static final String FIELDS_REG_TYPE_STRING = "string";
    public static final String FIELDS_REG_TYPE_NUMBER = "number";
    public static final String FIELDS_REG_TYPE_DATE = "date";
    private static final HashMap<String, FieldFormat> fieldsFormat = new HashMap<>();
    private static final HashMap<String, Validation> validations = new HashMap<>();
    private static HashMap<String, FieldDefinitions[]> fieldsDefinitions = null;
    private static HashMap<String, RegisterDefinitions> registersDefinitions = null;
    private final String xmlFile;

    protected static void addValidation(Validation validation) {
        validations.put(validation.getName(), validation);
    }

    public static Validation[] getValidations(String registerName, String fieldName) {
        String validationNames = "";
        for (FieldDefinitions fieldDefinitions : fieldsDefinitions.get(registerName)) {
            if (fieldDefinitions.name.equals(fieldName)) {
                validationNames = fieldDefinitions.validationNames;
                break;
            }
        }

        if (validationNames == null || validationNames.trim().equals(""))
            return null;

        String[] validationNamesArray = validationNames.split(",");
        Validation[] validationArray = new Validation[validationNamesArray.length];

        for (int i = 0; i < validationNamesArray.length; i++) {
            validationArray[i] = validations.get(validationNamesArray[i].trim());
        }

        return validationArray;
    }

    public static String getRequired(String registerName, String fieldName) {
        String result = "";

        for (FieldDefinitions fieldDefinitions : fieldsDefinitions.get(registerName)) {
            if (fieldDefinitions.name.equals(fieldName)) {
                result = fieldDefinitions.required;
                break;
            }
        }

        return result;
    }

    public static void addFieldFormat(String name, FieldFormat fieldFormat) {
        fieldsFormat.put(name, fieldFormat);
    }

    public static void addFieldDefinitions(String name, FieldDefinitions[] fieldDefinitions) {
        if (fieldsDefinitions == null) fieldsDefinitions = new HashMap<>();
        fieldsDefinitions.put(name, fieldDefinitions);
    }

    public static void addRegisterDefinitions(String name, RegisterDefinitions registerDefinitions) {
        if (registersDefinitions == null) registersDefinitions = new HashMap<>();
        registersDefinitions.put(name, registerDefinitions);
    }

    public static FieldDefinitions[] getFieldsDefinitions(String name, String definitionsXmlFile) {
        if (fieldsDefinitions == null) {
            fieldsDefinitions = new HashMap<>();
            DefinitionsLoader.load(definitionsXmlFile);
        }

        return fieldsDefinitions.get(name);
    }

    public static FieldFormat getFieldFormatByFieldName(String fieldName) {
        return fieldsFormat.get(fieldName);
    }

    public Definitions(String xmlFile) {
        this.xmlFile = xmlFile;
    }

    public String getXmlFile() {
        return xmlFile;
    }

    RegisterDefinitions getRegisterDefinitions(String registerName) {
        if (registersDefinitions == null) {
            registersDefinitions = new HashMap<>();
            DefinitionsLoader.load(this.getXmlFile());
        }

        return registersDefinitions.get(registerName);
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


