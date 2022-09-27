package SPEDFiscal;

public interface Unit {
    void count(Counter counter);
    int count();
    void validate();
    void write(Writer writer);
}
