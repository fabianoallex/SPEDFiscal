package SPEDFiscalLib;

public class Register9900 implements Unit {
    public static final String REGISTER_NAME = "9900";
    public static final String FIELD_REGISTER_NAME = "REG_BLC";
    public static final String FIELD_REGISTER_COUNT = "QTD_REG_BLC";
    private final Field<String> fieldRegisterName;
    private final Field<Integer> fieldRegisterCount;

    private final Register register;

    Register9900(Register register) {
        this.register = register;
        try {
            fieldRegisterName = this.register.getField(FIELD_REGISTER_NAME);
            fieldRegisterCount = this.register.getField(FIELD_REGISTER_COUNT);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Register getRegister() {
        return register;
    }
/*
    Register9900(Definitions definitions) {
        super(REGISTER_NAME, definitions);
        try {
            fieldRegisterName = (Field<String>) this.getField(FIELD_REGISTER_NAME);
            fieldRegisterCount = (Field<Integer>) this.getField(FIELD_REGISTER_COUNT);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

 */

    public Field<String> getFieldRegisterName() {
        return fieldRegisterName;
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
