package sped.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidationRegex extends Validation {
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

    @Override
    public void validate(Register register, Field<?> field, String value, ValidationListener validationListener) {
        String failMessage = this.getFailMessage();
        String expression = this.getExpression();

        if (!expression.isEmpty()) {
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(value);
            if (!matcher.matches()) {
                String message = "[" + value + "]: " + failMessage;
                validationListener.onErrorMessage(
                        FieldValidationEvent.newBuilder()
                                .setField(field)
                                .setRegister(register)
                                .setMessage(message)
                                .build()
                );
            }
        }
    }
}
