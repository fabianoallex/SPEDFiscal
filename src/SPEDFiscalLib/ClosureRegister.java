package SPEDFiscalLib;

public class ClosureRegister extends NamedRegister {
    public static final String FIELD_REGISTER_COUNT = "QTD_LIN";
    private final Field<Integer> fieldRegisterCount;

    ClosureRegister(Register register) {
        super(register.getName(), register);
        try {
            fieldRegisterCount = this.getRegister().getField(FIELD_REGISTER_COUNT);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Integer> getFieldRegisterCount() {
        return fieldRegisterCount;
    }
}
