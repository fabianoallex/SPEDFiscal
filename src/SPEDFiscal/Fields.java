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

class IntegerField extends Field<Integer>{
    protected IntegerField(String name) {
        super(name);
    }
}

class DoubleField extends Field<Double> {
    protected DoubleField(String name) {
        super(name);
    }
}

class DateField extends Field<Date> {
    protected DateField(String name) {
        super(name);
    }
}

class StringField extends Field<String>{
    protected StringField(String name) {
        super(name);
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

    public void setIntegerValue(String name, Integer value) {
        IntegerField field = (IntegerField) this.get(name);
        field.setValue(value);
    }

    public void setStringValue(String name, String value){
        StringField field = (StringField) this.get(name);
        field.setValue(value);
    }

    public void setDateValue(String name, Date value){
        DateField field = (DateField) this.get(name);
        field.setValue(value);
    }

    public void setDoubleValue(String name, Double value){
        DoubleField field = (DoubleField) this.get(name);
        field.setValue(value);
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

    private String getFormattedDoubleField(DoubleField field, FieldFormat fieldFormat){
        DecimalFormat df = new DecimalFormat(fieldFormat.getFormat());

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return getFormattedField(df.format(field.getValue()), fieldFormat);
    }

    private String getFormattedIntegerField(IntegerField field, FieldFormat fieldFormat){
        DecimalFormat df = new DecimalFormat(fieldFormat.getFormat());

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return getFormattedField(df.format(field.getValue()), fieldFormat);
    }

    private String getFormattedStringField(StringField field, FieldFormat fieldFormat){
        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        if (fieldFormat.getFormat().equals(FIELD_FORMAT_STRING_ONLY_NUMBERS)) {
            return getFormattedField(field.getValue().replaceAll("\\D+",""), fieldFormat);
        }

        return getFormattedField(field.getValue(), fieldFormat);
    }

    private String getFormattedDateField(DateField field, FieldFormat fieldFormat){
        SimpleDateFormat df = new SimpleDateFormat(fieldFormat.getFormat());

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return getFormattedField(df.format(field.getValue()), fieldFormat);
    }

    private String getFormattedField(Field<?> field, FieldFormat fieldFormat){
        if (!fieldFormat.getFormat().isEmpty()) {
            if (field instanceof StringField) return getFormattedStringField((StringField) field, fieldFormat);
            if (field instanceof IntegerField) return getFormattedIntegerField((IntegerField) field, fieldFormat);
            if (field instanceof DoubleField) return getFormattedDoubleField((DoubleField) field, fieldFormat);
            if (field instanceof DateField) return getFormattedDateField((DateField) field, fieldFormat);
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

    public IntegerField getIntegerField(String name){
        Field<?> field = getField(name);
        return (field instanceof IntegerField) ? (IntegerField) field : null;
    }

    public StringField getStringField(String name){
        Field<?> field = getField(name);
        return (field instanceof StringField) ? (StringField) field : null;
    }

    public DoubleField getDoubleField(String name) {
        Field<?> field = getField(name);
        return (field instanceof DoubleField) ? (DoubleField) field : null;
    }

    public DateField getDateField(String name) {
        Field<?> field = getField(name);
        return (field instanceof DateField) ? (DateField) field : null;
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

