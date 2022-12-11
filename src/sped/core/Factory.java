package sped.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class Factory {
    private final Definitions definitions;

    public Factory(Definitions definitions) {
        this.definitions = definitions;
    }

    public Definitions getDefinitions() {
        return definitions;
    }

    public SpedGenerator createSPEDGenerator() {
        return new SpedGenerator(this);
    }

    public Register createRegister(String name) {
        return new Register(name, this);
    }

    public Register0000 createRegister0000() {
        return new Register0000(this);
    }

    public Register9900 createRegister9900() {
        return new Register9900(this);
    }

    public Register9999 createRegister9999() {
        return new Register9999(this);
    }

    public OpeningRegister createOpeningRegister(String name) {
        return new OpeningRegister(this, name);
    }

    public ClosureRegister createClosureRegister(String name) {
        return new ClosureRegister(this, name);
    }

    public Fields createFields(String registerName) {
        Fields fields = new Fields();

        for (FieldDefinitions fieldDefinitions : this.definitions.getFieldsDefinitions(registerName)) {
            String fieldName = fieldDefinitions.name;
            String type = fieldDefinitions.type;
            String dec = fieldDefinitions.dec;
            String required = fieldDefinitions.required;

            if (type.equals(Definitions.FIELDS_REG_TYPE_STRING)) fields.addField(new Field<String>(fieldName, required));
            if (type.equals(Definitions.FIELDS_REG_TYPE_DATE)) fields.addField(new Field<Date>(fieldName, required));
            if (type.equals(Definitions.FIELDS_REG_TYPE_NUMBER))
                fields.addField(dec.isEmpty() ?
                        new Field<Integer>(fieldName, required) :
                        new Field<Double>(fieldName, required));
        }

        return fields;
    }

    public NamedRegister createNamedRegister(Class<? extends NamedRegister> clazz) {
        try {
            return clazz.getConstructor(Factory.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Block createBlock(Class<? extends Block> clazz) {
        try {
            return clazz.getConstructor(Factory.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public GeneratorBase createGenerator(Class<? extends GeneratorBase> clazz) {
        try {
            return clazz.getConstructor(Factory.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
