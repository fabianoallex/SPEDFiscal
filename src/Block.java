import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

class ClosureRegister extends Register {
    public static final String FIELD_REGISTER_COUNT = "QTD_LIN";
    private final IntegerField fieldRegisterCount;

    ClosureRegister(String name, FileWriter file) {
        super(name, file);
        fieldRegisterCount = this.getIntegerField(FIELD_REGISTER_COUNT);
    }

    public IntegerField getFieldRegisterCount() {
        return fieldRegisterCount;
    }
}

public class Block implements Unit {
    private static final String OPENING_BLOCK_REGISTER_NAME = "001";
    private static final String CLOSURE_BLOCK_REGISTER_NAME = "990";
    FileWriter fileWriter;
    private final String name;
    private final Register openingRegister;
    private final ClosureRegister closureRegister;
    private final ArrayList<Register> registers;

    Block(String name, FileWriter fileWriter){
        this.openingRegister = new Register(name + OPENING_BLOCK_REGISTER_NAME, fileWriter);
        this.closureRegister = new ClosureRegister(name + CLOSURE_BLOCK_REGISTER_NAME, fileWriter);
        this.name = name;
        this.registers = new ArrayList<>();
        this.fileWriter = fileWriter;
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

    Register addRegister(String name){
        Register register = new Register(name, fileWriter);
        this.registers.add(register);
        return register;
    }

    Register getRegister(String name){
        for (int i = 0; i< registers.size()-1; i++){
            Register register = registers.get(i);
            if (register.getName().equals(name)){
                return register;
            }
        }

        return null;
    }

    void totalize(
            
    ) {
        String[] names = {"0", "9"};  //block 0 count 0000 too // block 9 count 9999 too

        closureRegister.getFieldRegisterCount().setValue(
                this.count() + (Arrays.asList(names).contains(this.name) ? 1 : 0)
        );
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
        registers.forEach((register -> register.count(counter)));
    }

    @Override
    public int count() {
        int c = openingRegister.count() + closureRegister.count();
        for (Register register : registers) {
            c += register.count();
        }
        return c;
    }

    @Override
    public void validate() {

    }

    @Override
    public void writeToFile() {
        openingRegister.writeToFile();
        for (Register register : registers) register.writeToFile();
        closureRegister.writeToFile();
    }

    @Override
    public String toString() {
        return null;
    }
}
