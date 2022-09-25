import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class Register implements Unit {
    private final String name;
    FileWriter file;
    private final ArrayList<Unit> items;
    private final Fields fields;

    Register(String name, FileWriter file){
        this.name = name;
        this.file = file;
        items = new ArrayList<>();
        this.fields = Fields.createFields(name);
    }

    public Fields getFields() {
        return fields;
    }

    public FileWriter getFileWriter() {
        return file;
    }

    @Override
    public void count(Counter counter) {
        counter.increment(this.name);
        items.forEach((unit -> unit.count(counter)));
    }

    @Override
    public int count() {
        int c = 1; //itself

        for (Unit unit : items) {
            c += unit.count();
        }

        return c;
    }

    @Override
    public void validate() {
        //todo: implementar rotinas de validação dos registros
    }

    @Override
    public void writeToFile() {
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println(this);
    }

    @Override
    public String toString() {
        return "|%s|%s".formatted(this.getName(), fields.toString());
    }

    public void setFileWriter(FileWriter fileWriter) {
        this.file = fileWriter;
    }

    public String getName() {
        return name;
    }

    public Field<?> getFieldByName(String fieldName){
        return getFields().getFieldByName(fieldName);
    }

    public StringField getStringField(String fieldName){
        return getFields().getStringFieldByName(fieldName);
    }

    public IntegerField getIntegerField(String fieldName){
        return getFields().getIntegerFieldByName(fieldName);
    }

    public DoubleField getDoubleField(String fieldName){
        return getFields().getDoubleFieldByName(fieldName);
    }

    public DateField getDateField(String fieldName){
        return getFields().getDateFieldByName(fieldName);
    }

    public void setFieldValue(String name, String value) {
        this.getStringField(name).setValue(value);
    }

    public void setFieldValue(String name, Double value) {
        this.getDoubleField(name).setValue(value);
    }

    public void setFieldValue(String name, int value) {
        this.getIntegerField(name).setValue(value);
    }

    public void setFieldValue(String name, Date date) {
        this.getDateField(name).setValue(date);
    }
}
