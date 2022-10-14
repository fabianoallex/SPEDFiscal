package SPEDFiscal;

import java.util.ArrayList;
import java.util.Date;

class FieldNotFoundException extends Exception {
    public FieldNotFoundException(String fieldClassName, String fieldName, String registerName) {
        super(String.format("%s '%s' não encontrado no Registro '%s'.", fieldClassName, fieldName, registerName));
    }
}

public class Register implements Unit {
    private final String name;
    private final ArrayList<Register> registers;
    private final Fields fields;
    private final boolean isReference;
    private final String referenceKey;
    SPEDConfig config;

    Register(String name, SPEDConfig config){
        this.name = name;
        this.config = config;
        registers = new ArrayList<>();
        this.fields = this.createFields();

        RegisterDefinitions registerDefinitions = DefinitionsLoader.getRegisterDefinitions(name, config.definitionsXmlPath);
        this.isReference = !(registerDefinitions.key == null || registerDefinitions.key.isEmpty());
        this.referenceKey = registerDefinitions.key;
    }

    public String getID(){
        return (this.referenceKey == null) ? null : this.getField(this.referenceKey).getValue().toString();
    }

    public boolean isReference() {
        return isReference;
    }

    public Fields getFields() {
        return fields;
    }

    public Register addRegister(String name){
        Register register = new Register(name, this.config);
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

        for (Unit unit : registers)
            c += unit.count();

        return c;
    }

    @Override
    public void validate() {
        //todo: implementar rotinas de validação dos registros
    }

    @Override
    public void write(Writer writer) {
        writer.write(this.toString());

        for (Register register : registers)
            register.write(writer);
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

    public String getName() {
        return name;
    }

    public Field<?> getField(String fieldName){
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

    public void setFieldValue(String name, String value) throws FieldNotFoundException {
        StringField stringField = this.getStringField(name);

        if (stringField == null)
            throw new FieldNotFoundException(StringField.class.getSimpleName(), name, this.getName());

        stringField.setValue(value);
    }

    public void setFieldValue(String name, Double value) throws FieldNotFoundException {
        DoubleField doubleField = this.getDoubleField(name);

        if (doubleField == null)
            throw new FieldNotFoundException(DoubleField.class.getSimpleName(), name, this.getName());

        doubleField.setValue(value);
    }

    public void setFieldValue(String name, int value) throws FieldNotFoundException {
        IntegerField integerField = this.getIntegerField(name);

        if (integerField == null)
            throw new FieldNotFoundException(IntegerField.class.getSimpleName(), name, this.getName());

        integerField.setValue(value);
    }

    public void setFieldValue(String name, Date date) throws FieldNotFoundException {
        DateField dateField = this.getDateField(name);

        if (dateField == null)
            throw new FieldNotFoundException(DateField.class.getSimpleName(), name, this.getName());

        dateField.setValue(date);
    }

    public Fields createFields() {
        Fields fields = new Fields(this.name);

        for (FieldDefinitions fieldDefinitions : DefinitionsLoader.getFieldsDefinitions(this.name, this.config.getDefinitionsXmlPath())) {
            String fieldName = fieldDefinitions.name;
            String type = fieldDefinitions.type;
            String size = fieldDefinitions.size;
            String dec = fieldDefinitions.dec;
            String format = fieldDefinitions.format;
            String ref = fieldDefinitions.ref;

            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_STRING))
                fields.addField(new StringField(fieldName));

            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_DATE))
                fields.addField(new DateField(fieldName));

            if (type.equals(DefinitionsLoader.FIELDS_REG_TYPE_NUMBER))
                fields.addField(dec.isEmpty() ? new IntegerField(fieldName) : new DoubleField(fieldName));
        }

        return fields;
    }
}
