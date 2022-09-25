import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SPEDFile implements Unit {
    FileWriter fileWriter;
    private final Register0000 openingRegister;
    private final Register9999 closureRegister;
    private Block9 block9 = null;
    private final ArrayList<Block> blocks;

    SPEDFile(FileWriter fileWriter){
        openingRegister = new Register0000(fileWriter);
        closureRegister = new Register9999(fileWriter);
        blocks = new ArrayList<>();
        this.fileWriter = fileWriter;
    }

    Block addBlock(String blockName){
        Block block = new Block(blockName, fileWriter);
        this.blocks.add(block);
        return block;
    }

    Block getBlock(String blockName){
        for (int i = 0; i< blocks.size()-1; i++){
            Block block = blocks.get(i);
            if (block.getName().equals(blockName)){
                return block;
            }
        }

        return null;
    }

    void totalize(){
        if (this.block9 == null) {
            this.block9 = new Block9(fileWriter);
            this.blocks.add(block9);
        }

        Counter counter = new Counter();     //counting before generate 9900
        Counter counter9900 = new Counter(); //used to count 9900 registers will be created above

        this.count(counter);

        for (ItemCount itemCount : counter.getArrayList()) {
            if (!itemCount.getName().equals(Register9900.REGISTER_NAME)) {
                block9.addRegister9900(itemCount.getName(), itemCount.getCount());
                counter9900.increment(Register9900.REGISTER_NAME); //increment (9900)
            }
        }

        counter9900.increment(Register9900.REGISTER_NAME); //increment itself counting (|9900|9900|x|) --> added above
        block9.addRegister9900(Register9900.REGISTER_NAME, counter9900.getCount(Register9900.REGISTER_NAME)); //add |9900|9900|x|

        //update closureRegister file register counting
        this.closureRegister.getFieldRegisterCount().setValue(counter.getCount() + counter9900.getCount());

        //totalize all blocks
        ArrayList<String> al = new ArrayList<>();
        al.add("0");
        al.add("9");

        for (Block block : this.blocks)
            block.totalize(al.contains(block.getName()) ? 1 : 0);
    }

    @Override
    public void setFileWriter(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    @Override
    public FileWriter getFileWriter() {
        return this.fileWriter;
    }

    @Override
    public void count(Counter counter) {
        openingRegister.count(counter);
        closureRegister.count(counter);
        blocks.forEach((block -> block.count(counter)));
    }

    @Override
    public int count() {
        int c = openingRegister.count() +
                closureRegister.count();

        for (Block block : blocks) {
            c += block.count();
        }

        return c;
    }

    @Override
    public void validate() {

    }

    @Override
    public void writeToFile() {
        openingRegister.writeToFile();
        for (Block block : blocks) block.writeToFile();
        closureRegister.writeToFile();
    }
}

class Register0000 extends Register{
    public static final String REGISTER_NAME = "0000";

    Register0000(FileWriter file) {
        super(REGISTER_NAME, file);
    }
}

class Register9999 extends Register {
    public static final String REGISTER_NAME = "9999";
    public static final String FIELD_REGISTER_COUNT_NAME = "QTD_LIN";
    private final IntegerField fieldRegisterCount;

    Register9999(FileWriter file) {
        super(REGISTER_NAME, file);
        fieldRegisterCount = this.getIntegerField(FIELD_REGISTER_COUNT_NAME);
    }

    public IntegerField getFieldRegisterCount() {
        return fieldRegisterCount;
    }
}

class Register9900 extends Register {
    public static final String REGISTER_NAME = "9900";
    public static final String FIELD_REGISTER_NAME = "REG_BLC";
    public static final String FIELD_REGISTER_COUNT = "QTD_REG_BLC";
    private final StringField fieldRegisterName;
    private final IntegerField fieldRegisterCount;

    Register9900(FileWriter file) {
        super(REGISTER_NAME, file);
        fieldRegisterName = this.getStringField(FIELD_REGISTER_NAME);
        fieldRegisterCount = this.getIntegerField(FIELD_REGISTER_COUNT);
    }

    public StringField getFieldRegisterName() {
        return fieldRegisterName;
    }

    public IntegerField getFieldRegisterCount() {
        return fieldRegisterCount;
    }
}

class Block9 extends Block {
    public static final String BLOCK_NAME = "9";

    Block9(FileWriter fileWriter) {
        super(BLOCK_NAME, fileWriter);
    }

    void addRegister9900(String regName, int regTotal) {
        Register9900 register9900 = new Register9900(fileWriter);
        register9900.getFieldRegisterName().setValue(regName);
        register9900.getFieldRegisterCount().setValue(regTotal);
        this.getRegisters().add(register9900);
    }
}