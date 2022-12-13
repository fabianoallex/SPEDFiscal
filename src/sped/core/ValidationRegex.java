package sped.core;

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
    public void validate() {

    }
}
