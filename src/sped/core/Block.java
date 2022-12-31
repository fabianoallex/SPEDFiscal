package sped.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Block implements Unit {
    private final List<Register> registers = new ArrayList<>();
    private final String name;
    private final OpeningRegister openingRegister;
    private final ClosureRegister closureRegister;
    private final Context context;

    protected Block(String blockName, String openingRegisterName, String closureRegisterName, Context context) {
        this.context = context;
        this.name = blockName;

        OpeningRegister tempOpeningRegister = null;
        ClosureRegister tempClosureRegister = null;

        if (!openingRegisterName.isEmpty())
            tempOpeningRegister = OpeningRegister.create(context, openingRegisterName);

        if (!closureRegisterName.isEmpty())
            tempClosureRegister = ClosureRegister.create(context, closureRegisterName);

        this.openingRegister = tempOpeningRegister;
        this.closureRegister = tempClosureRegister;
    }

    protected Block(String blockName, Context context) {
        this(blockName, "", "", context);
    }

    public Context getContext() {
        return context;
    }

    public ClosureRegister getClosureRegister() {
        return closureRegister;
    }

    public OpeningRegister getOpeningRegister() {
        return openingRegister;
    }

    public List<Register> getRegisters() {
        return registers;
    }

    public String getName() {
        return name;
    }

    public Register addRegister(String name){
        Register register = Register.create(name, context);
        this.registers.add(register);
        return register;
    }

    public NamedRegister addNamedRegister(Class<? extends NamedRegister> clazz) {
        NamedRegister namedRegister = NamedRegister.create(context, clazz);
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

    public static Block create(Context context, Class<? extends Block> clazz){
        try {
            return clazz.getConstructor(Context.class).newInstance(context);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Builder newBuilder(Context context, BuildListner<Block> buildListner) {
        return new Builder(context, buildListner);
    }

    public static class Builder {
        private String blockName = "";
        private String openingRegisterName = "";
        private String closureRegisterName = "";
        private final Context context;
        private final BuildListner<Block> buildListner;

        public Builder withBlockName(String blockName) {
            this.blockName = blockName;
            return this;
        }

        public Builder withOpeningRegisterName(String openingRegisterName) {
            this.openingRegisterName = openingRegisterName;
            return this;
        }

        public Builder withClosureRegisterName(String closureRegisterName) {
            this.closureRegisterName = closureRegisterName;
            return this;
        }

        public Block build(){
            Block block = new Block(blockName, openingRegisterName, closureRegisterName, context);
            this.buildListner.onBuild(block);
            return block;
        }

        protected Builder(Context context, BuildListner<Block> buildListner) {
            this.context = context;
            this.buildListner = buildListner;
        }

        protected Builder(Context context) {
            this.context = context;
            this.buildListner = b -> {};
        }
    }
}