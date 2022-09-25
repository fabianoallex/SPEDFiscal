import java.io.FileWriter;

public interface Unit {
    void setFileWriter(FileWriter fileWriter);
    FileWriter getFileWriter();
    void count(Counter counter);
    int count();
    void validate();
    void writeToFile();
    String toString();
}
