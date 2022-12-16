package sped.core;

public class ClosureRegister extends NamedRegister {
    public static final String FIELD_REGISTER_COUNT = "QTD_LIN";
    private final Field<Integer> fieldRegisterCount;

    ClosureRegister(Context context, String name) {
        super(context, name);
        try {
            fieldRegisterCount = this.getRegister().getField(FIELD_REGISTER_COUNT);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Integer> getFieldRegisterCount() {
        return fieldRegisterCount;
    }

    public static ClosureRegister create(Context context, String name){
        return new ClosureRegister(context, name);
    }
}
