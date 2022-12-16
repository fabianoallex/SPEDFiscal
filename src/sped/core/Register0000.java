package sped.core;

public class Register0000 extends NamedRegister {
    public static final String REGISTER_NAME = "0000";

    Register0000(Context context) {
        super(context, REGISTER_NAME);
    }

    public static Register0000 create(Context context){
        return new Register0000(context);
    }
}
