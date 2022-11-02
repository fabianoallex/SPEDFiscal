package SPEDFiscal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ValidationEventField extends ValidationEvent {
    private final Field field;
    private final Register register;

    ValidationEventField(Register register, Field field, String message) {
        super(register.getName() + "." + field.getName() + ": \"" + message + "\"" );

        this.register = register;
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public Register getRegister() {
        return register;
    }
}

public class RegisterValidator {
    void validate(Register register, ValidationListener validationListener) {
        for (Map.Entry<String, Field<?>> e : register.getFields().entrySet()) {
            Field<?> field = e.getValue();

            String fieldFormatName = register.getName() + "." + field.getName();
            String formattedValue = register.getDefinitions().formatField(field, fieldFormatName);
            String required = register.getDefinitions().getRequired(register.getName(), field.getName());

            if (required.equals("O") && formattedValue.isEmpty()) {
                String message = "Campo ogrigatório não informado";
                validationListener.onErrorMessage(new ValidationEventField(register, field, message));
                continue; //quando o campo for obrigatorio e não for informado, nao faz as demais validações
            }

            Validation[] validations = register.getDefinitions().getValidations(
                    register.getName(),
                    field.getName()
            );

            if (validations != null) {
                for (Validation validation : validations) {
                    if (validation instanceof ValidationRegex validationRegex) {
                        //se o campo não é obrigatorio e não foi informado nao aplica as
                        // validações regex, se aplica apenas para regex, para scripts,
                        //tratado no outro if, nao se aplica essa verificação.
                        if (!required.equals("O") && formattedValue.isEmpty()) {
                            continue;
                        }

                        String failMessage = validationRegex.getFailMessage();
                        String expression = validationRegex.getExpression();

                        if (!expression.isEmpty()) {
                            Pattern pattern = Pattern.compile(expression);
                            Matcher matcher = pattern.matcher(formattedValue);
                            if (!matcher.matches()) {
                                String message = register.getName() + "." + field.getName() + ": \"" + formattedValue + "\": \"" + failMessage + "\"";
                                validationListener.onErrorMessage(new ValidationEvent(message));
                            }
                        }
                    }

                    if (validation instanceof ValidationScript validationScript) {
                        ScriptEngineManager mgr = new ScriptEngineManager();
                        ScriptEngine scriptEngine = mgr.getEngineByName("nashorn");

                        try {
                            scriptEngine.put("param", formattedValue);
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

                            if (isValidObject instanceof Boolean isValid && !isValid) {
                                message = register.getName() + "." + field.getName() + ": \"" + formattedValue + "\": \"" + message + "\".";
                                validationListener.onErrorMessage(new ValidationEvent((String) message));
                            }
                        } catch (ScriptException se) {
                            throw new RuntimeException(se);
                        }
                    }
                }
            }
        }
    }
}
