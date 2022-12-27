package sped.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

final public class Register implements Unit {
    private final String name;
    private final ArrayList<Register> registers = new ArrayList<>();
    private final Fields fields;
    private final String referenceKey;
    private final Context context;

    Register(String name, Context context) {
        this.name = name;
        this.context = context;
        this.fields =  Fields.create(context, name);
        this.referenceKey = this.context.getDefinitions().getRegisterDefinitions(this.name).key;
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

    public List<Validation> getValidationsForField(Field<?> field) {
        return this.context.getDefinitions().getValidationsForField(this.name, field.getName());
    }

    public Register addRegister(String name){
        Register register = Register.create(name, context);
        this.registers.add(register);
        return register;
    }

    public NamedRegister addNamedRegister(Class<? extends NamedRegister> clazz) {
        NamedRegister namedRegister = NamedRegister.create(context, clazz);
        this.registers.add(namedRegister.getRegister());
        return namedRegister;
    }

    @Override
    public void count(Counter counter) {
        counter.increment(this.name);
        registers.forEach((unit -> unit.count(counter)));
    }

    @Override
    public int count() {
        return registers.stream()
                .map(Register::count)
                .mapToInt(value -> value)
                .sum() + 1;
    }

    @Override
    public void validate(ValidationListener validationListener) {
        new RegisterValidator(this, validationListener).validate();
        registers.forEach(register -> register.validate(validationListener));
    }

    @Override
    public void write(Writer writer) {
        writer.write(this.toString(), this);
        registers.forEach(register -> register.write(writer));
    }

    @Override
    public String toString() {
        final String collect = fields.values()
                .stream()
                .map(field -> FieldFormatter.formatField(field, this))
                .collect(Collectors.joining(this.context.getDefinitions().getFieldsSeparator()));

        return "%s%s%s%s%s".formatted(
                this.context.getDefinitions().getBeginEndSeparator(),
                this.getName(),
                this.context.getDefinitions().getFieldsSeparator(),
                collect,
                this.context.getDefinitions().getBeginEndSeparator()
        );
    }

    public String getName() {
        return name;
    }

    public <T extends Field<?>> T getField(String fieldName) throws FieldNotFoundException {
        Field<?> field = this.getFields().getField(fieldName);

        if (field == null)
            throw new FieldNotFoundException(Field.class.getSimpleName(), fieldName, this.getName());

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

    public Field<String> getStringField(String fieldName) {
        try {
            return this.getField(fieldName);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Date> getDateField(String fieldName) {
        try {
            return this.getField(fieldName);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Integer> getIntegerField(String fieldName) {
        try {
            return this.getField(fieldName);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Double> getDoubleField(String fieldName) {
        try {
            return this.getField(fieldName);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Context getContext() {
        return this.context;
    }

    public static Register create(String name, Context context){
        return new Register(name, context);
    }
}


