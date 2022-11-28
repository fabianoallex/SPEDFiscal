package sped.lib;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Block implements Unit {
    private final ArrayList<Register> registers = new ArrayList<>();
    private final String name;
    private final OpeningRegister openingRegister;
    private final ClosureRegister closureRegister;
    private final Factory factory;

    Block(String blockName, String openingRegisterName, String closureRegisterName, Factory factory) {
        this.factory = factory;
        this.name = blockName;

        OpeningRegister tempOpeningRegister = null;
        ClosureRegister tempClosureRegister = null;

        if (!openingRegisterName.isEmpty())
            tempOpeningRegister = factory.createOpeningRegister(openingRegisterName);

        if (!closureRegisterName.isEmpty())
            tempClosureRegister = factory.createClosureRegister(closureRegisterName);

        this.openingRegister = tempOpeningRegister;
        this.closureRegister = tempClosureRegister;
    }

    Block(String blockName, Factory factory) {
        this(blockName, "", "", factory);
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

    public NamedRegister addNamedRegister(Class<? extends NamedRegister> clazz) {
        NamedRegister namedRegister = this.factory.createNamedRegister(clazz);
        this.registers.add(namedRegister.getRegister());
        return namedRegister;
    }

    void totalize(int initCount) {
        AtomicInteger count = new AtomicInteger();

        Optional.ofNullable(closureRegister).ifPresent(cRegister -> {
            cRegister.getFieldRegisterCount().setValue(this.count() + initCount);
            count.set(cRegister.getFieldRegisterCount().getValue());
        });

        Optional.ofNullable(openingRegister).ifPresent(oRegister -> oRegister.setThereIsMov((count.get() > 2)));
    }

    @Override
    public void count(Counter counter) {
        Optional.ofNullable(openingRegister).ifPresent(register -> register.count(counter));
        Optional.ofNullable(closureRegister).ifPresent(register -> register.count(counter));
        registers.forEach((register -> register.count(counter)));
    }

    @Override
    public int count() {
        AtomicReference<Integer> result = new AtomicReference<>(0);

        Optional.ofNullable(openingRegister).ifPresent(register -> result.updateAndGet(v -> v + register.count()));
        Optional.ofNullable(closureRegister).ifPresent(register -> result.updateAndGet(v -> v + register.count()));
        registers.forEach(register -> result.updateAndGet(v -> v + register.count()));

        return result.get();
    }

    @Override
    public void validate(ValidationListener validationListener) {
        Optional.ofNullable(openingRegister).ifPresent(register -> register.validate(validationListener));
        registers.forEach(register -> register.validate(validationListener));
        Optional.ofNullable(closureRegister).ifPresent(register -> register.validate(validationListener)) ;
    }

    @Override
    public void write(Writer writer) {
        Optional.ofNullable(openingRegister).ifPresent(register -> register.write(writer));
        registers.forEach(register -> register.write(writer));
        Optional.ofNullable(closureRegister).ifPresent(register -> register.write(writer));
    }
}

