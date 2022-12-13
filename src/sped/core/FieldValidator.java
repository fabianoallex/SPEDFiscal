package sped.core;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldValidator extends Validator {
    private final Register register;
    private Field<?> field;

    public FieldValidator(Register register, ValidationListener validatorListener) {
        super(validatorListener);
        this.register = register;
    }

    public void validate(Field<?> field) {
        this.setField(field);
        this.validate();
    }

    @Override
    public void validate() {
        String formattedValue = FieldFormatter.formatField(this.field, this.register);
        String required = this.field.getRequired();

        if (required.equals("O") && formattedValue.isEmpty()) {
            String message = "Campo ogrigatório não informado";
            this.getValidationListener().onErrorMessage(new FieldValidationEvent(register, field, message));
            return;
        }

        register.getFieldValidations(field.getName())
                .forEach(validation -> {
                    if (validation instanceof ValidationRegex validationRegex)
                        regexValidate(validationRegex, formattedValue, required);

                    if (validation instanceof ValidationScript validationScript)
                        scriptValidate(validationScript, formattedValue);

                    if (validation instanceof ValidationReflection validationReflection)
                        reflectionValidate(validationReflection, formattedValue);
                });
    }

    private void reflectionValidate(ValidationReflection validationReflection, String value) {
        ValidationMessage validationMessage = new ValidationMessage();

        if (!validationReflection.validate(validationMessage, value, register)) {
            String message = "[" + value + "]: " + validationMessage.getMessage();
            this.getValidationListener().onErrorMessage(new FieldValidationEvent(register, field, message));
        }
    }

    private void regexValidate(ValidationRegex validationRegex, String value, String required) {
        if (!required.equals("O") && value.isEmpty())
            return;

        String failMessage = validationRegex.getFailMessage();
        String expression = validationRegex.getExpression();

        if (!expression.isEmpty()) {
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(value);
            if (!matcher.matches()) {
                String message = "[" + value + "]: " + failMessage;
                this.getValidationListener().onErrorMessage(new FieldValidationEvent(register, field, message));
            }
        }
    }

    private void scriptValidate(ValidationScript validationScript, String value) {
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
                    validationScript.getScript(),
                    validationScript.getName()
                );

            scriptEngine.eval(script);

            var isValidObject = scriptEngine.get("isValid");
            var message = scriptEngine.get("message");

            if (isValidObject instanceof Boolean isValid && !isValid)
                this.getValidationListener().onErrorMessage(
                        new FieldValidationEvent(register, field, "[%s]: %s".formatted(value, message))
                );
        } catch (ScriptException se) {
            throw new RuntimeException(se);
        }
    }

    public void setField(Field<?> field) {
        this.field = field;
    }

    public Field<?> getField() {
        return field;
    }

    public Register getRegister() {
        return register;
    }
}
