package sped.core;

public class FieldValidationEvent extends ValidationEvent {
    private final Field<?> field;
    private final Register register;

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

        public Builder setField(Field<?> field) {
            this.field = field;
            return this;
        }

        public Builder setRegister(Register register) {
            this.register = register;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public FieldValidationEvent build() {
            return new FieldValidationEvent(register, field, message);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
