import SPEDFiscal.*;

public class Main {
    public static void main(String[] args) {
        try {
            //configurações utilizadas pela classe SPEDFile aaa
            SPEDConfig config = new SPEDConfig();
            config.setDefinitionsXmlPath("C:\\Users\\User\\IdeaProjects\\SPED-efd\\src\\definitions.xml");

            SPEDGenerator spedGenerator = new SPEDGenerator(config);
            Block b0 = spedGenerator.addBlock("0");
            Register r = b0.addRegister("0005");
            b0.addRegister("0005");

            r.setFieldValue("FANTASIA", "   TESTE FANTASIA");
            r.setFieldValue("CEP", "teste cep");
            r.setFieldValue("END", "  teste END");
            r.setFieldValue("NUM", "  teste NUM");
            r.setFieldValue("COMPL", "teste COMPL");
            r.setFieldValue("BAIRRO", "  teste BAIRRO");

            r = b0.addRegister("0200");
            r.setFieldValue("COD_ITEM", "1000");
            r.setFieldValue("DESCR_ITEM", "ABACATE");

            System.out.println("0200 ID: " + r.getID());

            r = b0.addRegister("0205");
            r.setFieldValue("COD_ITEM", "1000");
            r.setFieldValue("DESCR_ANT_ITEM", "ABACATE");



            Block bc = spedGenerator.addBlock("C");
            r = bc.addRegister("C100");
            bc.addRegister("C100");

            for (int i = 0; i < 2; i++) {
                Register c590 = bc.addRegister("C590");
                Register c591 = c590.addRegister("C591");
                c591.setFieldValue("VL_FCP_OP", 2555.9933 + i);
                c591.setFieldValue("VL_FCP_ST", 2333.09 + 2*i);
            }

            Block bd = spedGenerator.addBlock("D");
            Block be = spedGenerator.addBlock("E");

            spedGenerator.totalize();



            //Exemplo com StringBuilder
            SPEDStringBuilder writer = new SPEDStringBuilder(new StringBuilder());

            //exemplo com FileWriter:
            //FileWriter fileWriter = new FileWriter("c:/executaveis/teste2.txt");
            //SPEDFileWriter writer = new SPEDFileWriter(fileWriter);


            spedGenerator.write(writer);

            //fileWriter.close();

            System.out.println(writer.stringBuilder().toString());


            /*
            |0000|||||||||||||||
            |0001|0|
            |0005|TESTE|teste cep|teste END|teste NUM|teste COMPL|teste BAIRRO||||
            |0005||||||||||
            |0990|5|
            |C001|0|
            |C100|||||||||||||||||||||||||||||
            |C100|||||||||||||||||||||||||||||
            |C590|||||||||||
            |C591|2555,99|2333,09|
            |C590|||||||||||
            |C591|2556,99|2335,09|
            |C590|||||||||||
            |C591|2557,99|2337,09|
            |C590|||||||||||
            |C591|2558,99|2339,09|
            |C590|||||||||||
            |C591|2559,99|2341,09|
            |C990|14|
            |D001|1|
            |D990|2|
            |E001|1|
            |E990|2|
            |9001|0|
            |9900|0000|1|
            |9900|9999|1|
            |9900|0001|1|
            |9900|0990|1|
            |9900|0005|2|
            |9900|C001|1|
            |9900|C990|1|
            |9900|C100|2|
            |9900|C590|5|
            |9900|C591|5|
            |9900|D001|1|
            |9900|D990|1|
            |9900|E001|1|
            |9900|E990|1|
            |9900|9001|1|
            |9900|9990|1|
            |9900|9900|17|
            |9990|20|
            |9999|43|
            *
            * */
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}