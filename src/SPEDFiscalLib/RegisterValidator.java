package SPEDFiscalLib;

import java.util.Map;

public class RegisterValidator extends Validator {
    private final Register register;
    private final FieldValidator fieldValidator;

    RegisterValidator(Register register, ValidationListener validationListener) {
        super(validationListener);
        this.register = register;
        this.fieldValidator = new FieldValidator(register, validationListener);
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public void validate() {
        for (Map.Entry<String, Field<?>> e : register.getFields().entrySet()) {
            Field<?> field = e.getValue();
            fieldValidator.validate(field);
        }
    }
}
