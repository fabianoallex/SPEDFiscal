package SPEDFiscal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FieldFormatter {
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

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return formatField(field.getValue().toString(), fieldFormat);
    }

    private String formatField(String value, FieldFormat fieldFormat) {
        value = value
                .replace(FieldDefinitions.FIELD_SEPARATOR, " ")
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

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return formatField(df.format(field.getValue()), fieldFormat);
    }

    private String formatIntegerField(Field<?> field, FieldFormat fieldFormat) {
        DecimalFormat df = new DecimalFormat(fieldFormat.getFormat());

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return formatField(df.format(field.getValue()), fieldFormat);
    }

    private String formatStringField(Field<?> field, FieldFormat fieldFormat) {
        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        if (fieldFormat.getFormat().equals(FIELD_FORMAT_STRING_ONLY_NUMBERS)) {
            return formatField(field.getValue().toString().replaceAll("\\D+", ""), fieldFormat);
        }

        return formatField(field.getValue().toString(), fieldFormat);
    }

    private String formatDateField(Field<?> field, FieldFormat fieldFormat) {
        SimpleDateFormat df = new SimpleDateFormat(fieldFormat.getFormat());

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return formatField(df.format(field.getValue()), fieldFormat);
    }


}
