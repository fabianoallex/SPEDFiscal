package sped.lcdpr.v0013;

import sped.core.NamedRegister;
import sped.core.Register;

public class ClosureRegister extends NamedRegister {
    public static String REGISTER_NAME = "9999";
    public static String FIELD_NOME_CONTADOR = "IDENT_NOME";
    public static String FIELD_CPF_CNPJ_CONTADOR = "IDENT_CPF_CNPJ";
    public static String FIELD_CRC_CONTADOR = "IND_CRC";
    public static String FIELD_EMAIL_CONTADOR = "EMAIL";
    public static String FIELD_FONE_CONTADOR = "FONE";
    public static String FIELD_QTD_LIN = "QTD_LIN";

    public ClosureRegister(Register register) {
        super(register);
    }

    public void setNomeContador(String bairro) {
        this.getStringField(FIELD_NOME_CONTADOR).setValue(bairro);
    }

    public String getNomeContador() {
        return this.getStringField(FIELD_NOME_CONTADOR).getValue();
    }

    public void setCpfCnpjContador(String bairro) {
        this.getStringField(FIELD_CPF_CNPJ_CONTADOR).setValue(bairro);
    }

    public String getCpfCnpjContador() {
        return this.getStringField(FIELD_CPF_CNPJ_CONTADOR).getValue();
    }

    public void setCrcContador(String bairro) {
        this.getStringField(FIELD_CRC_CONTADOR).setValue(bairro);
    }

    public String getCrcContador() {
        return this.getStringField(FIELD_CRC_CONTADOR).getValue();
    }

    public void setEmailContador(String bairro) {
        this.getStringField(FIELD_EMAIL_CONTADOR).setValue(bairro);
    }

    public String getEmailContador() {
        return this.getStringField(FIELD_EMAIL_CONTADOR).getValue();
    }

    public void setFoneContador(String bairro) {
        this.getStringField(FIELD_FONE_CONTADOR).setValue(bairro);
    }

    public String getFoneContador() {
        return this.getStringField(FIELD_FONE_CONTADOR).getValue();
    }

    public Integer getQuantidadeLinhas() {
        return this.getIntegerField(FIELD_QTD_LIN).getValue();
    }

    public void setQuantidadeLinhas(Integer quantidadeLinhas) {
        this.getIntegerField(FIELD_QTD_LIN).setValue(quantidadeLinhas);
    }
}
