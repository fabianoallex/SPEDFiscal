import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

class FieldDefinitions {
    public static final String FIELD_EMPTY_STRING = "";
    public static final String FIELD_SEPARATOR = "|";
    public static final String FIELD_DEF_NAME = "name";
    public static final String FIELD_DEF_TYPE = "type";
    public static final String FIELD_DEF_SIZE = "size";
    public static final String FIELD_DEF_DEC = "size";
    public static final String FIELD_DEF_FORMAT = "format";
    public static final String FIELD_DEF_DESCRIPTION = "description";

    String name;
    String type;
    String size;
    String dec;
    String format;
    String description;
}
class FieldsDefinition {
    private static class XMLDefinitionsFieldsHandler extends DefaultHandler {
        public void startDocument() {}
        public void endDocument() {}
        private String registerName = "";
        private List<FieldDefinitions> fieldsDefinitions = null;

        public void startElement( String uri, String localName, String tag, Attributes attributes)  {
            if (tag.equals("register")){
                registerName = attributes.getValue("name");
                fieldsDefinitions = new ArrayList<FieldDefinitions>();
            }

            if (!registerName.isEmpty() && tag.equals("field")) {
                FieldDefinitions fieldDefinitions = new FieldDefinitions();

                fieldDefinitions.name = attributes.getValue(FieldDefinitions.FIELD_DEF_NAME);
                fieldDefinitions.type = attributes.getValue(FieldDefinitions.FIELD_DEF_TYPE);
                fieldDefinitions.size = attributes.getValue(FieldDefinitions.FIELD_DEF_SIZE);
                fieldDefinitions.dec = attributes.getValue(FieldDefinitions.FIELD_DEF_DEC);
                fieldDefinitions.format = attributes.getValue(FieldDefinitions.FIELD_DEF_FORMAT);
                fieldDefinitions.description = attributes.getValue(FieldDefinitions.FIELD_DEF_DESCRIPTION);

                fieldsDefinitions.add(fieldDefinitions);
            }
        }

        public void endElement(String uri, String localName, String tag) {
            if (tag.equals("register")) {
                FieldDefinitions[] fd = new FieldDefinitions[fieldsDefinitions.size()];
                fieldsDefinitions.toArray(fd);
                FieldsDefinition.addFieldDefinitions(registerName, fd);
            }
        }

        public void characters(char[] ch, int start, int length) {}
    }

    private static HashMap<String, FieldDefinitions[]> fieldsRegMap = null;
    public static final String FIELDS_REG_TYPE_STRING = "string";
    public static final String FIELDS_REG_TYPE_NUMBER = "number";
    public static final String FIELDS_REG_TYPE_DATE = "date";

    public static void addFieldDefinitions(String name, FieldDefinitions[] fieldDefinitions){
        if (fieldsRegMap == null){
            fieldsRegMap = new HashMap<>();
        }

        fieldsRegMap.put(name, fieldDefinitions);
    }

    public static FieldDefinitions[] getFieldsRegDefinitions(String name){
        if (fieldsRegMap == null){
            fieldsRegMap = new HashMap<>();
            populate();  //todo: verificar se vai ficar aqui mesmo
        }

        return fieldsRegMap.get(name);
    }

    public static void populate(){
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InputSource input = new InputSource("c:\\fabiano\\xml.xml");
            parser.parse(input, new XMLDefinitionsFieldsHandler());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}

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

    public static Fields createFields(String name) {
        Fields fields = new Fields(name);
        FieldsFormat fieldsFormat = Fields.getFieldsFormat(fields);

        boolean thereIsFormats = !fieldsFormat.isEmpty();

        for (FieldDefinitions fieldDefinitions : FieldsDefinition.getFieldsRegDefinitions(name)) {
            String fieldName = fieldDefinitions.name;
            String type = fieldDefinitions.type;
            String size = fieldDefinitions.size;
            String dec = fieldDefinitions.dec;
            String format = fieldDefinitions.format;

            if (type.equals(FieldsDefinition.FIELDS_REG_TYPE_STRING)) fields.addField(new StringField(fieldName));
            if (type.equals(FieldsDefinition.FIELDS_REG_TYPE_DATE)) fields.addField(new DateField(fieldName));
            if (type.equals(FieldsDefinition.FIELDS_REG_TYPE_NUMBER)) fields.addField(dec.isEmpty() ? new IntegerField(fieldName) : new DoubleField(fieldName));

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
                    .replace(FieldDefinitions.FIELD_SEPARATOR, FieldDefinitions.FIELD_EMPTY_STRING)
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
            return "";
        return getFormattedField(df.format(field.getValue()), fieldFormat);
    }

    private String getFormattedIntegerField(IntegerField field, FieldFormat fieldFormat){
        DecimalFormat df = new DecimalFormat(fieldFormat.getFormat());
        if (field.getValue() == null)
            return "";
        return getFormattedField(df.format(field.getValue()), fieldFormat);
    }

    private String getFormattedDateField(DateField field, FieldFormat fieldFormat){
        SimpleDateFormat df = new SimpleDateFormat(fieldFormat.getFormat());
        if (field.getValue() == null)
            return "";
        return getFormattedField(df.format(field.getValue()), fieldFormat);
    }

    private String getFormattedField(Field<?> field, FieldFormat fieldFormat){
        if (!fieldFormat.format.isEmpty()) {
            if (field instanceof IntegerField) return getFormattedIntegerField((IntegerField) field, fieldFormat);
            if (field instanceof DoubleField) return getFormattedDoubleField((DoubleField) field, fieldFormat);
            if (field instanceof DateField) return getFormattedDateField((DateField) field, fieldFormat);
        }

        if (field.getValue() == null)
            return "";
        return getFormattedField(field.getValue().toString(), fieldFormat);
    }

    public Field<?> getFieldByName(String name){

        for (Map.Entry<String, Field<?>> e : this.entrySet()) {
            Field<?> field = e.getValue();

            if (field.getName().equals(name))
                return field;
        }

        return null;
    }

    public IntegerField getIntegerFieldByName(String name){
        Field<?> field = getFieldByName(name);
        return (field instanceof IntegerField) ? (IntegerField) field : null;
    }

    public StringField getStringFieldByName(String name){
        Field<?> field = getFieldByName(name);
        return (field instanceof StringField) ? (StringField) field : null;
    }

    public DoubleField getDoubleFieldByName(String name) {
        Field<?> field = getFieldByName(name);
        return (field instanceof DoubleField) ? (DoubleField) field : null;
    }

    public DateField getDateFieldByName(String name) {
        Field<?> field = getFieldByName(name);
        return (field instanceof DateField) ? (DateField) field : null;
    }

    public String toString(){
        FieldsFormat fieldsFormat = Fields.getFieldsFormat(this);
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Field<?>> e : this.entrySet()) {
            String s = e.getKey();
            Field<?> field = e.getValue();

            FieldFormat fieldFormat = fieldsFormat.get(field.getName());
            result.append(getFormattedField(field, fieldFormat)).append("|");
        }

        return result.toString();
    }
}