function validate_dt_sit_esp(param, objMessage) {

    var dt_sit_esp = register.getDateField('DT_SIT_ESP').getValue();
    var dt_ini = register.getDateField('DT_INI').getValue();
    var dt_fin = register.getDateField('DT_FIN').getValue();

    if (dt_sit_esp.before(dt_ini)) {
        objMessage.message = 'Data da situção especial menor que a data de início ';
        return false;
    }

    if (dt_sit_esp.after(dt_fin)) {
        objMessage.message = 'Data da situção especial maior que a data de fim';
        return false;
    }

    return true;
}