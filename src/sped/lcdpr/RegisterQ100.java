package sped.lcdpr;

import sped.lib.Factory;
import sped.lib.NamedRegister;

public class RegisterQ100 extends NamedRegister {
    public static String REGISTER_NAME = "0050";
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

    public RegisterQ100(Factory factory) {
        super(factory, REGISTER_NAME);
    }
}
