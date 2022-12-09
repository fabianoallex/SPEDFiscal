function validate_cpf_ou_cnpj(cpf_cnpj, objMessage) {
    cpf_cnpj = cpf_cnpj.replace(/[^\d]+/g,'');

    if (cpf_cnpj.length == 11) {
      return validate_cpf(cpf_cnpj, objMessage);
    }

    if (cpf_cnpj.length == 14) {
       return validate_cnpj(cpf_cnpj, objMessage);
    }

    objMessage.message = 'Não foi possível determinar se é cpf ou cnpj';
    return false;
}

function validate_cpf(strCPF, objMessage) {
    if (!strCPF) return true;

    var Soma;
    var Resto;
    Soma = 0;
    if (strCPF == "00000000000") return false;

    for (i=1; i<=9; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (11 - i);
    Resto = (Soma * 10) % 11;

    if ((Resto == 10) || (Resto == 11))  Resto = 0;
    if (Resto != parseInt(strCPF.substring(9, 10)) ) {
      objMessage.message = 'CPF: Dígito verificador inválido';
      return false;
    }

    Soma = 0;
    for (i = 1; i <= 10; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (12 - i);
    Resto = (Soma * 10) % 11;

    if ((Resto == 10) || (Resto == 11))  Resto = 0;
    if (Resto != parseInt(strCPF.substring(10, 11) ) ) {
      objMessage.message = 'CPF: Dígito verificador inválido';
      return false;
    };

    return true;
}

function validate_cnpj(cnpj, objMessage) {
    cnpj = cnpj.replace(/[^\d]+/g,'');

    if (cnpj.length != 14) {
        objMessage.message = 'Tamanho inválido';
        return false;
    }

    size_p1 = cnpj.length - 2;
    cnpj_p1 = cnpj.substring(0, size_p1);
    cnpj_dv = cnpj.substring(size_p1);

    sum = 0;
    pos = size_p1 - 7;
    for (i = size_p1; i >= 1; i--) {
      sum += cnpj_p1.charAt(size_p1 - i) * pos--;
      if (pos < 2) pos = 9;
    }

    calc_dv = sum % 11 < 2 ? 0 : 11 - sum % 11;
    if (calc_dv != cnpj_dv.charAt(0)) {
        objMessage.message = 'CNPJ: Dígito verificador inválido';
        return false;
    }

    size_p1 = size_p1 + 1;
    cnpj_p1 = cnpj.substring(0, size_p1);
    sum = 0;
    pos = size_p1 - 7;
    for (i=size_p1; i>=1; i--) {
      sum += cnpj_p1.charAt(size_p1 - i) * pos--;
      if (pos < 2) pos = 9;
    }

    calc_dv = sum % 11 < 2 ? 0 : 11 - sum % 11;
    if (calc_dv != cnpj_dv.charAt(1)) {
        objMessage.message = 'CNPJ: Dígito verificador inválido';
        return false;
    }

    return true;
}
