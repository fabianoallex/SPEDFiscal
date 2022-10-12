# Estrutura do xml



```txt
definitions
  atributos:
    version:         versão do arquivo a ser gerado
  tags:
    register
      atributos:
        name:        nome do registro
        parent_type: [block|register|file]
                     tipo do registro pai
        parent:      nome do registro pai
        key          quando informado indica se o registro é referenciado em outros registros do arquivo.
                     nome do field que será utilizado como ID ao ser referenciado por outros registros.
        
      fields
        field
          atributos:
            name:        nome do campo
            pos:         posição do campo no registro
            type:        [number|string|date]
                         tipo do campo
                         tipos number são considerados inteiros se o atributo dec não for informado e double caso informado
            size:        tamanho máximo do texto no registro
            dec:         numero de casas decimais quando type = number. se em branco o tipo number será considerado inteiro
            format:      formato do campo
            description: descrição do campo conforme manual
            required     [O|OC]
                            O --> obrigatorio
                            OC --> 
            ref          quando preenchido indica o tipo do registro referenciado. 
                         o registro referenciado deve ter informado o atributo key
```

```xml
<fields>
    
</fields>
```