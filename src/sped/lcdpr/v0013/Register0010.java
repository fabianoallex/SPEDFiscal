package sped.lcdpr.v0013;

import sped.core.Context;
import sped.core.NamedRegister;

public class Register0010 extends NamedRegister {
    public enum FormaApuracao {
        LIVRO_CAIXA(0),
        APURACAO_LUCRO(1);

        final Integer value;
        FormaApuracao(Integer value) {
            this.value = value;
        }

        public static Register0010.FormaApuracao valueOf(int value) {
            for (Register0010.FormaApuracao formaApuracao : values())
                if (formaApuracao.value.equals(value))
                    return formaApuracao;

            return null;
        }
    };

    public static String REGISTER_NAME = "0010";
    public static String FIELD_FORMA_APURACAO = "FORMA_APUR";

    public Register0010(Context context) {
        super(context, REGISTER_NAME);
    }

    public void setFormaApuracao(FormaApuracao formaApuracao) {
        this.getIntegerField(FIELD_FORMA_APURACAO).setValue(formaApuracao.value);
    }

    public FormaApuracao getFormaApuracao() {
        return FormaApuracao.valueOf(this.getIntegerField(FIELD_FORMA_APURACAO).getValue());
    }
}

