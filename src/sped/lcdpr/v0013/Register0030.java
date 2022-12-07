package sped.lcdpr.v0013;

import sped.core.Factory;
import sped.core.NamedRegister;

public class Register0030 extends NamedRegister {
    public static String REGISTER_NAME = "0030";
    public static String FIELD_ENDERECO = "ENDERECO";
    public static String FIELD_NUMERO = "NUM";
    public static String FIELD_COMPLEMENTO = "COMPL";
    public static String FIELD_BAIRRO = "BAIRRO";
    public static String FIELD_UF = "UF";
    public static String FIELD_CODIGO_MUNICIPIO = "COD_MUN";
    public static String FIELD_CEP = "CEP";
    public static String FIELD_TELEFONE = "NUM_TEL";
    public static String FIELD_EMAIL = "EMAIL";

    public Register0030(Factory factory) {
        super(factory, REGISTER_NAME);
    }

    public void setEndereco(String endereco) {
        this.getStringField(FIELD_ENDERECO).setValue(endereco);
    }

    public String getEndereco() {
        return this.getStringField(FIELD_ENDERECO).getValue();
    }

    public void setNumero(String numero) {
        this.getStringField(FIELD_NUMERO).setValue(numero);
    }

    public String getNumero() {
        return this.getStringField(FIELD_NUMERO).getValue();
    }

    public void setComplemento(String complemento) {
        this.getStringField(FIELD_COMPLEMENTO).setValue(complemento);
    }

    public String getComplemento() {
        return this.getStringField(FIELD_COMPLEMENTO).getValue();
    }

    public void setBairro(String bairro) {
        this.getStringField(FIELD_BAIRRO).setValue(bairro);
    }

    public String getBairro() {
        return this.getStringField(FIELD_BAIRRO).getValue();
    }

    public void setUF(String uf) {
        this.getStringField(FIELD_UF).setValue(uf);
    }

    public String getUF() {
        return this.getStringField(FIELD_UF).getValue();
    }

    public void setCodigoMunicipio(Integer codigoMunicipio) {
        this.getIntegerField(FIELD_CODIGO_MUNICIPIO).setValue(codigoMunicipio);
    }

    public Integer getCodigoMunicipio() {
        return this.getIntegerField(FIELD_CODIGO_MUNICIPIO).getValue();
    }

    public void setCep(String cep) {
        this.getStringField(FIELD_CEP).setValue(cep);
    }

    public String getCep() {
        return this.getStringField(FIELD_CEP).getValue();
    }

    public void setTelefone(String endereco) {
        this.getStringField(FIELD_TELEFONE).setValue(endereco);
    }

    public String getTelefone() {
        return this.getStringField(FIELD_TELEFONE).getValue();
    }

    public void setEmail(String email) {
        this.getStringField(FIELD_EMAIL).setValue(email);
    }

    public String getEmail() {
        return this.getStringField(FIELD_EMAIL).getValue();
    }
}


