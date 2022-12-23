import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sped.core.SpedGenerator;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterTest {
    @Test
    @DisplayName("Geração do bloco 9")
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

        assertEquals(28, rC100.getFields().size(), "Deveria retornar 28 registros, conforme definitions.xml");

    }
}
