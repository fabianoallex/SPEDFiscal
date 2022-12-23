import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sped.core.SpedGenerator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class SpedGeneratorTest {
    @Test @DisplayName("Instanciação do spendGenerator")
    void instantiationTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        //verifica se há dois registros (0000 e 9999)
        assertEquals(
                2,
                spedGenerator.count(),
                "É esperado apenas dois registros");

        //verifica se há um registro 0000 (abertura)
        assertEquals(
                "0000",
                spedGenerator.getRegister0000().getRegister().getName(),
                "É esperado que o Registro0000 tenha o nome 0000");

        //verifica se há um registro 9999 (abertura)
        assertEquals(
                "9999",
                spedGenerator.getRegister9999().getRegister().getName(),
                "É esperado que o Registro9999 tenha o nome 9999");
    }

    @Test @DisplayName("Teste da totalizacao de registros do sped")
    void totalizeTest() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        //#1
        //antes do totalize é esperado que o total seja null
        assertEquals(
                null,
                spedGenerator.getRegister9999().getFieldRegisterCount().getValue(),
                "#1) É esperado que o total seja 2"
        );

        spedGenerator.totalize();

        //#2
        //apos o totalize é esperado que o total seja 2
        assertEquals(
                2,
                spedGenerator.getRegister9999().getFieldRegisterCount().getValue(),
                "#2) É esperado que o total seja 2"
        );

        //#3
        spedGenerator.newBlockBuilder()
                .setBlockName("C")
                .setOpeningRegisterName("C001")
                .setClosureRegisterName("C990")
                .build();

        spedGenerator.totalize();

        //apos o totalize é esperado que o total seja 2
        assertEquals(
                4,
                spedGenerator.getRegister9999().getFieldRegisterCount().getValue(),
                "#3) É esperado que o total seja 4"
        );

        //#4
        spedGenerator.generateBlock9();
        spedGenerator.totalize();

        //apos o generateBlock9 e o totalize é esperado que o total seja 13
        assertEquals(
                13,
                spedGenerator.getRegister9999().getFieldRegisterCount().getValue(),
                "#4) É esperado que o total seja 13"
        );
        /*
        |0000|||||||||||||||
        |C001|1|
        |C990|2|
        |9001|0|
        |9900|C001|1|
        |9900|C990|1|
        |9900|9001|1|
        |9900|0000|1|
        |9900|9999|1|
        |9900|9990|1|
        |9900|9900|7|
        |9990|10|
        |9999|13|
        */
    }

    @Test @DisplayName("Geração do bloco 9")
    void generateBlock9Test() {
        var spedGenerator = SpedGenerator.newBuilder("definitions.xml")
                .setFileLoader(fileName -> Objects.requireNonNull(SpedGeneratorTest.class.getResourceAsStream(fileName)))
                .build();

        //#1 - antes de gerar o bloco 9, uma tentantiva de acesso ao bloco 9 deve retornar null
        var block9 = spedGenerator.getBlock("9");
        assertNull(block9, "Era esperado que getBlock(\"9\") retornasse null antes de chamar generateBlock9()");

        //#2 - após gerar o bloco 9, deve ser retornado um objeto ao acessar o bloco 9
        spedGenerator.generateBlock9();

        block9 = spedGenerator.getBlock("9");
        assertNotNull(block9, "É esperado que após chamar generateBlock9(), seja retornado um objeto com getBlock(\"9\")");

        //#3 - verifica se o bloco possui 7 registros:
        /*
        |9001||         //abertura do bloco 9
        |9900|9001|1|   //totalizacao de registros 9001
        |9900|0000|1|   //totalizacao de registros 0000
        |9900|9999|1|   //totalizacao de registros 9999
        |9900|9990|1|   //totalizacao de registros 9990
        |9900|9900|5|   //totalizacao de registros 9900
        |9990||         //encerramento do bloco 9
        * */
        assertEquals(7, block9.count(), "#3 - count() deveria retornar 7");

        //#4 - adiciona um novo bloco e checa se a contagem estará correta
        /*
        *
        * */
        var blockC = spedGenerator.newBlockBuilder()
                .setBlockName("C")
                .setOpeningRegisterName("C001")
                .setClosureRegisterName("C990")
                .build();

        spedGenerator.generateBlock9();                 //ao gerar o bloco 9 novamente, deve-se chamar
        block9 = spedGenerator.getBlock("9"); //getBlock9 novamente para retornar a instancia atual

        assertEquals(9, block9.count(), "#4 - count() deveria retornar 9");

        //#5 — adicionar um registro ao bloco 9 e testar a contagem do bloco 9
        var rC100 = blockC.addRegister("C100");

        spedGenerator.generateBlock9();                 //ao gerar o bloco 9 novamente, deve-se chamar
        block9 = spedGenerator.getBlock("9"); //getBlock9 novamente para retornar a instancia atual

        assertEquals(10, block9.count(), "#5 - o bloco 9 deveria retornar 10 ao chamar count()");

        //#6 — ao adicionar um registro filho de outro registro, o bloco 9 deve retornar um registro a mais
        rC100.addRegister("C101");

        spedGenerator.generateBlock9();                 //ao gerar o bloco 9 novamente, deve-se chamar
        block9 = spedGenerator.getBlock("9"); //getBlock9 novamente para retornar a instancia atual

        assertEquals(11, block9.count(), "#6 - o bloco 9 deveria retornar 11 ao chamar count()");
    }
}
