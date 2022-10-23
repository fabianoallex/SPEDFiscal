package SPEDFiscal;

import java.util.ArrayList;

public class SPEDGenerator implements Unit {
    private final ArrayList<Block> blocks = new ArrayList<>();
    private SPEDDefinitions definitions;
    private final Register0000 openingRegister;
    private final Register9999 closureRegister;
    private Block9 block9 = null;

    public SPEDGenerator(SPEDDefinitions definitions){
        this.definitions = definitions;
        openingRegister = new Register0000(definitions);
        closureRegister = new Register9999(definitions);
    }

    public Register0000 getOpeningRegister() {
        return openingRegister;
    }

    public Block addBlock(String blockName){
        Block block = new Block(blockName, definitions);
        this.blocks.add(block);
        return block;
    }

    public Block getBlock(String blockName){
        for (int i=0; i<blocks.size()-1; i++){
            Block block = blocks.get(i);
            if (block.getName().equals(blockName)){
                return block;
            }
        }

        return null;
    }

    public void totalize(){
        this.blocks.remove(this.block9);  //se ja exister algum bloco9. remove
        this.block9 = new Block9(definitions);
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
        this.closureRegister.getFieldRegisterCount().setValue(counter.count() + counter9900.count());

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
        openingRegister.count(counter);
        closureRegister.count(counter);
        blocks.forEach((block -> block.count(counter)));
    }

    @Override
    public int count() {
        int c = openingRegister.count() + closureRegister.count();
        for (Block block : blocks) c += block.count();
        return c;
    }

    @Override
    public void validate() {

    }

    @Override
    public void write(Writer writer) {
        openingRegister.write(writer);
        for (Block block : blocks) block.write(writer);
        closureRegister.write(writer);
    }
}

class Register0000 extends Register{
    public static final String REGISTER_NAME = "0000";

    Register0000(SPEDDefinitions definitions) {
        super(REGISTER_NAME, definitions);
    }
}

class Register9999 extends Register {
    public static final String REGISTER_NAME = "9999";
    public static final String FIELD_REGISTER_COUNT_NAME = "QTD_LIN";
    private final Field<Integer> fieldRegisterCount;

    Register9999(SPEDDefinitions definitions) {
        super(REGISTER_NAME, definitions);
        try {
            fieldRegisterCount = (Field<Integer>) this.getField(FIELD_REGISTER_COUNT_NAME);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<Integer> getFieldRegisterCount() {
        return fieldRegisterCount;
    }
}

class Register9900 extends Register {
    public static final String REGISTER_NAME = "9900";
    public static final String FIELD_REGISTER_NAME = "REG_BLC";
    public static final String FIELD_REGISTER_COUNT = "QTD_REG_BLC";
    private final Field<String> fieldRegisterName;
    private final Field<Integer> fieldRegisterCount;

    Register9900(SPEDDefinitions definitions) {
        super(REGISTER_NAME, definitions);
        try {
            fieldRegisterName = (Field<String>) this.getField(FIELD_REGISTER_NAME);
            fieldRegisterCount = (Field<Integer>) this.getField(FIELD_REGISTER_COUNT);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Field<String> getFieldRegisterName() {
        return fieldRegisterName;
    }

    public Field<Integer> getFieldRegisterCount() {
        return fieldRegisterCount;
    }
}

class Block9 extends Block {
    public static final String BLOCK_NAME = "9";

    Block9(SPEDDefinitions definitions) {
        super(BLOCK_NAME, definitions);
    }

    void addRegister9900(String regName, int regTotal) {
        Register9900 register9900 = new Register9900(this.getDefinitions());
        register9900.getFieldRegisterName().setValue(regName);
        register9900.getFieldRegisterCount().setValue(regTotal);
        this.getRegisters().add(register9900);
    }
}