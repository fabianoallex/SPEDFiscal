package sped.lcdpr.v0013;

import sped.core.*;

public class LcdprGenerator extends GeneratorBase {
    private final OpeningRegister openingRegister;
    private final ClosureRegister closureRegister;

    public LcdprGenerator(Factory factory) {
        super(factory);
        openingRegister = new OpeningRegister(factory.createRegister(OpeningRegister.REGISTER_NAME));
        closureRegister = new ClosureRegister(factory.createRegister(ClosureRegister.REGISTER_NAME));
    }

    public OpeningRegister getOpeningRegister() {
        return openingRegister;
    }

    public ClosureRegister getClosureRegister() {
        return closureRegister;
    }

    public void totalize() {
        closureRegister.setQuantidadeLinhas(this.count());
    }

    public BlockQ addBlockQ() {
        return (BlockQ) addBlock(BlockQ.class);
    }

    public Block0 addBlock0() {
        return (Block0) addBlock(Block0.class);
    }

    @Override
    public void count(Counter counter) {
        openingRegister.count(counter);
        closureRegister.count(counter);
        super.count(counter);
    }

    @Override
    public int count() {
        return super.count() +
                openingRegister.count() +
                closureRegister.count();
    }

    @Override
    public void validate(ValidationListener validationListener) {
        openingRegister.validate(validationListener);
        super.validate(validationListener);
        closureRegister.validate(validationListener);
    }

    @Override
    public void write(Writer writer) {
        openingRegister.write(writer);
        super.write(writer);
        closureRegister.write(writer);
    }

    public static class Builder extends GeneratorBase.Builder<LcdprGenerator> {
        private Builder(String xmlFile) {
            super(xmlFile);
        }
    }

    public static Builder newBuilder(String xmlFile) {
        return new Builder(xmlFile);
    }
}
