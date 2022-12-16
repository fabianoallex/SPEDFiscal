package sped.core;

public class OpeningRegister extends NamedRegister {
    public static final String FIELD_REGISTER_THERE_IS_MOV = "IND_MOV";
    public static final int FIELD_REGISTER_IS_THERE_MOV_YES = 0;
    public static final int FIELD_REGISTER_IS_THERE_MOV_NO = 1;
    private final Field<Integer> fieldRegisterIsThereMov;

    OpeningRegister(Context context, String name) {
        super(context, name);
        try {
            fieldRegisterIsThereMov = this.getRegister().getField(FIELD_REGISTER_THERE_IS_MOV);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setThereIsMov(boolean thereIs) {
        fieldRegisterIsThereMov.setValue((thereIs) ? FIELD_REGISTER_IS_THERE_MOV_YES : FIELD_REGISTER_IS_THERE_MOV_NO);
    }

    public static OpeningRegister create(Context context, String name){
        return new OpeningRegister(context, name);
    }
}
