package SPEDFiscalLib;

import java.util.ArrayList;

public class Block implements Unit {
    private static final String OPENING_REGISTER_BLOCK_SUFFIX_NAME = "001";
    private static final String CLOSURE_REGISTER_BLOCK_SUFFIX_NAME = "990";
    private final ArrayList<Register> registers = new ArrayList<>();
    private final String name;
    private final OpeningRegister openingRegister;
    private final ClosureRegister closureRegister;
    private final Factory factory;

    Block(String name, Factory factory) {
        this.factory = factory;
        this.name = name;
        this.openingRegister = factory.createOpeningRegister(this.name + OPENING_REGISTER_BLOCK_SUFFIX_NAME);
        this.closureRegister = factory.createClosureRegister(this.name + CLOSURE_REGISTER_BLOCK_SUFFIX_NAME);
    }

    public Factory getFactory() {
        return factory;
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
        Register register = this.factory.createRegister(name);
        this.registers.add(register);
        return register;
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
        for (Register register : registers) c += register.count();
        return c;
    }

    @Override
    public void validate(ValidationListener validationListener) {
        openingRegister.validate(validationListener);
        for (Register register : registers ) register.validate(validationListener);
        closureRegister.validate(validationListener);
    }

    @Override
    public void write(Writer writer) {
        openingRegister.write(writer);
        for (Register register : registers) register.write(writer);
        closureRegister.write(writer);
    }
}

