package sped.lcdpr;

import sped.lib.Factory;
import sped.lib.NamedRegister;

import java.util.jar.Attributes;

public class Register0010 extends NamedRegister {
    public static String REGISTER_NAME = "0010";
    public static String FIELD_FORMA_APURACAO = "FORMA_APUR";

    public Register0010(Factory factory) {
        super(factory, REGISTER_NAME);
    }

    public void setFormaApuracao(Integer formaApuracao) {
        this.getIntegerField(FIELD_FORMA_APURACAO).setValue(formaApuracao);
    }

    public Integer getFormaApuracao() {
        return this.getIntegerField(FIELD_FORMA_APURACAO).getValue();
    }
}

