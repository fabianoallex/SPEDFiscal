package sped.lcdpr.v0013;

import sped.core.Context;
import sped.core.NamedRegister;

public class RegisterQ200 extends NamedRegister {
    public static String REGISTER_NAME = "Q200";
    public static String FIELD_MES_ANO = "MES";
    public static String FIELD_VAL_ENTRADA = "VL_ENTRADA";
    public static String FIELD_VAL_SAIDA = "VL_SAIDA";
    public static String FIELD_SALDO_FINAL = "SLD_FIN";
    public static String FIELD_NAT_SALDO_FINAL = "NAT_SLD_FIN";
    public static String FIELD_NAT_SALDO_FINAL_POSITIVE = "P";
    public static String FIELD_NAT_SALDO_FINAL_NEGATIVE = "N";

    public RegisterQ200(Context context) {
        super(context, REGISTER_NAME);
    }

    public void setMesAno(Integer codigoConta) {
        this.getIntegerField(FIELD_MES_ANO).setValue(codigoConta);
    }

    public Integer getMesAno() {
        return this.getIntegerField(FIELD_MES_ANO).getValue();
    }

    public void setValorEntrada(Double value) {
        this.getDoubleField(FIELD_VAL_ENTRADA).setValue(value);
    }

    public void setValorSaida(Double value) {
        this.getDoubleField(FIELD_VAL_SAIDA).setValue(value);
    }

    public void incrementValorEntrada(Double value) {
        if (this.getDoubleField(FIELD_VAL_ENTRADA).getValue() != null)
            value = value +  this.getDoubleField(FIELD_VAL_ENTRADA).getValue();

        setValorEntrada(value);

        //this.getDoubleField(FIELD_VAL_ENTRADA).setValue(value);
    }

    public void incrementValorSaida(Double value) {
        if (this.getDoubleField(FIELD_VAL_SAIDA).getValue() != null)
            value = value +  this.getDoubleField(FIELD_VAL_SAIDA).getValue();

        //this.getDoubleField(FIELD_VAL_SAIDA).setValue(value);

        setValorSaida(value);
    }

    public Double getValorLancamento() {
        return getValorEntrada() + getValorSaida();
    }

    public Double getValorEntrada() {
        return this.getDoubleField(FIELD_VAL_ENTRADA).getValue();
    }

    public Double getValorSaida() {
        return this.getDoubleField(FIELD_VAL_SAIDA).getValue();
    }

    public void setSaldoFinal(Double value) {
        this.getDoubleField(FIELD_SALDO_FINAL).setValue(Math.abs(value));
        this.getStringField(FIELD_NAT_SALDO_FINAL).setValue(
                value < 0 ? FIELD_NAT_SALDO_FINAL_NEGATIVE : FIELD_NAT_SALDO_FINAL_POSITIVE);
    }

    public Double getSaldoFinal() {
        return this.getDoubleField(FIELD_SALDO_FINAL).getValue() *
                (this.getStringField(FIELD_NAT_SALDO_FINAL).getValue().equals(FIELD_NAT_SALDO_FINAL_NEGATIVE) ? -1 : 1);
    }

    public void incrementSaldoFinal(Double value) {
        value += getSaldoFinal();
        setSaldoFinal(value);
    }
}

