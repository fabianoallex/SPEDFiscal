package SPEDFiscalLib;

public class OpeningRegister implements Unit {
    public static final String FIELD_REGISTER_THERE_IS_MOV = "IND_MOV";
    public static final int FIELD_REGISTER_IS_THERE_MOV_YES = 0;
    public static final int FIELD_REGISTER_IS_THERE_MOV_NO = 1;
    private final Field<Integer> fieldRegisterIsThereMov;
    private final Register register;

    OpeningRegister(Register register) {
        this.register = register;
        try {
            fieldRegisterIsThereMov = this.register.getField(FIELD_REGISTER_THERE_IS_MOV);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setThereIsMov(boolean thereIs) {
        fieldRegisterIsThereMov.setValue((thereIs) ? FIELD_REGISTER_IS_THERE_MOV_YES : FIELD_REGISTER_IS_THERE_MOV_NO);
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
