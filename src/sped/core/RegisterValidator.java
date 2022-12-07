package sped.core;

public class RegisterValidator extends Validator {
    private final Register register;
    private final FieldValidator fieldValidator;

    RegisterValidator(Register register, ValidationListener validationListener) {
        super(validationListener);
        this.register = register;
        this.fieldValidator = new FieldValidator(register, validationListener);
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public void validate() {
        register.getFields().forEach((key, field) -> fieldValidator.validate(field));
    }
}
