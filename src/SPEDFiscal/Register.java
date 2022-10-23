package SPEDFiscal;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

class FieldNotFoundException extends Exception {
    public FieldNotFoundException(String fieldClassName, String fieldName, String registerName) {
        super(String.format("%s '%s' não encontrado no Registro '%s'.", fieldClassName, fieldName, registerName));
    }
}

public class Register implements Unit {
    private final String name;
    private final ArrayList<Register> registers;
    private final Fields fields;
    private final String referenceKey;
    SPEDConfig config;

    Register(String name, SPEDConfig config){
        this.name = name;
        this.config = config;
        registers = new ArrayList<>();
        this.fields = config.getFieldsCreator().create(this.name);

        RegisterDefinitions registerDefinitions = DefinitionsLoader.getRegisterDefinitions(name, config.getDefinitionsXmlPath());
        this.referenceKey = registerDefinitions.key;
    }

    public <T> T getID() throws FieldNotFoundException {
        if (this.referenceKey == null) return null;

        Field<?> field = this.getField(this.referenceKey);

        if (field == null) throw new FieldNotFoundException(Field.class.getSimpleName(), this.referenceKey, this.getName());

        return (T) field.getValue();
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
        for (Unit unit : registers) c += unit.count();
        return c;
    }

    @Override
    public void validate() {
        //todo: implementar rotinas de validação dos registros
    }

    @Override
    public void write(Writer writer) {
        writer.write(this.toString());
        for (Register register : registers) register.write(writer);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilderFields = new StringBuilder();
        FieldFormatter fieldFormatter = new FieldFormatter();

        for (Map.Entry<String, Field<?>> e : fields.entrySet()) {
            Field<?> field = e.getValue();

            try {
                String fieldFormatName = this.getName() + "." + field.getName();
                FieldFormat fieldFormat = DefinitionsLoader.getFieldFormat(fieldFormatName);
                String formattedField = fieldFormatter.formatField(field, fieldFormat);

                stringBuilderFields
                        .append(formattedField)
                        .append(FieldDefinitions.FIELD_SEPARATOR)
                ;
            } catch (FieldNotFoundException ex) {
                throw new RuntimeException(ex);
            }
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
}


