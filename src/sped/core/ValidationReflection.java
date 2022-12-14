package sped.core;

public final class ValidationReflection extends Validation {
    private final ValidationHelper validationHelper;

    ValidationReflection(ValidationHelper validationHelper, String name) {
        super(name);
        this.validationHelper = validationHelper;
    }

    public ValidationHelper getvalidationHelper() {
        return this.validationHelper;
    }

    public boolean validate(ValidationMessage validationMessage, String value, Register register) {
        return this.getvalidationHelper().validate(validationMessage, this.getName(), value, register);
    }

    @Override
    public void validate(Register register, Field<?> field, String value, ValidationListener validationListener) {
        ValidationMessage validationMessage = new ValidationMessage();

        if (!this.validate(validationMessage, value, register)) {
            String message = "[" + value + "]: " + validationMessage.getMessage();
            validationListener.onErrorMessage(
                    FieldValidationEvent.newBuilder()
                            .setField(field)
                            .setRegister(register)
                            .setMessage(message)
                            .build()
            );
        }
    }
}
