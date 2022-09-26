package SPEDFiscal;

import java.util.ArrayList;

public class Block implements Unit {
    private static final String OPENING_REGISTER_BLOCK_SUFFIX_NAME = "001";
    private static final String CLOSURE_REGISTER_BLOCK_SUFFIX_NAME = "990";
    Writer writer;
    private final String name;
    private final OpeningRegister openingRegister;
    private final ClosureRegister closureRegister;
    private final ArrayList<Register> registers;

    Block(String name, Writer writer){
        this.openingRegister = new OpeningRegister(name + OPENING_REGISTER_BLOCK_SUFFIX_NAME, writer);
        this.closureRegister = new ClosureRegister(name + CLOSURE_REGISTER_BLOCK_SUFFIX_NAME, writer);
        this.name = name;
        this.registers = new ArrayList<>();
        this.writer = writer;
    }

    public ClosureRegister getClosureRegister() {
        return closureRegister;
    }

    public ArrayList<Register> getRegisters() {
        return registers;
    }

    public String getName() {
        return name;
    }

    public Register addRegister(String name){
        Register register = new Register(name, writer);
        this.registers.add(register);
        return register;
    }

    Register getRegister(String name){
        for (int i = 0; i< registers.size()-1; i++){
            Register register = registers.get(i);
            if (register.getName().equals(name)){
                return register;
            }
        }

        return null;
    }

    void totalize(int initCount) {
        closureRegister.getFieldRegisterCount().setValue(this.count() + initCount);
        openingRegister.setThereIsMov((closureRegister.getFieldRegisterCount().getValue() > 2 ));
    }

    @Override
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public Writer getWriter() {
        return this.writer;
    }

    @Override
    public void count(Counter counter) {
        openingRegister.count(counter);
        closureRegister.count(counter);
        registers.forEach((register -> register.count(counter)));
    }

    @Override
    public int count() {
        int c = openingRegister.count() + closureRegister.count();
        for (Register register : registers) {
            c += register.count();
        }
        return c;
    }

    @Override
    public void validate() {

    }

    @Override
    public void write() {
        openingRegister.write();
        for (Register register : registers) register.write();
        closureRegister.write();
    }
}

class OpeningRegister extends Register {
    public static final String FIELD_REGISTER_THERE_IS_MOV = "IND_MOV";
    public static final int FIELD_REGISTER_IS_THERE_MOV_YES = 0;
    public static final int FIELD_REGISTER_IS_THERE_MOV_NO = 1;
    private final IntegerField fieldRegisterIsThereMov;

    OpeningRegister(String name, Writer writer) {
        super(name, writer);
        fieldRegisterIsThereMov = this.getIntegerField(FIELD_REGISTER_THERE_IS_MOV);
    }

    public void setThereIsMov(boolean thereIs) {
        fieldRegisterIsThereMov.setValue((thereIs) ? FIELD_REGISTER_IS_THERE_MOV_YES : FIELD_REGISTER_IS_THERE_MOV_NO);
    }
}

class ClosureRegister extends Register {
    public static final String FIELD_REGISTER_COUNT = "QTD_LIN";
    private final IntegerField fieldRegisterCount;

    ClosureRegister(String name, Writer writer) {
        super(name, writer);
        fieldRegisterCount = this.getIntegerField(FIELD_REGISTER_COUNT);
    }

    public IntegerField getFieldRegisterCount() {
        return fieldRegisterCount;
    }
}
