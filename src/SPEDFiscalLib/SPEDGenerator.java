package SPEDFiscalLib;

import java.util.ArrayList;

public class SPEDGenerator implements Unit {
    private final ArrayList<Block> blocks = new ArrayList<>();
    private final Register0000 register0000;
    private final Register9999 register9999;
    private final Factory factory;
    private Block9 block9 = null;

    SPEDGenerator(Factory factory) {
        this.factory = factory;
        register0000 = this.factory.createRegister0000();
        register9999 = this.factory.createRegister9999();
    }

    public Register0000 getRegister0000() {
        return register0000;
    }
    public Register9999 getRegister9999() {
        return register9999;
    }

    public Block addBlock(String blockName) {
        Block block = new Block(blockName, factory);
        this.blocks.add(block);
        return block;
    }

    public Block getBlock(String blockName) {
        for (int i=0; i<blocks.size()-1; i++) {
            Block block = blocks.get(i);
            if (block.getName().equals(blockName)) return block;
        }

        return null;
    }

    public void totalize(){
        this.blocks.remove(this.block9);  //se ja exister algum bloco9. remove
        this.block9 = new Block9(this.factory);
        this.blocks.add(this.block9);

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

        //update closureRegister file register counting
        this.register9999.getFieldRegisterCount().setValue(counter.count() + counter9900.count());

        //os blocos 0 e 9 devem ter incrementado +1 ao total do bloco, devido os registros 0000 e 9999
        //não fazerem parte dos blocos 0 e 9, mas devem ser considerados na totalização desses blocos
        ArrayList<String> al = new ArrayList<>();
        al.add("0");
        al.add("9");

        //totalize all blocks
        for (Block block : this.blocks)
            block.totalize(al.contains(block.getName()) ? 1 : 0);
    }

    @Override
    public void count(Counter counter) {
        register0000.count(counter);
        register9999.count(counter);
        blocks.forEach((block -> block.count(counter)));
    }

    @Override
    public int count() {
        int c = register0000.count() + register9999.count();
        for (Block block : blocks) c += block.count();
        return c;
    }

    @Override
    public void validate(ValidationListener validationListener) {
        register0000.validate(validationListener);
        for (Block block : blocks)  block.validate(validationListener);
        register9999.validate(validationListener);
    }

    @Override
    public void write(Writer writer) {
        register0000.write(writer);
        for (Block block : blocks) block.write(writer);
        register9999.write(writer);
    }
}


