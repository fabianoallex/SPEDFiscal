package sped.lib;

public class Block9 extends Block {
    public static final String BLOCK_NAME = "9";
    public static final String OPENING_REGISTER_NAME = "9001";
    public static final String CLOSURE_REGISTER_NAME = "9990";

    Block9(Factory factory) {
        super(BLOCK_NAME, OPENING_REGISTER_NAME, CLOSURE_REGISTER_NAME, factory);
    }

    void addRegister9900(String regName, int regTotal) {
        Register9900 register9900 = this.getFactory().createRegister9900();
        register9900.getFieldRegisterName().setValue(regName);
        register9900.getFieldRegisterCount().setValue(regTotal);
        this.getRegisters().add(register9900.getRegister());
    }
}
