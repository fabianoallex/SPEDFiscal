package sped.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexValidation extends Validation {
    public static final String REGEX_DEF_NAME = "name";
    public static final String REGEX_DEF_EXPRESSION = "expression";
    public static final String REGEX_DEF_FAIL_MESSAGE = "fail_message";
    private final String expression;
    private final String failMessage;

    RegexValidation(String name, String expression, String failMessage) {
        super(name);
        this.failMessage = failMessage;
        this.expression = expression;
    }

    public String getExpression() {
        if (expression == null)
            return "";

        return expression;
    }

    public String getFailMessage() {
        return failMessage;
    }

    @Override
    public void validate(Register register, Field<?> field, String value, ValidationListener validationListener) {
        if (this.getExpression().isEmpty())
            return;

        Pattern pattern = Pattern.compile(this.getExpression());
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            validationListener.onErrorMessage(
                    FieldValidationEvent.newBuilder()
                            .setField(field)
                            .setRegister(register)
                            .setMessage("[%s]: %s".formatted(value, this.getFailMessage()))
                            .build()
            );
        }
    }
}
