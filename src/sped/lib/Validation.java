package sped.lib;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Validation {
    private final String name;

    Validation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

final class ValidationReflection extends Validation {
    private final ValidationHelper validationHelper;
    ValidationReflection(ValidationHelper validationHelper, String name) {
        super(name);
        this.validationHelper = validationHelper;
    }

    public ValidationHelper getvalidationHelper() {
        return this.validationHelper;
    }

    public boolean validate(ValidationMessage validationMessage, String value, Register register) {
         return this.getvalidationHelper().validate(validationMessage, this.getName(), value, register);
    }
}

final class ValidationScript extends Validation {
    public static final String SCRIPT_ENGINE_NAME = "js";
    public static final String SCRIPT_DEF_NAME = "name";
    public static final String SCRIPT_DEF_FILE = "file";
    private final String fileName;
    private final DefinitionsFileLoader definitionsFileLoader;
    private String script;

    ValidationScript(String name, String fileName, DefinitionsFileLoader definitionsFileLoader) {
        super(name);
        this.definitionsFileLoader = definitionsFileLoader;
        this.fileName = fileName;
    }

    ValidationScript(String name) {
        this(name, "", null);
    }

    private String getFileContents() {
        try {
            //URL url = new URL(this.fileName);

            BufferedReader buffRead = new BufferedReader(new InputStreamReader(this.definitionsFileLoader.getInputStream(this.fileName)));

            //BufferedReader buffRead = new BufferedReader(new FileReader(Paths.get(url.toURI()).toFile()));

            final String fileContents = buffRead
                    .lines()
                    .collect(Collectors.joining("\n"));

            buffRead.close();

            return fileContents;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getScriptFromFile() {
        if (this.getFileName() != null)
            return this.getFileContents();

        return "";
    }

    public String getScript() {
        if (this.getFileName() == null || this.getFileName().isEmpty())
            return this.script;

        return getScriptFromFile();
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getFileName() {
        return fileName;
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