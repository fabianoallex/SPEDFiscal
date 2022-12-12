package sped.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class GeneratorBase implements Unit {
    private final ArrayList<Block> blocks = new ArrayList<>();
    private final Factory factory;

    public GeneratorBase(Factory factory) {
        this.factory = factory;
    }

    protected ArrayList<Block> getBlocks() {
        return blocks;
    }

    @Override
    public void count(Counter counter) {
        blocks.forEach((block -> block.count(counter)));
    }

    @Override
    public int count() {
        return blocks.stream().mapToInt(Block::count).sum();
    }

    @Override
    public void validate(ValidationListener validationListener) {
        blocks.forEach(block -> block.validate(validationListener));
    }

    @Override
    public void write(Writer writer) {
        blocks.forEach(block -> block.write(writer));
    }

    public Block addBlock(String blockName, String openingRegisterName, String closureRegisterName) {
        Block block = new Block(blockName, openingRegisterName, closureRegisterName, factory);
        this.blocks.add(block);
        return block;
    }

    public Block addBlock(Class<? extends Block> clazz) {
        Block block = this.factory.createBlock(clazz);
        this.blocks.add(block);
        return block;
    }

    public Block addBlock(String blockName) {
        Block block = new Block(blockName, factory);
        this.blocks.add(block);
        return block;
    }

    public Block getBlock(String blockName) {
        for (int i = 0; i < blocks.size() - 1; i++) {
            Block block = blocks.get(i);

            if (block.getName().equals(blockName))
                return block;
        }

        return null;
    }

    public Factory getFactory() {
        return factory;
    }

    public static abstract class Builder {
        private final String xmlFile;
        private String fieldsSeparator = Definitions.REGISTER_FIELD_SEPARATOR_DEFAULT;
        private String beginEndSeparator = Definitions.REGISTER_FIELD_BEGIN_END_SEPARATOR_DEFAULT;
        private DefinitionsFileLoader definitionsFileLoader;
        private ValidationHelper validationHelper;
        public Builder(String xmlFile) {
            this.xmlFile = xmlFile;
        }

        public Builder setBeginEndSeparator(String registerBeginEndSeparator) {
            this.beginEndSeparator = registerBeginEndSeparator;
            return this;
        }

        public Builder setFieldsSeparator(String fieldsSeparator) {
            this.fieldsSeparator = fieldsSeparator;
            return this;
        }

        public Builder setFileLoader(DefinitionsFileLoader definitionsFileLoader) {
            this.definitionsFileLoader = definitionsFileLoader;
            return this;
        }

        public Builder setValidationHelper(ValidationHelper validationHelper) {
            this.validationHelper = validationHelper;
            return this;
        }

        public GeneratorBase build(Class<? extends GeneratorBase> clazz){
            try {
                Factory factory = new Definitions.Builder(this.xmlFile)
                        .setBeginEndSeparator(this.beginEndSeparator)
                        .setFieldsSeparator(this.fieldsSeparator)
                        .setValidationHelper(this.validationHelper)
                        .setFileLoader(this.definitionsFileLoader)
                        .build()
                        .newFactory();

                return clazz.getConstructor(Factory.class).newInstance(factory);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        public abstract GeneratorBase build();
    }
}
