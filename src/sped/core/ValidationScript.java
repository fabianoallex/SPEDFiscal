package sped.core;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public final class ValidationScript extends Validation {
    public static final String SCRIPT_ENGINE_NAME = "js";
    public static final String SCRIPT_DEF_NAME = "name";
    public static final String SCRIPT_DEF_FILE = "file";
    private final String fileName;
    private final FileLoader fileLoader;
    private String script;

    ValidationScript(String name, String fileName, FileLoader fileLoader) {
        super(name);
        this.fileLoader = fileLoader;
        this.fileName = fileName;
    }

    ValidationScript(String name) {
        this(name, "", null);
    }

    @Override
    public void validate(Register register, Field<?> field, String value, ValidationListener validationListener) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine scriptEngine = mgr.getEngineByName(ValidationScript.SCRIPT_ENGINE_NAME);

        try {
            scriptEngine.put("param", value);
            scriptEngine.put("register", register);
            String script =
                    """
                        %s;
                        var objMessage = {message: ''};
                        var isValid = %s(param, objMessage);
                        var message = objMessage.message;
                    """.formatted(
                            this.getScript(),
                            this.getName()
                    );

            scriptEngine.eval(script);

            var isValidObject = scriptEngine.get("isValid");
            if (isValidObject instanceof Boolean isValid && !isValid)
                validationListener.onErrorMessage(
                        FieldValidationEvent.newBuilder()
                                .setField(field)
                                .setRegister(register)
                                .setMessage("[%s]: %s".formatted(value, scriptEngine.get("message")))
                                .build()
                );
        } catch (ScriptException se) {
            throw new RuntimeException(se);
        }
    }

    private String getFileContents() {
        try {
            BufferedReader buffRead = new BufferedReader(new InputStreamReader(this.fileLoader.getInputStream(this.fileName)));

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
