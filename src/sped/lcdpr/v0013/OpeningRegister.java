package sped.lcdpr.v0013;

import sped.core.NamedRegister;
import sped.core.Register;

import java.util.Date;

public class OpeningRegister extends NamedRegister {

    public enum SituacaoEspecial {
        NORMAL(0),
        FALECIMENTO(1),
        ESPOLIO(2),
        SAIDA_PAIS(3);

        final Integer value;
        SituacaoEspecial(Integer value) {
            this.value = value;
        }

        public static OpeningRegister.SituacaoEspecial valueOf(int value) {
            for (OpeningRegister.SituacaoEspecial situacaoEspecial : values())
                if (situacaoEspecial.value.equals(value))
                    return situacaoEspecial;

            return null;
        }
    };

    public enum SituacaoInicioPeriodo {
        REGULAR(0),
        ABERTURA(1),
        INICIO_OBRIGATORIEDADE(2);

        final Integer value;
        SituacaoInicioPeriodo(Integer value) {
            this.value = value;
        }

        public static OpeningRegister.SituacaoInicioPeriodo valueOf(int value) {
            for (OpeningRegister.SituacaoInicioPeriodo situacaoInicioPeriodo : values())
                if (situacaoInicioPeriodo.value.equals(value))
                    return situacaoInicioPeriodo;

            return null;
        }
    };

    public static String REGISTER_NAME = "0000";
    public static String FIELD_NOME_ESCRITURACAO = "NOME_ESC";
    public static String FIELD_NOME_ESCRITURACAO_VALUE = "LCDPR";
    public static String FIELD_CODIGO_VERSAO = "COD_VER";
    public static String FIELD_CODIGO_VERSAO_VALUE = "0013";
    public static String FIELD_CPF = "CPF";
    public static String FIELD_NOME = "NOME";
    public static String FIELD_INDICADOR_SITUACAO_INI_PER = "IND_SIT_INI_PER";
    public static String FIELD_SITUACAO_ESPECIAL = "SIT_ESPECIAL";
    public static String FIELD_DATA_SITUACAO_ESPECIAL = "DT_SIT_ESP";
    public static String FIELD_DATA_INICIAL = "DT_INI";
    public static String FIELD_DATA_FINAL = "DT_FIN";

    public OpeningRegister(Register register) {
        super(register);
        this.getStringField(FIELD_NOME_ESCRITURACAO).setValue(FIELD_NOME_ESCRITURACAO_VALUE);
        this.getStringField(FIELD_CODIGO_VERSAO).setValue(FIELD_CODIGO_VERSAO_VALUE);
    }

    public void setCodigoVersao(String bairro) {
        this.getStringField(FIELD_CODIGO_VERSAO).setValue(bairro);
    }

    public String getCodigoVersao() {
        return this.getStringField(FIELD_CODIGO_VERSAO).getValue();
    }

    public void setCpf(String bairro) {
        this.getStringField(FIELD_CPF).setValue(bairro);
    }

    public String getCpf() {
        return this.getStringField(FIELD_CPF).getValue();
    }

    public void setNome(String bairro) {
        this.getStringField(FIELD_NOME).setValue(bairro);
    }

    public String getNome() {
        return this.getStringField(FIELD_NOME).getValue();
    }

    public void setSituacaoInicioPeriodo(SituacaoInicioPeriodo situacaoInicioPeriodo) {
        this.getIntegerField(FIELD_INDICADOR_SITUACAO_INI_PER).setValue(situacaoInicioPeriodo.value);
    }

    public SituacaoInicioPeriodo getSituacaoInicioPeriodo() {
        return SituacaoInicioPeriodo.valueOf(this.getIntegerField(FIELD_INDICADOR_SITUACAO_INI_PER).getValue());
    }

    public void setSituacaoEspecial(SituacaoEspecial situacaoEspecial) {
        this.getIntegerField(FIELD_SITUACAO_ESPECIAL).setValue(situacaoEspecial.value);
    }

    public SituacaoEspecial getSituacaoEspecial() {
        return SituacaoEspecial.valueOf(this.getIntegerField(FIELD_SITUACAO_ESPECIAL).getValue());
    }

    public void setDataSituacaoEspecial(Date date) {
        this.getDateField(FIELD_DATA_SITUACAO_ESPECIAL).setValue(date);
    }

    public Date getDataSituacaoEspecial() {
        return this.getDateField(FIELD_DATA_SITUACAO_ESPECIAL).getValue();
    }

    public void setInicialDate(Date date) {
        this.getDateField(FIELD_DATA_INICIAL).setValue(date);
    }

    public Date getInitialDate() {
        return this.getDateField(FIELD_DATA_INICIAL).getValue();
    }

    public void setFinalDate(Date date) {
        this.getDateField(FIELD_DATA_FINAL).setValue(date);
    }

    public Date getFinalDate() {
        return this.getDateField(FIELD_DATA_FINAL).getValue();
    }
}
