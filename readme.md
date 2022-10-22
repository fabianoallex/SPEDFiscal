
# Gerador de arquivos do SPED
Classes para geração de arquivos dentro da estrutura da ECF, ECD e EFD.

A definição dos registros, nomes de campos, tipos de dados, formato, etc, devem ser feita previamente no arquivo definitions.xml.

Com isso é possível ter diferentes arquivos xml, de acordo com o tipo de escrituração e versão.

# Estrutura do xml



```txt
definitions
  tags:
    register
      atributos:
        name:        nome do registro
        parent_type: [block|register|file]
                     tipo do registro pai
                     *** para uso futuro. não tratado no momento ***
        parent:      nome do registro pai
                      *** para uso futuro. não tratado no momento ***
        key          quando informado indica se o registro é referenciado em outros registros do arquivo.
                     nome do field que será utilizado como ID ao ser referenciado por outros registros.
        
      fields
        field
          atributos:
            name:        nome do campo
            pos:         posição do campo no registro
                         *** para uso futuro. não tratado no momento ***
            type:        [number|string|date]
                         tipo do campo
                         tipos number são considerados inteiros se o atributo dec não for informado e double caso informado
            size:        tamanho máximo do texto no registro
            dec:         numero de casas decimais quando type = number. se em branco o tipo number será considerado inteiro
            format:      formato do campo
                         type string:
                            onlynumber: ao formatar o valor no arquivo, mantem apenas numeros
                         type date:
                            formatos aceitos por SimpleDateFormat()
            description: descrição do campo conforme manual
                         *** para uso futuro. não tratado no momento ***
            required     [O|OC]
                            O --> obrigatorio
                            OC --> 
                         *** para uso futuro. não tratado no momento ***
            ref          quando preenchido indica o tipo do registro referenciado. 
                         o registro referenciado deve ter informado o atributo key
```

