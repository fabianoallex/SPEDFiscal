package SPEDFiscalLib;

public class Register9999 implements Unit {
    public static final String REGISTER_NAME = "9999";
    public static final String FIELD_REGISTER_COUNT_NAME = "QTD_LIN";
    private final Field<Integer> fieldRegisterCount;
    private final Register register;

    Register9999(Register register) {
        this.register = register;
        try {
            fieldRegisterCount = this.register.getField(FIELD_REGISTER_COUNT_NAME);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Register getRegister() {
        return register;
    }

    /*
    Register9999(Definitions definitions) {
        super(REGISTER_NAME, definitions);
        try {
            fieldRegisterCount = (Field<Integer>) this.getField(FIELD_REGISTER_COUNT_NAME);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

     */

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
