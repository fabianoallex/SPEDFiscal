#Estrutura do xml

      fields
        field
          atributos:
            name:        no do campo
            pos:         posição do campo no registro
            type:        tipo do campo
                         valores válidos: number|string|date
                         tipos number são considerados inteiros se o atributo dec não for informado e double caso informado
            size:        tamanho máximo do texto no registro
            dec:         numero de casas decimais quando type = number. se em branco o tipo number será considerado inteiro
            format:      formato do campo
            description: descrição do campo conforme manual
            registerRef  quando preenchido indica o tipo do registro referenciado. 
                         o registro referenciado deve ter informado os atributos reffered e refferedId