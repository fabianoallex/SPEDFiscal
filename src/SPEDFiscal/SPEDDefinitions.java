package SPEDFiscal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SPEDDefinitions {
    private final String definitionsXmlPath;
    private final FieldsCreator fieldsCreator;
    private final FieldFormatter fieldFormatter;

    public SPEDDefinitions(String definitionsXmlPath) {
        this.definitionsXmlPath = definitionsXmlPath;
        this.fieldsCreator = new FieldsCreator(definitionsXmlPath);
        this.fieldFormatter = new FieldFormatter();
    }

    public String getDefinitionsXmlPath() {
        return definitionsXmlPath;
    }

    public FieldsCreator getFieldsCreator() {
        return this.fieldsCreator;
    }

    public FieldFormatter getFieldFormatter() {
        return this.fieldFormatter;
    }

    RegisterDefinitions getRegisterDefinitions(String registerName) {
        return DefinitionsLoader.getRegisterDefinitions(registerName, this.getDefinitionsXmlPath());
    }

    public FieldFormat getFieldFormat(String name) {
        return DefinitionsLoader.getFieldFormat(name);
    }

    public String formatField(Field<?> field, String fieldFormatName) throws FieldNotFoundException {
        FieldFormat fieldFormat = this.getFieldFormat(fieldFormatName);
        return this.getFieldFormatter().formatField(field, fieldFormat);
    }
}

class FieldsCreator {
    private final String definitionsXmlPath;

    FieldsCreator(String definitionsXmlPath) {
        this.definitionsXmlPath = definitionsXmlPath;
    }

    Fields create(String registerName){
        Fields fields = new Fields();

        for (FieldDefinitions fieldDefinitions : DefinitionsLoader.getFieldsDefinitions(registerName, definitionsXmlPath)) {
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

    public String formatField(Field<?> field, FieldFormat fieldFormat) throws FieldNotFoundException {
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

class FieldValidator {
    void validateField(Field field, String validation, ValidationListener validationListener) {

    }
}