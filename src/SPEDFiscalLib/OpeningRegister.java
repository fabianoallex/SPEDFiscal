package SPEDFiscalLib;

public class OpeningRegister extends NamedRegister {
    public static final String FIELD_REGISTER_THERE_IS_MOV = "IND_MOV";
    public static final int FIELD_REGISTER_IS_THERE_MOV_YES = 0;
    public static final int FIELD_REGISTER_IS_THERE_MOV_NO = 1;
    private final Field<Integer> fieldRegisterIsThereMov;

    OpeningRegister(Factory factory, String name) {
        super(factory, name);
        try {
            fieldRegisterIsThereMov = this.getRegister().getField(FIELD_REGISTER_THERE_IS_MOV);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setThereIsMov(boolean thereIs) {
        fieldRegisterIsThereMov.setValue((thereIs) ? FIELD_REGISTER_IS_THERE_MOV_YES : FIELD_REGISTER_IS_THERE_MOV_NO);
    }
}
