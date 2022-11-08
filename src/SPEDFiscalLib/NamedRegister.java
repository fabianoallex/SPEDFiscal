package SPEDFiscalLib;

abstract public class NamedRegister implements Unit {
    private final Register register;

    NamedRegister(Register register) {
        this.register = register;
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public void count(Counter counter) {
        this.register.count(counter);
    }

    @Override
    public int count() {
        return this.register.count();
    }

    @Override
    public void validate(ValidationListener validationListener) {
        this.register.validate(validationListener);
    }

    @Override
    public void write(Writer writer) {
        this.register.write(writer);
    }
}
