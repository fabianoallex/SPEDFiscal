package SPEDFiscal;

public record SPEDStringBuilder(StringBuilder stringBuilder) implements Writer {
    @Override
    public void write(String string) {
        this.stringBuilder.append(string);
        this.stringBuilder.append("\n\r");
    }
}
