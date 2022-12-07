package sped.core;

import java.util.Date;

abstract public class NamedRegister implements Unit {
    private final Register register;

    public NamedRegister(Register register){
        this.register = register;
    }

    public NamedRegister(Factory factory, String name) {
        this.register = factory.createRegister(name);
    }

    public NamedRegister addNamedRegister(Class<? extends NamedRegister> clazz) {
        return this.register.addNamedRegister(clazz);
    }

    public Register addRegister(String name){
        return this.register.addRegister(name);
    }

    public Register getRegister() {
        return register;
    }

    public Field<String> getStringField(String fieldName) {
        try {
            return this.getRegister().getField(fieldName);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Date> getDateField(String fieldName) {
        try {
            return this.getRegister().getField(fieldName);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Integer> getIntegerField(String fieldName) {
        try {
            return this.getRegister().getField(fieldName);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Double> getDoubleField(String fieldName) {
        try {
            return this.getRegister().getField(fieldName);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return this.getRegister().toString();
    }

    @Override
    public void count(Counter counter) {
        this.register.count(counter);
    }

    @Override
    public int count() {
        return this.register.count();
    }

    @Override
    public void validate(ValidationListener validationListener) {
        this.register.validate(validationListener);
    }

    @Override
    public void write(Writer writer) {
        this.register.write(writer);
    }
}
