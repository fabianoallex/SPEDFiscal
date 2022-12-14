package sped.core;

public final class ReflectionValidation extends Validation {
    private final ValidationHelper validationHelper;

    public static Builder newBuilder() {
        return new Builder();
    }

    private ReflectionValidation(ValidationHelper validationHelper, String name) {
        super(name);
        this.validationHelper = validationHelper;
    }

    public ValidationHelper getvalidationHelper() {
        return this.validationHelper;
    }

    private boolean validate(ValidationMessage validationMessage, String value, Register register) {
        return this.getvalidationHelper()
                .validate(validationMessage, this.getName(), value, register);
    }

    @Override
    public void validate(Register register, Field<?> field, String value, ValidationListener validationListener) {
        ValidationMessage validationMessage = new ValidationMessage();

        if (!this.validate(validationMessage, value, register)) {
            validationListener.onErrorMessage(
                    FieldValidationEvent.newBuilder()
                            .setField(field)
                            .setRegister(register)
                            .setMessage("[%s]: %s".formatted(value, validationMessage.getMessage()))
                            .build()
            );
        }
    }

    public static class Builder {
        private ValidationHelper validationHelper;
        private String name;

        private Builder() {}
        public Builder setValidationHelper(ValidationHelper validationHelper) {
            this.validationHelper = validationHelper;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public ReflectionValidation build() {
            return new ReflectionValidation(validationHelper, name);
        }
    }
}
