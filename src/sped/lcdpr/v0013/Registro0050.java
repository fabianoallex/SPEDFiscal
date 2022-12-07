package sped.lcdpr.v0013;

import sped.core.Factory;
import sped.core.NamedRegister;

public class Registro0050 extends NamedRegister {
    public static String REGISTER_NAME = "0050";
    public static String FIELD_CODIGO_CONTA = "COD_CONTA";
    public static String FIELD_PAIS_CONTA = "PAIS_CONTA";
    public static String FIELD_BANCO = "BANCO";
    public static String FIELD_NOME_BANCO = "NOME_BANCO";
    public static String FIELD_AGENCIA = "AGENCIA";
    public static String FIELD_NUM_CONTA = "NUM_CONTA";

    public Registro0050(Factory factory) {
        super(factory, REGISTER_NAME);
    }

    public void setCodigoConta(Integer codigoConta) {
        this.getIntegerField(FIELD_CODIGO_CONTA).setValue(codigoConta);
    }

    public Integer getCodigoConta() {
        return this.getIntegerField(FIELD_CODIGO_CONTA).getValue();
    }

    public void setPais(String pais) {
        this.getStringField(FIELD_PAIS_CONTA).setValue(pais);
    }

    public String getPais() {
        return this.getStringField(FIELD_PAIS_CONTA).getValue();
    }

    public void setCodigoBanco(Integer codigoBanco) {
        this.getIntegerField(FIELD_BANCO).setValue(codigoBanco);
    }

    public Integer getCodigoBanco() {
        return this.getIntegerField(FIELD_BANCO).getValue();
    }

    public void setNomeBanco(String nomeBanco) {
        this.getStringField(FIELD_NOME_BANCO).setValue(nomeBanco);
    }

    public String getNomeBanco() {
        return this.getStringField(FIELD_NOME_BANCO).getValue();
    }

    public void setAgencia(Integer agencia) {
        this.getIntegerField(FIELD_AGENCIA).setValue(agencia);
    }

    public Integer getAgencia() {
        return this.getIntegerField(FIELD_AGENCIA).getValue();
    }

    public void setNumConta(String numConta) {
        this.getStringField(FIELD_NUM_CONTA).setValue(numConta);
    }

    public String getNumConta() {
        return this.getStringField(FIELD_NUM_CONTA).getValue();
    }
}


