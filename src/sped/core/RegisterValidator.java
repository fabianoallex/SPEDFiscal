package sped.core;

public class RegisterValidator extends Validator {
    private final Register register;

    RegisterValidator(Register register, ValidationListener validationListener) {
        super(validationListener);
        this.register = register;
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public void validate() {
        register.getFields().forEach((key, field) -> validateField(field));
    }

    private void validateField(Field<?> field) {
        if (field == null)
            return;

        String formattedValue = FieldFormatter.formatField(field, register);

        if (!validateFieldRequired(field, formattedValue))
            return;

        register.getValidationsForField(field)
                .forEach(validation -> validation.validate(register, field, formattedValue, getValidationListener()));
    }

    private boolean validateFieldRequired(Field<?> field, String formattedValue) {
        if (field.getRequired().equals("O") && formattedValue.isEmpty()) {
            this.getValidationListener().onErrorMessage(
                    FieldValidationEvent.newBuilder()
                            .setField(field)
                            .setRegister(register)
                            .setMessage("Campo ogrigatório não informado")
                            .build()
            );

            return false;
        }

        return true;
    }
}
