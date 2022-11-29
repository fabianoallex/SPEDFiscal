package sped.lcdpr;

import sped.lib.Factory;
import sped.lib.NamedRegister;
import java.util.Date;

public class RegisterQ100 extends NamedRegister {
    enum TipoLancamento {
        RECEITA(1),
        DESPESA(2),
        ADIANTAMENTO(3);

        final Integer value;
        TipoLancamento(Integer value) {
            this.value = value;
        }
    };

    enum TipoDocumento {
        NOTA_FISCAL(1),
        FATURA(2),
        RECIBO(3),
        CONTRATO(4),
        FOLHA_PAGAMENTO(5),
        OUTROS(6);

        final Integer value;

        TipoDocumento(Integer value) {
            this.value = value;
        }

        public static TipoDocumento valueOf(int value) {
            for (TipoDocumento tipoDocumento : values()) {
                if (tipoDocumento.value.equals(value)) {
                    return tipoDocumento;
                }
            }

            return null;
        }
    }

    public static String REGISTER_NAME = "Q100";
    public static String FIELD_DATA = "DATA";
    public static String FIELD_COD_IMOVEL = "COD_IMOVEL";
    public static String FIELD_COD_CONTA = "COD_CONTA";
    public static String FIELD_NUM_DOC = "NUM_DOC";
    public static String FIELD_TIPO_DOC = "TIPO_DOC";
    public static String FIELD_HISTORICO = "HIST";
    public static String FIELD_ID_PARTICIPANTE = "ID_PARTIC";
    public static String FIELD_TIPO_LANC = "TIPO_LANC";
    public static String FIELD_VAL_ENTRADA = "VAL_ENTRADA";
    public static String FIELD_VAL_SAIDA = "VAL_SAIDA";
    public static String FIELD_SALDO_FINAL = "SLD_FIN";
    public static String FIELD_NAT_SALDO_FINAL = "NAT_SLD_FIN";
    public static String FIELD_NAT_SALDO_FINAL_POSITIVE = "P";
    public static String FIELD_NAT_SALDO_FINAL_NEGATIVE = "N";

    public RegisterQ100(Factory factory) {
        super(factory, REGISTER_NAME);
    }

    public void setDate(Date date) {
        this.getDateField(FIELD_DATA).setValue(date);
    }

    public Date getDate() {
        return this.getDateField(FIELD_DATA).getValue();
    }

    public void setCodigoImovel(Integer codigoImovel) {
        this.getIntegerField(FIELD_COD_IMOVEL).setValue(codigoImovel);
    }

    public Integer getCodigoImovel() {
        return this.getIntegerField(FIELD_COD_IMOVEL).getValue();
    }

    public void setCodigoConta(Integer codigoConta) {
        this.getIntegerField(FIELD_COD_CONTA).setValue(codigoConta);
    }

    public Integer getCodigoConta() {
        return this.getIntegerField(FIELD_COD_CONTA).getValue();
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.getStringField(FIELD_NUM_DOC).setValue(numeroDocumento);
    }

    public String getNumeroDocumento() {
        return this.getStringField(FIELD_NUM_DOC).getValue();
    }

    public void setTipoDocumento(TipoDocumento tipo) {
        this.getIntegerField(FIELD_TIPO_DOC).setValue(tipo.value);
    }

    public TipoDocumento getTipoDocumento() {
        return TipoDocumento.valueOf(this.getIntegerField(FIELD_TIPO_DOC).getValue());
    }

    public void setHistorico(String historico) {
        this.getStringField(FIELD_HISTORICO).setValue(historico);
    }

    public String getHistorico() {
        return this.getStringField(FIELD_HISTORICO).getValue();
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.getStringField(FIELD_ID_PARTICIPANTE).setValue(cpfCnpj);
    }

    public String getCpfCnpj() {
        return this.getStringField(FIELD_ID_PARTICIPANTE).getValue();
    }

    public void setValorLancamento(Double value, TipoLancamento tipo) {
        this.getIntegerField(FIELD_TIPO_LANC).setValue(tipo.value);
        this.getDoubleField(FIELD_VAL_ENTRADA).setValue(!tipo.equals(TipoLancamento.DESPESA) ? value : 0.00);
        this.getDoubleField(FIELD_VAL_SAIDA).setValue(tipo.equals(TipoLancamento.DESPESA) ? value : 0.00);
    }

    public void setSaldoFinal(Double value) {
        this.getDoubleField(FIELD_SALDO_FINAL).setValue(Math.abs(value));
        this.getStringField(FIELD_NAT_SALDO_FINAL).setValue(
                value < 0 ? FIELD_NAT_SALDO_FINAL_NEGATIVE : FIELD_NAT_SALDO_FINAL_POSITIVE);
    }

    public Double getValorEntrada() {
        return this.getDoubleField(FIELD_VAL_ENTRADA).getValue();
    }

    public Double getValorSaida() {
        return this.getDoubleField(FIELD_VAL_SAIDA).getValue();
    }

    public Double getSaldoFinal() {
        return this.getDoubleField(FIELD_SALDO_FINAL).getValue() *
                (this.getStringField(FIELD_NAT_SALDO_FINAL).getValue().equals(FIELD_NAT_SALDO_FINAL_NEGATIVE) ? -1 : 1);
    }
}
