package sped.core;

public abstract class Validator {
    private final ValidationListener validationListener;

    Validator(ValidationListener validatorListener) {
        this.validationListener = validatorListener;
    }

    public ValidationListener getValidationListener() {
        return validationListener;
    }

    public abstract void validate();
}
