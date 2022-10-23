package SPEDFiscal;

import java.util.ArrayList;

public class Block implements Unit {
    private static final String OPENING_REGISTER_BLOCK_SUFFIX_NAME = "001";
    private static final String CLOSURE_REGISTER_BLOCK_SUFFIX_NAME = "990";
    private final String name;
    private final OpeningRegister openingRegister;
    private final ClosureRegister closureRegister;
    private final ArrayList<Register> registers;
    SPEDDefinitions definitions;

    Block(String name, SPEDDefinitions definitions) {
        this.definitions = definitions;
        this.name = name;
        this.openingRegister = new OpeningRegister(name + OPENING_REGISTER_BLOCK_SUFFIX_NAME, definitions);
        this.closureRegister = new ClosureRegister(name + CLOSURE_REGISTER_BLOCK_SUFFIX_NAME, definitions);
        this.registers = new ArrayList<>();
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
        Register register = new Register(name, this.definitions);
        this.registers.add(register);
        register.getFields().getField("teste");
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
    public void write(Writer writer) {
        openingRegister.write(writer);
        for (Register register : registers) register.write(writer);
        closureRegister.write(writer);
    }
}

class OpeningRegister extends Register {
    public static final String FIELD_REGISTER_THERE_IS_MOV = "IND_MOV";
    public static final int FIELD_REGISTER_IS_THERE_MOV_YES = 0;
    public static final int FIELD_REGISTER_IS_THERE_MOV_NO = 1;
    private final Field<Integer> fieldRegisterIsThereMov;

    OpeningRegister(String name, SPEDDefinitions definitions) {
        super(name, definitions);
        try {
            fieldRegisterIsThereMov = this.getField(FIELD_REGISTER_THERE_IS_MOV);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setThereIsMov(boolean thereIs) {
        fieldRegisterIsThereMov.setValue((thereIs) ? FIELD_REGISTER_IS_THERE_MOV_YES : FIELD_REGISTER_IS_THERE_MOV_NO);
    }
}

class ClosureRegister extends Register {
    public static final String FIELD_REGISTER_COUNT = "QTD_LIN";
    private final Field<Integer> fieldRegisterCount;

    ClosureRegister(String name, SPEDDefinitions definitions) {
        super(name, definitions);
        try {
            fieldRegisterCount = this.getField(FIELD_REGISTER_COUNT);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Integer> getFieldRegisterCount() {
        return fieldRegisterCount;
    }
}
