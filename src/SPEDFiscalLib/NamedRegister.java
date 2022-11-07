package SPEDFiscalLib;

abstract public class NamedRegister implements Unit {
    private final String name;
    private final Register register;

    NamedRegister(String name, Register register) {
        this.name = name;
        this.register = register;
    }

    public Register getRegister() {
        return register;
    }

    public String getName(){
        return this.name;
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
