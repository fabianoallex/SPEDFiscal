package sped.core;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FieldFormatter {
    public static final String FIELD_FORMAT_STRING_ONLY_NUMBERS = "onlynumbers";
    public static final String FIELD_FORMAT_LCDPR_DOUBLE_FORMAT = "lcdprdouble";

    static public String formatField(Field<?> field, Register register) {
        String fieldFormatName = register.getName() + "." + field.getName();
        FieldFormat fieldFormat = register.getFactory().getDefinitions().getFieldFormatByFieldName(fieldFormatName);
        return formatField(field, fieldFormat);
    }

    static public String formatField(Field<?> field, FieldFormat fieldFormat) {
        if (field.getValue() instanceof Register register) {
            Field<?> tempField = new Field<>();
            tempField.setValue(register.getID());
            return formatField(tempField, fieldFormat); //recursive
        }

        if (!fieldFormat.getFormat().isEmpty()) {
            if (field.getValue() instanceof String) return formatStringField(field, fieldFormat);
            if (field.getValue() instanceof Integer) return formatIntegerField(field, fieldFormat);
            if (field.getValue() instanceof Double) return formatDoubleField(field, fieldFormat);
            if (field.getValue() instanceof Date) return formatDateField(field, fieldFormat);
        }

        if (field.getValue() == null) return FieldDefinitions.FIELD_EMPTY_STRING;
        return sanitizeField(field.getValue().toString(), fieldFormat);
    }

    static private String sanitizeField(String value, FieldFormat fieldFormat) {
        value = value
                .replace("\n", FieldDefinitions.FIELD_EMPTY_STRING)
                .replace("\r", FieldDefinitions.FIELD_EMPTY_STRING)
                .trim();

        return (fieldFormat.getMaxSize() > 0 && value.length() > fieldFormat.getMaxSize())
                ?
                value.substring(0, fieldFormat.getMaxSize()).trim() :
                value;
    }

    static private String formatDoubleField(Field<?> field, FieldFormat fieldFormat) {
        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        if (fieldFormat.getFormat().equals(FIELD_FORMAT_LCDPR_DOUBLE_FORMAT)) {
            DecimalFormat df = new DecimalFormat("0");
            return sanitizeField(df.format(((Field<Double>)field).getValue()*100), fieldFormat);
        }

        DecimalFormat df = new DecimalFormat(fieldFormat.getFormat());
        return sanitizeField(df.format(field.getValue()), fieldFormat);
    }

    static private String formatIntegerField(Field<?> field, FieldFormat fieldFormat) {
        DecimalFormat df = new DecimalFormat(fieldFormat.getFormat());
        if (field.getValue() == null) return FieldDefinitions.FIELD_EMPTY_STRING;
        return sanitizeField(df.format(field.getValue()), fieldFormat);
    }

    static private String formatStringField(Field<?> field, FieldFormat fieldFormat) {
        if (field.getValue() == null) return FieldDefinitions.FIELD_EMPTY_STRING;

        if (fieldFormat.getFormat().equals(FIELD_FORMAT_STRING_ONLY_NUMBERS))
            return sanitizeField(field.getValue().toString().replaceAll("\\D+", ""), fieldFormat);

        return sanitizeField(field.getValue().toString(), fieldFormat);
    }

    static private String formatDateField(Field<?> field, FieldFormat fieldFormat) {
        SimpleDateFormat df = new SimpleDateFormat(fieldFormat.getFormat());
        if (field.getValue() == null) return FieldDefinitions.FIELD_EMPTY_STRING;
        return sanitizeField(df.format(field.getValue()), fieldFormat);
    }
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