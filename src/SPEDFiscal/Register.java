package SPEDFiscal;

import java.util.ArrayList;
import java.util.Date;

public class Register implements Unit {
    private final String name;
    private final ArrayList<Register> registers;
    private final Fields fields;
    Writer writer;
    SPEDConfig config;

    Register(String name, Writer writer, SPEDConfig config){
        this.name = name;
        this.writer = writer;
        this.config = config;
        registers = new ArrayList<>();
        this.fields = Fields.createFields(name, this.config.getFieldsDefinitionsXmlPath());
    }

    public Fields getFields() {
        return fields;
    }

    public Writer getWriter() {
        return writer;
    }

    public Register addRegister(String name){
        Register register = new Register(name, writer, this.config);
        this.registers.add(register);
        return register;
    }

    @Override
    public void count(Counter counter) {
        counter.increment(this.name);
        registers.forEach((unit -> unit.count(counter)));
    }

    @Override
    public int count() {
        int c = 1; //itself

        for (Unit unit : registers) {
            c += unit.count();
        }

        return c;
    }

    @Override
    public void validate() {
        //todo: implementar rotinas de validação dos registros
    }



    @Override
    public void write() {
        writer.write(this.toString());

        for (Register register : registers) {
            register.write();
        }
    }

    @Override
    public String toString() {
        return "%s%s%s%s".formatted(
                FieldDefinitions.FIELD_SEPARATOR,
                this.getName(),
                FieldDefinitions.FIELD_SEPARATOR,
                fields.toString()
        );
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public String getName() {
        return name;
    }

    public Field<?> getFieldByName(String fieldName){
        return getFields().getField(fieldName);
    }

    public StringField getStringField(String fieldName){
        return getFields().getStringField(fieldName);
    }

    public IntegerField getIntegerField(String fieldName){
        return getFields().getIntegerField(fieldName);
    }

    public DoubleField getDoubleField(String fieldName){
        return getFields().getDoubleField(fieldName);
    }

    public DateField getDateField(String fieldName){
        return getFields().getDateField(fieldName);
    }

    public void setFieldValue(String name, String value) {
        this.getStringField(name).setValue(value);
    }

    public void setFieldValue(String name, Double value) {
        this.getDoubleField(name).setValue(value);
    }

    public void setFieldValue(String name, int value) {
        this.getIntegerField(name).setValue(value);
    }

    public void setFieldValue(String name, Date date) {
        this.getDateField(name).setValue(date);
    }
}
