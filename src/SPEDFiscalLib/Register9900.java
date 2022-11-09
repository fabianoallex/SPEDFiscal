package SPEDFiscalLib;

public class Register9900 extends NamedRegister {
    public static final String REGISTER_NAME = "9900";
    public static final String FIELD_REGISTER_NAME = "REG_BLC";
    public static final String FIELD_REGISTER_COUNT = "QTD_REG_BLC";
    private final Field<String> fieldRegisterName;
    private final Field<Integer> fieldRegisterCount;

    Register9900(SPEDFactory factory) {
        super(factory, REGISTER_NAME);
        try {
            fieldRegisterName = this.getRegister().getField(FIELD_REGISTER_NAME);
            fieldRegisterCount = this.getRegister().getField(FIELD_REGISTER_COUNT);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<String> getFieldRegisterName() {
        return fieldRegisterName;
    }

    public Field<Integer> getFieldRegisterCount() {
        return fieldRegisterCount;
    }
}
