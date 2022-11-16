package SPEDFiscalLib;

public record SPEDStringBuilder(StringBuilder stringBuilder) implements Writer {
    @Override
    public void write(String string, Register register) {
        this.stringBuilder.append(string);
        this.stringBuilder.append("\n\r");
    }
}
