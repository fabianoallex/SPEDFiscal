package SPEDFiscalLib;

public class FieldValidationEvent extends ValidationEvent {
    private final Field<?> field;
    private final Register register;

    FieldValidationEvent(Register register, Field<?> field, String message) {
        super(register.getName() + "." + field.getName() + ": \"" + message + "\".", register);
        this.register = register;
        this.field = field;
    }

    public Field<?> getField() {
        return field;
    }

    public Register getRegister() {
        return register;
    }
}
