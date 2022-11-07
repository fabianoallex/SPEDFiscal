package SPEDFiscalLib;

public class ClosureRegister implements Unit {
    public static final String FIELD_REGISTER_COUNT = "QTD_LIN";
    private final Field<Integer> fieldRegisterCount;
    private Register register;

    ClosureRegister(Register register) {
        this.register = register;
        try {
            fieldRegisterCount = this.register.getField(FIELD_REGISTER_COUNT);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Integer> getFieldRegisterCount() {
        return fieldRegisterCount;
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
