
# Gerador de arquivos do SPED
Lib para geração de arquivos dentro da estrutura da ECF, ECD e EFD.

A definição dos registros, nomes de campos, tipos de dados, formato, etc, devem ser feita previamente no arquivo definitions.xml.

Com isso é possível ter diferentes arquivos xml, de acordo com o tipo de escrituração e versão.

# Estrutura do xml



```txt
definitions
  tags:
    validations:    lista de validações definidas diretamente no xml, pode ser uma function javascript
                    ou uma expressao regular
        scripts:    lista de scripts
          script:   tag script define uma funcao javascript para validacao
            atrib
              name: nome da função javascript
              file: caminho onde está o arquivo de script. padrão: [xml_path\scripts\nome_do_arquivo.js]
                    se não informado, o script será o conteudo da tag: <script><![CDATA[function x() {...}]]></script>
              
        regexs:     lista de expressoes regulares utilizadas para validar um valor
          regex:
            atrib
              name: nome que identifica a expressão regular
              expression: expressão regular
              fail_message: mensagem de erro retornada caso a validação falhe.
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
            size:        tamanho máximo do texto que será enviado ao arquivo
            dec:         numero de casas decimais quando type = number. se em branco o tipo number será considerado inteiro
            format:      formato do campo
                         type string:
                            'onlynumber': ao formatar o valor no arquivo, mantem apenas os caracters numericos da string
                         type date:
                            formatos aceitos por SimpleDateFormat()
                         type number:
                            formatos aceitos por DecimalFormat()
            description: descrição do campo conforme manual
                         *** para uso futuro. não tratado no momento ***
            required:    [O|OC]
                            O --> obrigatorio
                            OC --> obrigatorio se existir informação
                         *** para uso futuro. não tratado no momento ***
            ref:         quando preenchido indica o tipo do registro referenciado. 
                         o registro referenciado deve ter informado o atributo key
            validations: nomes da functions de validação que serão aplicadas ao field, separadas por virgula
                         para cada nome será verificado se é um exempressão regular ou um scrpit. 
                         não sendo nenhum dos dois será invocado, via reflexion, o método da classe ValidationHelper,
                         com mesmo nome, passado como parametro no construtor da Classe Definitions.
```

# exemplos
https://github.com/fabianoallex/SPEDFiscalLibExample
https://github.com/fabianoallex/SPEDFiscalLibExampleFX
