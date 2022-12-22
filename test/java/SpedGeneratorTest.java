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

}
