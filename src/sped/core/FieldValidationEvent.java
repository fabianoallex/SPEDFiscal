package sped.core;

public class FieldValidationEvent extends ValidationEvent {
    private final Field<?> field;
    private final Register register;

    public static Builder newBuilder() {
        return new Builder();
    }

    private FieldValidationEvent(Register register, Field<?> field, String message) {
        super(
                register.getName() + "." + field.getName() + ": \"" + message + "\".",
                register
        );
        this.register = register;
        this.field = field;
    }

    public Field<?> getField() {
        return field;
    }

    public Register getRegister() {
        return register;
    }

    public static class Builder {
        private Field<?> field;
        private Register register;
        private String message;

        private Builder() {}

        public Builder withField(Field<?> field) {
            this.field = field;
            return this;
        }

        public Builder withRegister(Register register) {
            this.register = register;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public FieldValidationEvent build() {
            return new FieldValidationEvent(register, field, message);
        }
    }
}
