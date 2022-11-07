package SPEDFiscalLib;

public interface ValidationHelper {
    boolean validate(ValidationMessage validationMessage, String methodName, String value, Register register);
}
