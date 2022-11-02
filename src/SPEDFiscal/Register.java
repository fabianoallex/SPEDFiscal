package SPEDFiscal;

import java.util.ArrayList;
import java.util.Map;

class FieldNotFoundException extends Exception {
    public FieldNotFoundException(String fieldClassName, String fieldName, String registerName) {
        super(String.format("%s '%s' não encontrado no Registro '%s'.", fieldClassName, fieldName, registerName));
    }
}

public class Register implements Unit {
    private final String name;
    private final ArrayList<Register> registers = new ArrayList<>();
    private final Fields fields;
    private final String referenceKey;
    private final SPEDDefinitions definitions;

    Register(String name, SPEDDefinitions definitions){
        this.name = name;
        this.definitions = definitions;
        this.fields = definitions.getFieldsCreator().create(this.name);
        this.referenceKey = definitions.getRegisterDefinitions(this.name).key;
    }

    public <T> T getID() {
        if (this.referenceKey == null)
            return null;

        Field<?> field;
        try {
            field = this.getField(this.referenceKey);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }

        return (T) field.getValue();
    }

    public Fields getFields() {
        return fields;
    }

    public Register addRegister(String name){
        Register register = new Register(name, this.definitions);
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
        for (Unit unit : registers) c += unit.count();
        return c;
    }

    @Override
    public void validate(ValidationListener validationListener) {
        new RegisterValidator(this, validationListener).validate();

        for (Register register : registers)
            register.validate(validationListener);
    }

    @Override
    public void write(Writer writer) {
        writer.write(this.toString());
        for (Register register : registers) register.write(writer);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilderFields = new StringBuilder();

        for (Map.Entry<String, Field<?>> e : fields.entrySet()) {
            Field<?> field = e.getValue();

            String fieldFormatName = this.getName() + "." + field.getName();
            String formattedField = this.definitions.formatField(field, fieldFormatName);

            stringBuilderFields
                    .append(formattedField)
                    .append(FieldDefinitions.FIELD_SEPARATOR);
        }

        return "%s%s%s%s".formatted(
                FieldDefinitions.FIELD_SEPARATOR,
                this.getName(),
                FieldDefinitions.FIELD_SEPARATOR,
                stringBuilderFields.toString()
        );
    }

    public String getName() {
        return name;
    }

    public <T extends Field<?>> T getField(String fieldName) throws FieldNotFoundException {
        Field<?> field = this.getFields().getField(fieldName);
        if (field == null) throw new FieldNotFoundException(Field.class.getSimpleName(), fieldName, this.getName());

        return (T) field;
    }

    public <T> void setFieldValue(String fieldName, T value) throws FieldNotFoundException {
        Field<T> field = this.getField(fieldName);
        field.setValue(value);
    }

    public <T> T getFieldValue(String fieldName) throws FieldNotFoundException {
        Field<T> field = this.getField(fieldName);
        return field.getValue();
    }

    protected SPEDDefinitions getDefinitions() {
        return this.definitions;
    }
}


