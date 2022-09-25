import java.io.FileWriter;
import java.util.ArrayList;

public class Block implements Unit {
    private static final String OPENING_REGISTER_BLOCK_SUFFIX_NAME = "001";
    private static final String CLOSURE_REGISTER_BLOCK_SUFFIX_NAME = "990";
    FileWriter fileWriter;
    private final String name;
    private final OpeningRegister openingRegister;
    private final ClosureRegister closureRegister;
    private final ArrayList<Register> registers;

    Block(String name, FileWriter fileWriter){
        this.openingRegister = new OpeningRegister(name + OPENING_REGISTER_BLOCK_SUFFIX_NAME, fileWriter);
        this.closureRegister = new ClosureRegister(name + CLOSURE_REGISTER_BLOCK_SUFFIX_NAME, fileWriter);
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

    void totalize(int initCount) {
        closureRegister.getFieldRegisterCount().setValue(this.count() + initCount);
        openingRegister.setThereIsMov((closureRegister.getFieldRegisterCount().getValue() > 2 ));
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
}

class OpeningRegister extends Register {
    public static final String FIELD_REGISTER_THERE_IS_MOV = "IND_MOV";
    public static final int FIELD_REGISTER_THERE_IS_MOV_YES = 0;
    public static final int FIELD_REGISTER_THERE_IS_MOV_NO = 1;

    private final IntegerField fieldRegisterThereIsMov;

    OpeningRegister(String name, FileWriter file) {
        super(name, file);
        fieldRegisterThereIsMov = this.getIntegerField(FIELD_REGISTER_THERE_IS_MOV);
    }

    public void setThereIsMov(boolean thereIs) {
        this.setFieldValue(
                FIELD_REGISTER_THERE_IS_MOV,
                (thereIs) ? FIELD_REGISTER_THERE_IS_MOV_YES : FIELD_REGISTER_THERE_IS_MOV_NO
        );
    }
}

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
