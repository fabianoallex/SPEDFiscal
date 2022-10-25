package SPEDFiscal;

public interface ValidationListener {
    void onSuccessMessage(ValidationEvent event);
    void onWarningMessage(ValidationEvent event);
    void onErrorMessage(ValidationEvent event);
}
