package SPEDFiscal;

import java.io.FileWriter;
import java.io.PrintWriter;

public interface Writer {
    void write(String string);
}

record SPEDStringBuilder(StringBuilder stringBuilder) implements Writer {
    @Override
    public void write(String string) {
        this.stringBuilder.append(string);
        this.stringBuilder.append("\n\r");
    }
}