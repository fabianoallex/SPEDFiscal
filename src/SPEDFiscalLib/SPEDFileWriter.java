package SPEDFiscalLib;

import java.io.FileWriter;
import java.io.PrintWriter;

public class SPEDFileWriter implements Writer {
    private final PrintWriter printWriter;

    public SPEDFileWriter(FileWriter fileWriter) {
        this.printWriter = new PrintWriter(fileWriter);
    }

    @Override
    public void write(String string) {
        this.printWriter.println(string);
    }
}
