package SPEDFiscalLib;

public interface Unit {
    void count(Counter counter);
    int count();
    void validate(ValidationListener validationListener);
    void write(Writer writer);
}
