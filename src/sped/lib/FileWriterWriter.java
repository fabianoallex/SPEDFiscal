package sped.lib;

import java.io.FileWriter;
import java.io.PrintWriter;

public class FileWriterWriter implements Writer {
    private final PrintWriter printWriter;

    public FileWriterWriter(FileWriter fileWriter) {
        this.printWriter = new PrintWriter(fileWriter);
    }

    @Override
    public void write(String string, Register register) {
        this.printWriter.println(string);
    }
}
