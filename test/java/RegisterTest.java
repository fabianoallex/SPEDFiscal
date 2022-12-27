import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sped.core.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    //classe usada para o teste addNamedRegisterTest
    public static class NamedRegisterTest extends NamedRegister {
        public NamedRegisterTest(Context context) {
            super(context, "C101");
        }
    }

    public static class ValidationTest implements ValidationHelper {
        private boolean validate_cpf(String cpf) {
            return false;
        }

        @Override
        public boolean validate(ValidationMessage validationMessage, String methodName, String value, Register register) {
            try {
                Method method = this.getClass().getDeclaredMethod(methodName, ValidationMessage.class, String.class, Register.class);
                return (boolean) method.invoke(this, validationMessage, value, register);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean validate_cpf(ValidationMessage validationMessage, String value, Register register) {
            validationMessage.setMessage("Validação de CPF não implementada");
            return false;
        }

        public boolean validate_dates_0000(ValidationMessage validationMessage, String value, Register register) {
            Calendar cal = Calendar.getInstance();

            try {
                Date dt_ini = register.<Date>getFieldValue("DT_INI");
                Date dt_fin = register.<Date>getFieldValue("DT_FIN");

                cal.setTime(dt_ini);
                int m_dt_ini = cal.get(Calendar.MONTH);
                int y_dt_ini = cal.get(Calendar.YEAR);

                cal.setTime(dt_fin);
                int m_dt_fin = cal.get(Calendar.MONTH);
                int y_dt_fin = cal.get(Calendar.YEAR);

                if (y_dt_ini != y_dt_fin) {
                    validationMessage.setMessage("Data de início e fim devem ser no mesmo ano");
                    return false;
                }
            } catch (FieldNotFoundException e) {
                throw new RuntimeException(e);
            }

            return true;
        }

        public boolean validate_codigo_municipio(ValidationMessage validationMessage, String value, Register register) {
            //checa 7 digitos
            if (value.length() != 7) {
                validationMessage.setMessage("Código do município deve ter 7 dígitos");
                return false;
            }

            //checa se só tem numeros
            if (!value.matches("[0-9]*")) {
                validationMessage.setMessage("Código do município inválido");
                return false;
            }

            //checa se é um estado válido
            if (!value.substring(0,2).matches("12|27|13|16|29|23|53|32|52|21|31|50|51|15|25|26|22|41|33|24|11|14|43|42|28|35|17")) {
                validationMessage.setMessage("Código do município inválido. Código de Estado inexistente: %s".formatted(value.substring(0,2)));
                return false;
            }

            //checa digito verificador
            int sum = 0;
            Integer[] doublePositions = new Integer[]{1, 3, 5};
            List<Integer> list = Arrays.asList(doublePositions);
            for (int i=0; i<value.length(); i++) {
                int dig = Integer.parseInt(String.valueOf(value.charAt(i)));

                if (i==6) {
                    int dv = (10 - (sum % 10)) % 10;
                    if (dig != dv) {
                        validationMessage.setMessage("Dígito verificador do Código do Município inválido");
                        return false;
                    }
                }

                if (list.contains(i)) {
                    int doubleDig = dig * 2;
                    sum += doubleDig > 9 ? doubleDig-9 : doubleDig; //-9 --> -10 +1  ex. 9*9 = 18 --> 1+8 = 9
                } else {
                    sum += dig;
                }
            }
            return true;
        }
    }

    @Test
    @DisplayName("Teste do método validate() sem erros")
    void validateWithoutErrorsTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setValidationHelper(new ValidationTest())
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var block0 = spedGenerator.newBlockBuilder()
                .setBlockName("0")
                .setOpeningRegisterName("0001")
                .setClosureRegisterName("0990")
                .build();

        var r0000 = spedGenerator.getRegister0000().getRegister();

        try {
            r0000.setFieldValue("COD_VER", "10");
            r0000.setFieldValue("COD_FIN", "1");
            r0000.setFieldValue("DT_INI", new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2022"));
            r0000.setFieldValue("DT_FIN", new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2022"));
            r0000.setFieldValue("NOME", "JUCA");
            r0000.setFieldValue("CNPJ", "15.024.630/0001-73");
            r0000.setFieldValue("UF", "MT");
            r0000.setFieldValue("IE", "123456789");
            r0000.setFieldValue("COD_MUN", "5103403");
            r0000.setFieldValue("IND_PERFIL", "A");
            r0000.setFieldValue("IND_ATIV", "1");
        } catch (FieldNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        }


        List<String> eventsErrors = new ArrayList<>();

        r0000.validate(new ValidationListener() {
            @Override
            public void onSuccess(ValidationEvent event) {
                //todo não implementado
            }

            @Override
            public void onWarning(ValidationEvent event) {
                //todo não implementado
            }

            @Override
            public void onError(ValidationEvent event) {
                System.out.println(event.getMessage());
                eventsErrors.add(event.getMessage());
            }
        });

        //#1 - verifica se a quantidade de mensagens de erros é a esperada - esperado 0
        assertEquals(
                0,
                eventsErrors.size(),
                "#1 A quantidade de erros retornada é diferente da esperada"
        );
    }

    @Test
    @DisplayName("Teste do método validate() com erros")
    void validateWithErrorsTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var block0 = spedGenerator.newBlockBuilder()
                .setBlockName("0")
                .setOpeningRegisterName("0001")
                .setClosureRegisterName("0990")
                .build();

        var r0000 = spedGenerator.getRegister0000().getRegister();

        List<String> eventsErrors = new ArrayList<>();

        r0000.validate(new ValidationListener() {
            @Override
            public void onSuccess(ValidationEvent event) {
                //todo não implementado
            }

            @Override
            public void onWarning(ValidationEvent event) {
                //todo não implementado
            }

            @Override
            public void onError(ValidationEvent event) {
                System.out.println(event.getMessage());
                eventsErrors.add(event.getMessage());
            }
        });
        /* são esperados as seguintes mensagens de validação
        01) 0000.COD_VER: "Campo ogrigatório não informado".
        02) 0000.COD_FIN: "Campo ogrigatório não informado".
        03) 0000.DT_INI: "Campo ogrigatório não informado".
        04) 0000.DT_FIN: "Campo ogrigatório não informado".
        05) 0000.NOME: "Campo ogrigatório não informado".
        06) 0000.CNPJ: "[]: Tamanho inválido".
        07) 0000.CNPJ: "[]: Obrigatório informar CNPJ ou CPF para o Registro 0000".
        08) 0000.CPF: "[]: Obrigatório informar CNPJ ou CPF para o Registro 0000".
        09) 0000.UF: "Campo ogrigatório não informado".
        10) 0000.IE: "Campo ogrigatório não informado".
        11) 0000.COD_MUN: "Campo ogrigatório não informado".
        12) 0000.IND_PERFIL: "Campo ogrigatório não informado".
        13) 0000.IND_ATIV: "Campo ogrigatório não informado".
        * */

        //#1 - verifica se a quantidade de mensagens de erros é a esperada - esperado 15
        assertEquals(
                13,
                eventsErrors.size(),
                "#1 A quantidade de erros retornada é diferente da esperada"
        );

        //#2 - verifica se COD_VER retornou a mensagem esperada
        assertEquals(
                "0000.COD_VER: \"Campo ogrigatório não informado\".",
                eventsErrors.get(0),
                "#2 É esperado uma mensagem de erro na validação de COD_VER"
        );

        //#3 - verifica se CNPJ retornou as mensagens esperadas
        var countErrosCNPJ = eventsErrors.stream()
                .filter(s -> s.contains("0000.CNPJ"))
                .count();

        assertEquals(
                2,
                countErrosCNPJ,
                "#3 São esperadas 2 mensagens de erro na validação de CNPJ"
        );
    }

    @Test
    @DisplayName("Teste do método getID()")
    void getIDTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var block0 = spedGenerator.newBlockBuilder()
                .setBlockName("0")
                .setOpeningRegisterName("0001")
                .setClosureRegisterName("0990")
                .build();

        var r0150 = block0.addRegister("0150");

        //#1 - Testa se o campo definido como key no arquivo definitions.xml está sendo retornado no getID()
        var codParticipante = "id-1234";

        try {
            //field COD_PART foi definido como key na tag register do arquivo definitions.xml
            r0150.setFieldValue("COD_PART", codParticipante);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }

        var id1 = r0150.<String>getID();
        assertEquals(
                codParticipante,
                id1,
                "#1 Era esperado que o ID fosse o mesmo valor atribuido ao field 'COD_PART'"
        );

        //#2 - testa o retorno de getID de um registro sem key definido no arquivo definitions.xml
        var r0100 = block0.addRegister("0100");

        var id2 = r0100.<String>getID();
        assertEquals(
                null,
                id2,
                "#2 Era esperado que o ID retornado fosse null, pois não há valor definido para key no arquivo definitions.xml"
        );

    }

    @Test
    @DisplayName("Teste do método getValidationsForField")
    void getValidationsForFieldTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var r0000 = spedGenerator.getRegister0000().getRegister();
        try {
            //#1 - testa quantas validações tem o campo COD_VER - esperado 0
            assertEquals(
                    0,
                    r0000.getValidationsForField(r0000.getField("COD_VER")).size(),
                    "#1 É esperado que 'COD_VER' não tenha nenhuma validação"
            );

            //#2 - testa quantas validações tem o campo COD_VER - esperado 1
            assertEquals(
                    1,
                    r0000.getValidationsForField(r0000.getField("COD_FIN")).size(),
                    "#2 É esperado que 'COD_FIN' tenha uma validação"
            );

            //#3 - testa o nome da validação aplicada ao field COD_VER
            assertEquals(
                    "finalidade_arquivo",
                    r0000.getValidationsForField(r0000.getField("COD_FIN")).get(0).getName(),
                    "#3 É esperado que a validação do field 'COD_FIN' tenha o nome 'finalidade_arquivo'"
            );

            //#4 - testa quantas validações tem o campo COD_MUN - esperado 2
            assertEquals(
                    2,
                    r0000.getValidationsForField(r0000.getField("COD_MUN")).size(),
                    "#4 É esperado que 'COD_MUN' tenha duas validação"
            );

            //#5 - testa os nomes das validações aplicadas ao field COD_MUN
            assertEquals(
                    "codigo_municipio",
                    r0000.getValidationsForField(r0000.getField("COD_MUN")).get(0).getName(),
                    "#5 É esperado que a primeira validação do field 'COD_MUN' tenha o nome 'codigo_municipio'"
            );

            //#6 - testa os nomes das validações aplicadas ao field COD_MUN
            assertEquals(
                    "validate_codigo_municipio",
                    r0000.getValidationsForField(r0000.getField("COD_MUN")).get(1).getName(),
                    "#6 É esperado que a segunda validação do field 'COD_MUN' tenha o nome 'validate_codigo_municipio'"
            );
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Testa a inclusão de Registro filho através do método addRegister")
    void addRegisterTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var blockC = spedGenerator.newBlockBuilder()
                .setBlockName("C")
                .setOpeningRegisterName("C001")
                .setClosureRegisterName("C990")
                .build();

        //#1 — verifica se um registro adicionado via addRegister de fato está inserido nos registros filhos
        var rC100 = blockC.addRegister("C100");
        rC100.addRegister("C101");
        Counter counter = new Counter();
        rC100.count(counter);
        assertEquals(
                1,
                counter.count("C101"),
                "#1 Era esperado que o registro adicionado via método addRegister fosse considerado na contagem dos registros"
        );
    }

    @Test
    @DisplayName("Testa a inclusão de Registro filho através do método addNamedRegister")
    void addNamedRegisterTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var blockC = spedGenerator.newBlockBuilder()
                .setBlockName("C")
                .setOpeningRegisterName("C001")
                .setClosureRegisterName("C990")
                .build();

        //#1 - verifica se um registro adicionado via addNamedRegister de fato está inserido nos registros filhos
        var rC100 = blockC.addRegister("C100");

        rC100.addNamedRegister(NamedRegisterTest.class);
        Counter counter = new Counter();
        rC100.count(counter);

        assertEquals(
                1,
                counter.count("C101"),
                "#1 Era esperado que o registro adicionado via método addNamedRegister fosse considerado na contagem dos registros"
        );
    }

    @Test
    @DisplayName("teste do método getFields()")
    void getFieldsTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var blockC = spedGenerator.newBlockBuilder()
                .setBlockName("C")
                .setOpeningRegisterName("C001")
                .setClosureRegisterName("C990")
                .build();


        var rC100 = blockC.addRegister("C100");

        //#1 - verifica se os 28 fields definidos em definitions.xml serão importados
        assertEquals(
                28,
                rC100.getFields().size(),
                "#1 Deveria retornar 28 registros, conforme definitions.xml"
        );

        //#2 - verifica nome a nome se todos os fields definidos em definitions.xml podem ser encontrados
        List<String> fieldNames = Arrays.asList(
            "IND_OPER", "IND_EMIT", "COD_PART", "COD_MOD", "COD_SIT",
            "SER", "NUM_DOC", "CHV_NFE", "DT_DOC", "DT_E_S", "VL_DOC",
            "IND_PGTO", "VL_DESC", "VL_ABAT_NT", "VL_MERC", "IND_FRT",
            "VL_FRT", "VL_SEG", "VL_OUT_DA", "VL_BC_ICMS", "VL_ICMS",
            "VL_BC_ICMS_ST", "VL_ICMS_ST", "VL_IPI", "VL_PIS", "VL_COFINS",
            "VL_PIS_ST", "VL_COFINS_ST");

        fieldNames.forEach(fieldName -> {
            assertDoesNotThrow(
                    () -> rC100.getField(fieldName),
                    "#2 Field '%s' deveria ser encontrado mas não foi".formatted(fieldName)
            );
        });
    }

    @Test
    @DisplayName("teste do método toString()")
    void toStringTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var blockC = spedGenerator.newBlockBuilder()
                .setBlockName("C")
                .setOpeningRegisterName("C001")
                .setClosureRegisterName("C990")
                .build();

        //#1 - verifica se o toString retorna todos os fields como esperado
        var rC100 = blockC.addRegister("C100");
        var toStringResult = rC100.toString();

        assertEquals(
                "|C100|||||||||||||||||||||||||||||",
                toStringResult,
                "#1 a saída de toString está diferente da esperada"
        );

        //#2 - certifica que os registros filhos não sejam retornados no toString
        rC100.addRegister("C101");
        var toStringResultAfterAddSonRegister = rC100.toString();

        assertEquals(
                toStringResult,
                toStringResultAfterAddSonRegister,
                "#2 após adicionar um registro filho, toString deve continuar com a mesma saída de antes"
        );
    }

    @Test
    @DisplayName("teste do método count(Counter counter)")
    void countWithCounterParameterTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var blockC = spedGenerator.newBlockBuilder()
                .setBlockName("C")
                .setOpeningRegisterName("C001")
                .setClosureRegisterName("C990")
                .build();

        var rC100 = blockC.addRegister("C100");

        //#1 - Contagem de Registro sem registros filhos
        var counter1 = new Counter();
        rC100.count(counter1);
        assertEquals(
                1,
                counter1.count(),
                "#1 É esperado que um Registro sem Filhos retorne 1 ao contar"
        );

        //#2 - Contagem de Registro com 1 Registro filho
        var counter2 = new Counter();
        rC100.addRegister("C101");
        rC100.count(counter2);
        assertEquals(
                2,
                counter2.count(),
                "#2 É esperado que um Registro Com 1 Registro Filho retorne 2 ao contar"
        );

        //#3 - Contagem de Registro específico
        var counter3 = new Counter();
        rC100.addRegister("C101");
        rC100.count(counter3);
        assertEquals(
                2,
                counter3.count("C101"),
                "#2 É esperado que o contador retornasse 2 ao contar pelo registro específico"
        );
    }

    @Test
    @DisplayName("teste do método count()")
    void countTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var blockC = spedGenerator.newBlockBuilder()
                .setBlockName("C")
                .setOpeningRegisterName("C001")
                .setClosureRegisterName("C990")
                .build();

        var rC100 = blockC.addRegister("C100");

        //#1 - Contagem de Registro sem registros filhos
        assertEquals(
                1,
                rC100.count(),
                "#1 É esperado que um Registro sem Filhos retorne 1 ao contar"
        );

        //#2 - Contagem de Registro com 1 Registro filho
        rC100.addRegister("C101");

        assertEquals(
                2,
                rC100.count(),
                "#2 É esperado que um Registro Com 1 Registro Filho retorne 2 ao contar"
        );

        //#3 - Contagem de Registro com 2 Registros Filhos
        rC100.addRegister("C101");

        assertEquals(
                3,
                rC100.count(),
                "#3 É esperado que um Registro Com 2 Registros Filhos retorne 3 ao contar"
        );

        //#4 - Contagem de Registro com Registros Filhos que também contém Registros Filhos
        var rC110 = rC100.addRegister("C110");
        rC110.addRegister("C111");

        assertEquals(
                5, //c100 + 2xC101 + C110 + C111
                rC100.count(),
                "#4 Falha ao contar registro com registros filhos que também tem registros filhos"
        );
    }

    @Test @DisplayName("teste do método write()")
    void writeTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        var blockC = spedGenerator.newBlockBuilder()
                .setBlockName("C")
                .setOpeningRegisterName("C001")
                .setClosureRegisterName("C990")
                .build();


        var rC100 = blockC.addRegister("C100");

        //#1 — verifica se o registro c100 irá escrever - sem valores atribuidos aos fields
        AtomicReference<String> rC100Write = new AtomicReference<>();
        rC100.write((string, register) -> rC100Write.set(string));

        assertEquals(
                "|C100|||||||||||||||||||||||||||||",
                rC100Write.toString(),
                "#1 a saída de write() está diferente da esperada"
        );

        //#2 — verifica se o registro c100 irá escrever - com valores atribuidos aos fields
        List<String> fieldNames = Arrays.asList(
                "IND_OPER", "IND_EMIT", "COD_PART", "COD_MOD", "COD_SIT",
                "SER", "NUM_DOC", "CHV_NFE", "DT_DOC", "DT_E_S", "VL_DOC",
                "IND_PGTO", "VL_DESC", "VL_ABAT_NT", "VL_MERC", "IND_FRT",
                "VL_FRT", "VL_SEG", "VL_OUT_DA", "VL_BC_ICMS", "VL_ICMS",
                "VL_BC_ICMS_ST", "VL_ICMS_ST", "VL_IPI", "VL_PIS", "VL_COFINS",
                "VL_PIS_ST", "VL_COFINS_ST");

        Date dtDoc = null;
        Date dtES = null;
        try {
            dtDoc = new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2022");
            dtES = new SimpleDateFormat("dd/MM/yyyy").parse("02/12/2022");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        try {
            //fiedls setados com dados inválidos, apenas para testar a saida dos dados
            //e se todos os fields estão saindo no write.
            rC100.setFieldValue("IND_OPER", "A");
            rC100.setFieldValue("IND_EMIT", "B");
            rC100.setFieldValue("COD_PART", "C");
            rC100.setFieldValue("COD_MOD", "D");
            rC100.setFieldValue("COD_SIT", "E");
            rC100.setFieldValue("SER", "F");
            rC100.setFieldValue("NUM_DOC", "1");
            rC100.setFieldValue("CHV_NFE", "99999");
            rC100.setFieldValue("DT_DOC", dtDoc);
            rC100.setFieldValue("DT_E_S", dtES);
            rC100.setFieldValue("VL_DOC", 2.0);
            rC100.setFieldValue("IND_PGTO", "H");
            rC100.setFieldValue("VL_DESC", 3.0);
            rC100.setFieldValue("VL_ABAT_NT", 4.0);
            rC100.setFieldValue("VL_MERC", 5.0);
            rC100.setFieldValue("IND_FRT", "I");
            rC100.setFieldValue("VL_FRT", 6.0);
            rC100.setFieldValue("VL_SEG", 7.0);
            rC100.setFieldValue("VL_OUT_DA", 8.0);
            rC100.setFieldValue("VL_BC_ICMS", 9.0);
            rC100.setFieldValue("VL_ICMS", 10.0);
            rC100.setFieldValue("VL_BC_ICMS_ST", 11.0);
            rC100.setFieldValue("VL_ICMS_ST", 12.0);
            rC100.setFieldValue("VL_IPI", 13.0);
            rC100.setFieldValue("VL_PIS", 14.0);
            rC100.setFieldValue("VL_COFINS", 15.0);
            rC100.setFieldValue("VL_PIS_ST", 16.0);
            rC100.setFieldValue("VL_COFINS_ST", 17.0);
        } catch (FieldNotFoundException e) {
            throw new RuntimeException(e);
        }

        AtomicReference<String> rC100Write2 = new AtomicReference<>();
        rC100.write((string, register) -> rC100Write2.set(string));

        assertEquals(
                "|C100|A|B|C|D|E|F|1|99999|01122022|02122022|2,00|H|3,00|4,00|5,00|I|6,00|7,00|8,00|9,00|10,00|11,00|12,00|13,00|14,00|15,00|16,00|17,00|",
                rC100Write2.toString(),
                "#2 a saída de write() está diferente da esperada"
        );

        //#3 — checa se o write está retornado o registro
        AtomicReference<Register> registerRetornedFromWrite = new AtomicReference<>();
        rC100.write((string, register) -> registerRetornedFromWrite.set(register));


        assertEquals(
                rC100,
                registerRetornedFromWrite.get(),
                "#3 O objeto (Register) retornado pelo write não é o esperado"
        );

        //#4 - teste do método write ao adicionar registros filhos
        rC100.addRegister("C101");

        AtomicInteger countWrite= new AtomicInteger();
        rC100.write((string, register) -> countWrite.getAndIncrement());

        assertEquals(
                2,
                countWrite.get(),
                "#4 Era esperado a escrita de dois Registers"
        );
    }
}
