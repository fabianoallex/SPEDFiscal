package SPEDFiscal;

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

            if (!required.equals("O") && formattedValue.isEmpty()) {
                continue;
            }

            if (required.equals("O") && formattedValue.isEmpty()) {
                String message = "Campo ogrigatório não informado";
                validationListener.onErrorMessage(new ValidationEventField(register, field, message));
                continue;
            }

            ValidationRegex validationRegex = register.getDefinitions().getValidationRegex(register.getName(), field.getName());
            String expression = validationRegex != null ? validationRegex.getExpression() : "";
            String failMessage = validationRegex != null ? validationRegex.getFailMessage() : "";

            if (!expression.isEmpty()) {
                Pattern pattern = Pattern.compile(expression);
                Matcher matcher = pattern.matcher(formattedValue);
                if (!matcher.matches()) {
                    String message = register.getName() + "." + field.getName() + ": \"" + formattedValue + "\": \"" + failMessage + "\"";
                    validationListener.onErrorMessage(new ValidationEvent(message));
                }
            }
        }
    }
}
