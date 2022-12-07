package sped.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final public class Field<T> {
    private final String name;
    private T value;
    private String required;

    private Field(String name) {
        this.name = name;
        this.value = null;
    }

    Field(String name, String required) {
        this.name = name;
        this.value = null;
        this.required = required;
    }

    Field() {
        this.name = "";
        this.value = null;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return value;
    }

    public String getValueAsString() {
        if (getValue() instanceof String valueAsString) {
            return valueAsString.isEmpty() ? "" : valueAsString;
        }

        if (getValue() instanceof Integer valueAsInteger) {
            return valueAsInteger.toString();
        }

        if (getValue() instanceof Double valueAsDouble) {
            return valueAsDouble.toString();
        }

        if (getValue() instanceof Date valueAsDate) {
            return new SimpleDateFormat("dd/MM/yyyy").format(valueAsDate);
        }

        if (getValue() instanceof Register valueAsRegister) {
            Field<?> tempField = new Field<>();
            tempField.setValue(valueAsRegister.getID());

            return tempField.getValueAsString(); //recursive
        }

        return (getValue() != null ? (String) getValue() : "");
    }

    public void setValueFromString(String value) throws ParseException {
        if (getValue() instanceof String) {
            Field<String> stringField = (Field<String>) this;
            stringField.setValue(value.isEmpty() ? null : value);
            return;
        }

        if (getValue() instanceof Integer) {
            Field<Integer> integerField = (Field<Integer>) this;
            integerField.setValue(value.isEmpty() ? null : Integer.parseInt(value));
            return;
        }

        if (getValue() instanceof Double) {
            Field<Double> doubleField = (Field<Double>) this;
            doubleField.setValue(value.isEmpty() ? null : Double.parseDouble(value));
            return;
        }

        if (getValue() instanceof Date) {
            Field<Date> dateField = (Field<Date>) this;
            dateField.setValue(value.isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(value));
            return;
        }

        this.setValue((T) value);
    }
}
