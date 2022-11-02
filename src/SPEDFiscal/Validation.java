package SPEDFiscal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Validation {
    private final String name;

    Validation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

final class ValidationScript extends Validation {
    public static final String SCRIPT_ENGINE_NAME = "js";
    public static final String SCRIPT_DEF_NAME = "name";
    public static final String SCRIPT_DEF_FILE = "file";
    private final String fileName;
    private String script;

    private String getFileContents() {
        StringBuilder stringBuilder = new StringBuilder();
        String linha = "";

        try {
            BufferedReader buffRead = new BufferedReader(new FileReader(this.getFileName()));

            while (linha != null) {
                stringBuilder.append(linha);
                linha = buffRead.readLine();
            }

            buffRead.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }

    private String getScriptFromFile() {
        if (this.getFileName() != null)
            return this.getFileContents();

        return "";
    }

    public String getScript() {
        if (this.getFileName() != null && this.getFileName().isEmpty())
            return this.script;

        return getScriptFromFile();
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getFileName() {
        return fileName;
    }

    ValidationScript(String name) {
        this(name, "");
    }

    ValidationScript(String name, String fileName) {
        super(name);
        this.fileName = fileName;
    }
}

final class ValidationRegex extends Validation {
    public static final String REGEX_DEF_NAME = "name";
    public static final String REGEX_DEF_EXPRESSION = "expression";
    public static final String REGEX_DEF_FAIL_MESSAGE = "fail_message";
    private final String expression;
    private final String failMessage;

    ValidationRegex(String name, String expression, String failMessage) {
        super(name);
        this.failMessage = failMessage;
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public String getFailMessage() {
        return failMessage;
    }
}