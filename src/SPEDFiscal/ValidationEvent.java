package SPEDFiscal;

public class ValidationEvent {
    private final String message;

    ValidationEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
