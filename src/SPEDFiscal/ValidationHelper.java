package SPEDFiscal;

public interface ValidationHelper {
    public String[] getValidationNames();
    public boolean validate(String validationName, String value, Register register);
}
