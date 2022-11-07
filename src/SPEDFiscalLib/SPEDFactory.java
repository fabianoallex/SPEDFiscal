package SPEDFiscalLib;

import java.util.Date;

public class SPEDFactory {
    private final Definitions definitions;

    public SPEDFactory(Definitions definitions) {
        this.definitions = definitions;
    }

    public Definitions getDefinitions() {
        return definitions;
    }

    public SPEDGenerator createSPEDGenerator() {
        return new SPEDGenerator(this);
    }

    public Register createRegister(String name) {
        return new Register(name, this);
    }

    public Register0000 createRegister0000() {
        return new Register0000(this.createRegister(Register0000.REGISTER_NAME));
    }

    public Register9900 createRegister9900() {
        return new Register9900(this.createRegister(Register9900.REGISTER_NAME));
    }

    public Register9999 createRegister9999() {
        return new Register9999(this.createRegister(Register9999.REGISTER_NAME));
    }

    public OpeningRegister createOpeningRegister(String name) {
        return new OpeningRegister(this.createRegister(name));
    }

    public ClosureRegister createClosureRegister(String name) {
        return new ClosureRegister(this.createRegister(name));
    }

    public Fields createFields(String registerName) {
        Fields fields = new Fields();

        for (FieldDefinitions fieldDefinitions : this.definitions.getFieldsDefinitions(registerName)) {
            String fieldName = fieldDefinitions.name;
            String type = fieldDefinitions.type;
            String size = fieldDefinitions.size;
            String dec = fieldDefinitions.dec;
            String format = fieldDefinitions.format;
            String ref = fieldDefinitions.ref;

            if (type.equals(Definitions.FIELDS_REG_TYPE_STRING)) fields.addField(new Field<String>(fieldName));
            if (type.equals(Definitions.FIELDS_REG_TYPE_DATE)) fields.addField(new Field<Date>(fieldName));

            if (type.equals(Definitions.FIELDS_REG_TYPE_NUMBER))
                fields.addField(dec.isEmpty() ?
                        new Field<Integer>(fieldName) :
                        new Field<Double>(fieldName));
        }

        return fields;
    }
}
