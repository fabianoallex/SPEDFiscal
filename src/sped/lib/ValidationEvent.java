package sped.lib;

public class ValidationEvent {
    private final String message;
    private final Register register;

    ValidationEvent(String message, Register register) {
        this.message = message;
        this.register = register;
    }

    public String getMessage() {
        return this.message;
    }

    public Register getRegister() {
        return register;
    }
}