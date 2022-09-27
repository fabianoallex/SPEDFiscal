# Estrutura do xml



```txt
fieldsDefinition
  atributos:
    type:          tipo de arquivos a ser gerado
                   valores válidos: efd-icms-ipi|efd-contrib|ecd
    version:       versão do arquivo a ser gerado
  tags:
    register
      atributos:
        name:        nome do registro
        parent_type: tipo do registro pai 
                     valores válidos: block|register|file
        parent:      nome do registro pai
        reffered     indica se o registro é referenciado em outros registros do arquivo 
                     valores válidos: true|false 
        refferedId   nome do field que será utilizado como ID ao ser referenciado por outros registros.
        
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
```

```xml
<fields>
    
</fields>
```