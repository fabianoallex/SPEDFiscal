package SPEDFiscalLib;

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

    public void setValueFromString(String value) throws ParseException {
        if (getValue() instanceof String) {
            Field<String> stringField = (Field<String>) this;
            stringField.setValue(value);
            return;
        }

        if (getValue() instanceof Integer) {
            Field<Integer> integerField = (Field<Integer>) this;
            integerField.setValue(Integer.parseInt(value));
            return;
        }

        if (getValue() instanceof Double) {
            Field<Double> doubleField = (Field<Double>) this;
            doubleField.setValue(Double.parseDouble(value));
            return;
        }

        if (getValue() instanceof Date) {
            Field<Date> dateField = (Field<Date>) this;
            dateField.setValue(new SimpleDateFormat("dd/MM/yyyy").parse(value));
            return;
        }

        this.setValue((T) value);
    }
}
