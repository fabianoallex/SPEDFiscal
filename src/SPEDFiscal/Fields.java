package SPEDFiscal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

class Field<T>  {
    private final String name;
    private T value;

    Field(String name){
        this.name = name;
        this.value = null;
    }

    public void setValue(T value) {
        this.value = value;
    }

    String getName(){
        return this.name;
    }

    T getValue(){
        return value;
    }
}

class IntegerField extends Field<Integer>{
    IntegerField(String name) {
        super(name);
    }
}

class DoubleField extends Field<Double> {
    DoubleField(String name) {
        super(name);
    }
}

class DateField extends Field<Date> {
    DateField(String name) {
        super(name);
    }
}

class StringField extends Field<String>{
    StringField(String name) {
        super(name);
    }
}

class FieldFormat {
    String format;
    int maxSize;

    FieldFormat(String format){
        this.format = format;
        this.maxSize = 0;
    }

    FieldFormat(String format, int maxSize){
        this.format = format;
        this.maxSize = maxSize;
    }

    FieldFormat(int maxSize){
        this.format = "";
        this.maxSize = maxSize;
    }

    FieldFormat(){
        this.format = "";
        this.maxSize = 0;
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

class FieldsFormat extends HashMap<String, FieldFormat>{

}

public class Fields extends LinkedHashMap<String, Field<?>> {
    private static final HashMap<String, FieldsFormat> fieldsFormat = new HashMap<>();
    public static FieldsFormat getFieldsFormat(Fields fields){
        return fieldsFormat.get(fields.getName());
    }

    private final String name;
    public String getName() {
        return name;
    }

    private static void addFieldsFormat(Fields fields){
        if (Fields.getFieldsFormat(fields) == null) {
            fieldsFormat.put(fields.getName(), new FieldsFormat());
        }
    }

    public static Fields createFields(String name, String fieldsDefinitionsXmlPath) {
        Fields fields = new Fields(name);
        FieldsFormat fieldsFormat = Fields.getFieldsFormat(fields);

        boolean thereIsFormats = !fieldsFormat.isEmpty();

        for (FieldDefinitions fieldDefinitions : DefinitionsLoader.getFieldsRegDefinitions(name, fieldsDefinitionsXmlPath)) {
            String fieldName = fieldDefinitions.name;
            String type = fieldDefinitions.type;
            String size = fieldDefinitions.size;
            String dec = fieldDefinitions.dec;
            String format = fieldDefinitions.format;

            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_STRING)) fields.addField(new StringField(fieldName));
            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_DATE)) fields.addField(new DateField(fieldName));
            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_NUMBER)) fields.addField(dec.isEmpty() ? new IntegerField(fieldName) : new DoubleField(fieldName));

            if (!thereIsFormats) {
                fieldsFormat.put(fieldName, new FieldFormat(format, Integer.parseInt("0" + size)));
            }
        }

        return fields;
    }

    private Fields(String name){
        this.name = name;
        Fields.addFieldsFormat(this);
    }

    void addField(Field<?> field) {
        this.put(field.getName(), field);
    }

    void setIntegerValue(String name, Integer value) {
        IntegerField field = (IntegerField) this.get(name);
        field.setValue(value);
    }

    void setStringValue(String name, String value){
        StringField field = (StringField) this.get(name);
        field.setValue(value);
    }

    void setDateValue(String name, Date value){
        DateField field = (DateField) this.get(name);
        field.setValue(value);
    }

    void setDoubleValue(String name, Double value){
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

    private String getFormattedDateField(DateField field, FieldFormat fieldFormat){
        SimpleDateFormat df = new SimpleDateFormat(fieldFormat.getFormat());

        if (field.getValue() == null)
            return FieldDefinitions.FIELD_EMPTY_STRING;

        return getFormattedField(df.format(field.getValue()), fieldFormat);
    }

    private String getFormattedField(Field<?> field, FieldFormat fieldFormat){
        if (!fieldFormat.format.isEmpty()) {
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
        FieldsFormat fieldsFormat = Fields.getFieldsFormat(this);
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Field<?>> e : this.entrySet()) {
            Field<?> field = e.getValue();
            FieldFormat fieldFormat = fieldsFormat.get(field.getName());
            result.append(getFormattedField(field, fieldFormat)).append(FieldDefinitions.FIELD_SEPARATOR);
        }

        return result.toString();
    }
}

