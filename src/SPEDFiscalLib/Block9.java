package SPEDFiscalLib;

public class Block9 extends Block {
    public static final String BLOCK_NAME = "9";

    Block9(SPEDFactory factory) {
        super(BLOCK_NAME, factory);
    }

    void addRegister9900(String regName, int regTotal) {
        Register9900 register9900 = this.getFactory().createRegister9900();
        register9900.getFieldRegisterName().setValue(regName);
        register9900.getFieldRegisterCount().setValue(regTotal);
        this.getRegisters().add(register9900.getRegister());
    }
}
