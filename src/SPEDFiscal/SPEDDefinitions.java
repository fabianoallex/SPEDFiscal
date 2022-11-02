package SPEDFiscal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SPEDDefinitions {
    private final String definitionsXmlFile;
    private final FieldsCreator fieldsCreator;
    private final FieldFormatter fieldFormatter;

    public SPEDDefinitions(String definitionsXmlFile) {
        this.definitionsXmlFile = definitionsXmlFile;
        this.fieldsCreator = new FieldsCreator(definitionsXmlFile);
        this.fieldFormatter = new FieldFormatter();
    }

    public String getDefinitionsXmlFile() {
        return definitionsXmlFile;
    }

    public FieldsCreator getFieldsCreator() {
        return this.fieldsCreator;
    }

    public FieldFormatter getFieldFormatter() {
        return this.fieldFormatter;
    }

    RegisterDefinitions getRegisterDefinitions(String registerName) {
        return DefinitionsLoader.getRegisterDefinitions(registerName, this.getDefinitionsXmlFile());
    }

    public FieldFormat getFieldFormat(String fieldName) {
        return DefinitionsLoader.getFieldFormat(fieldName);
    }

    public Validation[] getValidations(String registerName, String fieldName) {
        return DefinitionsLoader.getValidations(registerName, fieldName);
    }

    public String getInnerValidation(String registerName, String fieldName) {
        return DefinitionsLoader.getInnerValidation(registerName, fieldName);
    }

    public String getRequired(String registerName, String fieldName) {
        return DefinitionsLoader.getRequired(registerName, fieldName);
    }

    public String formatField(Field<?> field, String fieldFormatName) {
        FieldFormat fieldFormat = this.getFieldFormat(fieldFormatName);
        return this.getFieldFormatter().formatField(field, fieldFormat);
    }
}

class FieldsCreator {
    private final String definitionsXmlFile;

    FieldsCreator(String definitionsXmlFile) {
        this.definitionsXmlFile = definitionsXmlFile;
    }

    Fields create(String registerName){
        Fields fields = new Fields();

        for (FieldDefinitions fieldDefinitions : DefinitionsLoader.getFieldsDefinitions(registerName, definitionsXmlFile)) {
            String fieldName = fieldDefinitions.name;
            String type = fieldDefinitions.type;
            String size = fieldDefinitions.size;
            String dec = fieldDefinitions.dec;
            String format = fieldDefinitions.format;
            String ref = fieldDefinitions.ref;

            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_STRING)) fields.addField(new Field<String>(fieldName));
            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_DATE)) fields.addField(new Field<Date>(fieldName));

            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_NUMBER))
                fields.addField(dec.isEmpty() ?
                        new Field<Integer>(fieldName) :
                        new Field<Double>(fieldName));
        }

        return fields;
    }
}

class FieldFormatter {
    public static final String FIELD_FORMAT_STRING_ONLY_NUMBERS = "onlynumbers";

    public String formatField(Field<?> field, FieldFormat fieldFormat)  {
        if (field.getValue() instanceof Register register) {
            Field<?> tempField = new Field<>();
            tempField.setValue(register.getID());
            return this.formatField(tempField, fieldFormat); //recursive
        }

        if (!fieldFormat.getFormat().isEmpty()) {
            if (field.getValue() instanceof String) return formatStringField(field, fieldFormat);
            if (field.getValue() instanceof Integer) return formatIntegerField(field, fieldFormat);
            if (field.getValue() instanceof Double) return formatDoubleField(field, fieldFormat);
            if (field.getValue() instanceof Date) return formatDateField(field, fieldFormat);
        }

        if (field.getValue() == null) return FieldDefinitions.FIELD_EMPTY_STRING;
        return formatField(field.getValue().toString(), fieldFormat);
    }

    private String formatField(String value, FieldFormat fieldFormat) {
        value = value
                .replace(FieldDefinitions.FIELD_SEPARATOR, "/")
                .replace("\n", FieldDefinitions.FIELD_EMPTY_STRING)
                .replace("\r", FieldDefinitions.FIELD_EMPTY_STRING)
                .trim();

        return (fieldFormat.getMaxSize() > 0 && value.length() > fieldFormat.getMaxSize())
                ?
                value.substring(0, fieldFormat.getMaxSize()).trim() :
                value;
    }

    private String formatDoubleField(Field<?> field, FieldFormat fieldFormat) {
        DecimalFormat df = new DecimalFormat(fieldFormat.getFormat());
        if (field.getValue() == null) return FieldDefinitions.FIELD_EMPTY_STRING;
        return formatField(df.format(field.getValue()), fieldFormat);
    }

    private String formatIntegerField(Field<?> field, FieldFormat fieldFormat) {
        DecimalFormat df = new DecimalFormat(fieldFormat.getFormat());
        if (field.getValue() == null) return FieldDefinitions.FIELD_EMPTY_STRING;
        return formatField(df.format(field.getValue()), fieldFormat);
    }

    private String formatStringField(Field<?> field, FieldFormat fieldFormat) {
        if (field.getValue() == null) return FieldDefinitions.FIELD_EMPTY_STRING;

        if (fieldFormat.getFormat().equals(FIELD_FORMAT_STRING_ONLY_NUMBERS))
            return formatField(field.getValue().toString().replaceAll("\\D+", ""), fieldFormat);

        return formatField(field.getValue().toString(), fieldFormat);
    }

    private String formatDateField(Field<?> field, FieldFormat fieldFormat) {
        SimpleDateFormat df = new SimpleDateFormat(fieldFormat.getFormat());
        if (field.getValue() == null) return FieldDefinitions.FIELD_EMPTY_STRING;
        return formatField(df.format(field.getValue()), fieldFormat);
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
    public static final String FIELD_DEF_INNER_VALIDATION = "inner_validation";
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
    String innerValidation;
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

class FieldFormat {
    private String format;
    private int maxSize;

    FieldFormat(String format, int maxSize){
        this.format = format;
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}

