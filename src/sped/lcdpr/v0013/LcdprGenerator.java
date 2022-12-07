package sped.lcdpr.v0013;

import sped.core.*;
import sped.lcdpr.v0013.ClosureRegisterLCDPR;
import sped.lcdpr.v0013.OpeningRegisterLCDPR;

import java.util.List;

public class LcdprGenerator extends GeneratorBase {
    private final OpeningRegisterLCDPR openingRegisterLCDPR;
    private final ClosureRegisterLCDPR closureRegisterLCDPR;

    public LcdprGenerator(Factory factory) {
        super(factory);
        openingRegisterLCDPR = new OpeningRegisterLCDPR(factory.createRegister(OpeningRegisterLCDPR.REGISTER_NAME));
        closureRegisterLCDPR = new ClosureRegisterLCDPR(factory.createRegister(ClosureRegisterLCDPR.REGISTER_NAME));
    }

    public OpeningRegisterLCDPR getOpeningRegisterLCDPR() {
        return openingRegisterLCDPR;
    }

    public ClosureRegisterLCDPR getClosureRegisterLCDPR() {
        return closureRegisterLCDPR;
    }

    public void totalize() {
        closureRegisterLCDPR.setQuantidadeLinhas(this.count());
    }

    @Override
    public void count(Counter counter) {
        openingRegisterLCDPR.count(counter);
        closureRegisterLCDPR.count(counter);
        super.count(counter);
    }

    @Override
    public int count() {
        return super.count() +
                openingRegisterLCDPR.count() +
                closureRegisterLCDPR.count();
    }

    @Override
    public void validate(ValidationListener validationListener) {
        openingRegisterLCDPR.validate(validationListener);
        super.validate(validationListener);
        closureRegisterLCDPR.validate(validationListener);
    }

    @Override
    public void write(Writer writer) {
        openingRegisterLCDPR.write(writer);
        super.write(writer);
        closureRegisterLCDPR.write(writer);
    }
}
