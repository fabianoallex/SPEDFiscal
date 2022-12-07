package sped.lcdpr.v0013;

import sped.core.Factory;
import sped.core.NamedRegister;

public class Register0040 extends NamedRegister {
    public static String REGISTER_NAME = "0040";
    public static String FIELD_CODIGO_IMOVEL = "COD_IMOVEL";
    public static String FIELD_PAIS = "PAIS";
    public static String FIELD_MOEDA = "MOEDA";
    public static String FIELD_CAD_ITR = "CAD_ITR";
    public static String FIELD_CAEPF = "CAEPF";
    public static String FIELD_IE = "INSCR_ESTADUAL";
    public static String FIELD_NOME_IMOVEL = "NOME_IMOVEL";
    public static String FIELD_ENDERECO = "ENDERECO";
    public static String FIELD_NUMERO = "NUM";
    public static String FIELD_COMPLEMENTO = "COMPL";
    public static String FIELD_BAIRRO = "BAIRRO";
    public static String FIELD_UF = "UF";
    public static String FIELD_CODIGO_MUNICIPIO = "COD_MUN";
    public static String FIELD_CEP = "CEP";
    public static String FIELD_TIPO_EXPLORACAO = "TIPO_EXPLORACAO";
    public static String FIELD_PARTICIPACAO = "PARTICIPACAO";

    public Register0040(Factory factory) {
        super(factory, REGISTER_NAME);
    }

    public void setCodigoImovel(Integer codigoImovel) {
        this.getIntegerField(FIELD_CODIGO_IMOVEL).setValue(codigoImovel);
    }

    public Integer getCodigoImovel() {
        return this.getIntegerField(FIELD_CODIGO_IMOVEL).getValue();
    }

    public void setPais(String pais) {
        this.getStringField(FIELD_PAIS).setValue(pais);
    }

    public String getPais() {
        return this.getStringField(FIELD_PAIS).getValue();
    }

    public void setMoeda(String moeda) {
        this.getStringField(FIELD_MOEDA).setValue(moeda);
    }

    public String getMoeda() {
        return this.getStringField(FIELD_MOEDA).getValue();
    }

    public void setCADITR(String caditr) {
        this.getStringField(FIELD_CAD_ITR).setValue(caditr);
    }

    public String getCADITR() {
        return this.getStringField(FIELD_CAD_ITR).getValue();
    }

    public void setCAEPF(String caepf) {
        this.getStringField(FIELD_CAEPF).setValue(caepf);
    }

    public String getCAEPF() {
        return this.getStringField(FIELD_CAEPF).getValue();
    }

    public void setIE(String ie) {
        this.getStringField(FIELD_IE).setValue(ie);
    }

    public String getIE() {
        return this.getStringField(FIELD_IE).getValue();
    }

    public void setNomeImovel(String nomeImovel) {
        this.getStringField(FIELD_NOME_IMOVEL).setValue(nomeImovel);
    }

    public String getNomeImovel() {
        return this.getStringField(FIELD_NOME_IMOVEL).getValue();
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

    public void setTipoExploracao(Integer tipoExploracao) {
        this.getIntegerField(FIELD_TIPO_EXPLORACAO).setValue(tipoExploracao);
    }

    public Integer getTipoExploracao() {
        return this.getIntegerField(FIELD_TIPO_EXPLORACAO).getValue();
    }

    public void setParticipacao(Double participacao) {
        this.getDoubleField(FIELD_PARTICIPACAO).setValue(participacao);
    }

    public Double getParticipacao() {
        return this.getDoubleField(FIELD_PARTICIPACAO).getValue();
    }
}


