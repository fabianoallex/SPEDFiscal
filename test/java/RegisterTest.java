import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sped.core.FieldNotFoundException;
import sped.core.SpedGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    @Test
    @DisplayName("getFields")
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
                    "#2 É esperado que todos os fiels sejam encontrados"
            );
        });
    }
}
