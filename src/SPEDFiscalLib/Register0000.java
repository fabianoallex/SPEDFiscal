package SPEDFiscalLib;

public class Register0000 implements Unit {
    public static final String REGISTER_NAME = "0000";

    private final Register register;

    Register0000(Register register) {
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
