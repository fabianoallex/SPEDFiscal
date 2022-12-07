package sped.core;

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
}
