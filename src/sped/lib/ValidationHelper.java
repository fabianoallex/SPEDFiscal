package sped.lib;

public interface ValidationHelper {
    boolean validate(ValidationMessage validationMessage, String methodName, String value, Register register);
}
