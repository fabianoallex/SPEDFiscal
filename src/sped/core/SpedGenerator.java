package sped.core;

import sped.lcdpr.v0013.ClosureRegisterLCDPR;
import sped.lcdpr.v0013.OpeningRegisterLCDPR;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class SpedGenerator extends GeneratorBase {
    private final Register0000 register0000;
    private final Register9999 register9999;
    private Block9 block9 = null;

    SpedGenerator(Factory factory) {
        super(factory);
        register0000 = this.getFactory().createRegister0000();
        register9999 = this.getFactory().createRegister9999();
    }

    public NamedRegister createNamedRegisterTo0000(Class<? extends NamedRegister> clazz) {
        try {
            return clazz.getConstructor(Register.class)
                    .newInstance(this.register0000.getRegister());
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public NamedRegister createNamedRegisterTo9999(Class<? extends NamedRegister> clazz) {
        try {
            return clazz.getConstructor(Register.class)
                    .newInstance(this.register9999.getRegister());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Register0000 getRegister0000() {
        return register0000;
    }
    public Register9999 getRegister9999() {
        return register9999;
    }

    public void generateBlock9(){
        this.getBlocks().remove(this.block9);  //se ja exister algum bloco9. remove
        this.block9 = new Block9(this.getFactory());
        this.getBlocks().add(this.block9);

        Counter counter = new Counter();     //counting before generate 9900
        Counter counter9900 = new Counter(); //used to count 9900 registers will be created above

        this.count(counter);

        counter.forEach((registerName, count) -> {
            if (!registerName.equals(Register9900.REGISTER_NAME)) {
                block9.addRegister9900(registerName, count);
                counter9900.increment(Register9900.REGISTER_NAME); //increment (9900)
            }
        });

        counter9900.increment(Register9900.REGISTER_NAME); //increment itself counting (|9900|9900|x|) --> added above
        block9.addRegister9900(Register9900.REGISTER_NAME, counter9900.count(Register9900.REGISTER_NAME)); //add |9900|9900|x|
    }

    public void totalize() {
        List<String> blocks0and9 = List.of("0", "9"); //blocos 0 e 9 tem +1 incrementado ao total
        getBlocks().forEach(block -> block.totalize(blocks0and9.contains(block.getName()) ? 1 : 0));
        register9999.getFieldRegisterCount().setValue(this.count());
    }

    @Override
    public void count(Counter counter) {
        register0000.count(counter);
        register9999.count(counter);
        super.count(counter);
    }

    @Override
    public int count() {
        return super.count() +
                register0000.count() +
                register9999.count();
    }

    @Override
    public void validate(ValidationListener validationListener) {
        register0000.validate(validationListener);
        super.validate(validationListener);
        register9999.validate(validationListener);
    }

    @Override
    public void write(Writer writer) {
        register0000.write(writer);
        super.write(writer);
        register9999.write(writer);
    }
}


