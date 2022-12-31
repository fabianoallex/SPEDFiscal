package sped.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

public class GeneratorBase implements Unit {
    private final ArrayList<Block> blocks = new ArrayList<>();
    private final Context context;

    public GeneratorBase(Context context) {
        this.context = context;
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
        return blocks.stream()
                .mapToInt(Block::count)
                .sum();
    }

    @Override
    public void validate(ValidationListener validationListener) {
        blocks.forEach(block -> block.validate(validationListener));
    }

    @Override
    public void write(Writer writer) {
        blocks.forEach(block -> block.write(writer));
    }

    private Block addBlock(String blockName, String openingRegisterName, String closureRegisterName) {
        return Block.newBuilder(this.getContext(), this.blocks::add)
                .withBlockName(blockName)
                .withOpeningRegisterName(openingRegisterName)
                .withClosureRegisterName(closureRegisterName)
                .build();
    }

    public Block addBlock(Class<? extends Block> clazz) {
        Block block = Block.create(context, clazz);
        this.blocks.add(block);
        return block;
    }

    public Block.Builder newBlockBuilder(){
        return Block.newBuilder(this.getContext(), this.blocks::add);
    }

    public Block addBlock(String blockName) {
        Block block = new Block(blockName, context);
        this.blocks.add(block);
        return block;
    }

    public Block getBlock(String blockName) {
        return blocks.stream()
                .filter(block -> block.getName().equals(blockName))
                .findFirst()
                .orElse(null);
    }

    public Context getContext() {
        return context;
    }

    public static class Builder<T extends GeneratorBase> {
        private final String xmlFile;
        private String fieldsSeparator = Definitions.REGISTER_FIELD_SEPARATOR_DEFAULT;
        private String beginEndSeparator = Definitions.REGISTER_FIELD_BEGIN_END_SEPARATOR_DEFAULT;
        private FileLoader fileLoader;
        private ValidationHelper validationHelper;
        public Builder(String xmlFile) {
            this.xmlFile = xmlFile;
        }

        public Builder<T> withBeginEndSeparator(String registerBeginEndSeparator) {
            this.beginEndSeparator = registerBeginEndSeparator;
            return this;
        }

        public Builder<T> withFieldsSeparator(String fieldsSeparator) {
            this.fieldsSeparator = fieldsSeparator;
            return this;
        }

        public Builder<T> withFileLoader(FileLoader fileLoader) {
            this.fileLoader = fileLoader;
            return this;
        }

        public Builder<T> withValidationHelper(ValidationHelper validationHelper) {
            this.validationHelper = validationHelper;
            return this;
        }

        public T build() {
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            @SuppressWarnings("unchecked")
            var clazz = (Class<T>) type.getActualTypeArguments()[0];

            return this.build(clazz);
        }

        private T build(Class<T> clazz) {
            try {
                Context context = Definitions.newBuilder(this.xmlFile)
                        .withBeginEndSeparator(this.beginEndSeparator)
                        .withFieldsSeparator(this.fieldsSeparator)
                        .withValidationHelper(this.validationHelper)
                        .withFileLoader(this.fileLoader)
                        .build()
                        .newContext();

                return clazz.getConstructor(Context.class).newInstance(context);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static GeneratorBase create(Context context, Class<? extends GeneratorBase> clazz){
        try {
            return clazz.getConstructor(Context.class).newInstance(context);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
