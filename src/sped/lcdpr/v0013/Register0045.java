package sped.lcdpr.v0013;

import sped.core.Context;
import sped.core.NamedRegister;

public class Register0045 extends NamedRegister {
    public enum TipoContraparte {
        CONDOMINIO(1),
        ARRENDADOR(2),
        PARCEIRO(3),
        COMODANTE(4),
        OUTRO(5);

        final Integer value;

        TipoContraparte(Integer value) {
            this.value = value;
        }

        public static Register0045.TipoContraparte valueOf(int value) {
            for (Register0045.TipoContraparte tipoContraparte : values())
                if (tipoContraparte.value.equals(value))
                    return tipoContraparte;

            return null;
        }
    };

    public static String REGISTER_NAME = "0045";
    public static String FIELD_CODIGO_IMOVEL = "COD_IMOVEL";
    public static String FIELD_TIPO_CONTRAPARTE = "TIPO_CONTRAPARTE";
    public static String FIELD_ID_CONTRAPARTE = "ID_CONTRAPARTE";
    public static String FIELD_NOME_CONTRAPARTE = "NOME_CONTRAPARTE";
    public static String FIELD_PERC_CONTRAPARTE = "PERC_CONTRAPARTE";

    public Register0045(Context context) {
        super(context, REGISTER_NAME);
    }

    public void setCodigoImovel(Integer codigoImovel) {
        this.getIntegerField(FIELD_CODIGO_IMOVEL).setValue(codigoImovel);
    }

    public Integer getCodigoImovel() {
        return this.getIntegerField(FIELD_CODIGO_IMOVEL).getValue();
    }

    public void setTipoContraparte(TipoContraparte tipoContraparte) {
        this.getIntegerField(FIELD_TIPO_CONTRAPARTE).setValue(tipoContraparte.value);
    }

    public TipoContraparte getTipoContraparte() {
        return TipoContraparte.valueOf(this.getIntegerField(FIELD_TIPO_CONTRAPARTE).getValue());
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.getStringField(FIELD_ID_CONTRAPARTE).setValue(cpfCnpj);
    }

    public String getCpfCnpj() {
        return this.getStringField(FIELD_ID_CONTRAPARTE).getValue();
    }

    public void setNome(String nome) {
        this.getStringField(FIELD_NOME_CONTRAPARTE).setValue(nome);
    }

    public String getNome() {
        return this.getStringField(FIELD_NOME_CONTRAPARTE).getValue();
    }

    public void setParticipacao(Double participacao) {
        this.getDoubleField(FIELD_PERC_CONTRAPARTE).setValue(participacao);
    }

    public Double getParticipacao() {
        return this.getDoubleField(FIELD_PERC_CONTRAPARTE).getValue();
    }
}

