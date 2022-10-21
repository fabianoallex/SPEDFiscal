package SPEDFiscal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

class Field<T>  {
    private final String name;
    private T value;

    protected Field(String name){
        this.name = name;
        this.value = null;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getName(){
        return this.name;
    }

    public T getValue(){
        return value;
    }
}

public class Fields extends LinkedHashMap<String, Field<?>> {
    public static final String FIELD_FORMAT_STRING_ONLY_NUMBERS = "onlynumbers";
    private final String registerName;

    public String getRegisterName() {
        return registerName;
    }

    Fields(String registerName){
        this.registerName = registerName;
    }

    public void addField(Field<?> field) {
        this.put(field.getName(), field);
    }

    private String getFormattedField(String value, FieldFormat fieldFormat){
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

    private String getFormattedDoubleField(Field<?> field, FieldFormat fieldFormat){
        DecimalFormat df = new DecimalFormat(fieldFormat.getFormat());

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return getFormattedField(df.format(field.getValue()), fieldFormat);
    }

    private String getFormattedIntegerField(Field<?> field, FieldFormat fieldFormat){
        DecimalFormat df = new DecimalFormat(fieldFormat.getFormat());

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return getFormattedField(df.format(field.getValue()), fieldFormat);
    }

    private String getFormattedStringField(Field<?> field, FieldFormat fieldFormat){
        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        if (fieldFormat.getFormat().equals(FIELD_FORMAT_STRING_ONLY_NUMBERS)) {
            return getFormattedField(field.getValue().toString().replaceAll("\\D+",""), fieldFormat);
        }

        return getFormattedField(field.getValue().toString(), fieldFormat);
    }

    private String getFormattedDateField(Field<?> field, FieldFormat fieldFormat){
        SimpleDateFormat df = new SimpleDateFormat(fieldFormat.getFormat());

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return getFormattedField(df.format(field.getValue()), fieldFormat);
    }

    private String getFormattedField(Field<?> field, FieldFormat fieldFormat){
        if (!fieldFormat.getFormat().isEmpty()) {
            if (field.getValue() instanceof String) return getFormattedStringField(field, fieldFormat);
            if (field.getValue() instanceof Integer) return getFormattedIntegerField(field, fieldFormat);
            if (field.getValue() instanceof Double) return getFormattedDoubleField(field, fieldFormat);
            if (field.getValue() instanceof Date) return getFormattedDateField(field, fieldFormat);
        }

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return getFormattedField(field.getValue().toString(), fieldFormat);
    }

    public Field<?> getField(String name){
        for (Map.Entry<String, Field<?>> e : this.entrySet()) {
            Field<?> field = e.getValue();

            if (field.getName().equals(name))
                return field;
        }

        return null;
    }

    public String toString(){
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Field<?>> e : this.entrySet()) {
            Field<?> field = e.getValue();
            FieldFormat fieldFormat = DefinitionsLoader.getFieldFormat(this.registerName + "." + field.getName());
            result.append(getFormattedField(field, fieldFormat)).append(FieldDefinitions.FIELD_SEPARATOR);
        }

        return result.toString();
    }
}

