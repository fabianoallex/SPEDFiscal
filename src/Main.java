public class Main {
    public static void main(String[] args) {
        try {
            //exemplo com FileWriter:
            //FileWriter fileWriter = new FileWriter("c:/executaveis/teste2.txt");
            //MyWriterFileWriter writer = new MyWriterFileWriter(fileWriter);

            //Exemplo com StringBuilder
            SPEDStringBuilder writer = new SPEDStringBuilder(new StringBuilder());

            SPEDFile spedFile = new SPEDFile(writer);

            Block b0 = spedFile.addBlock("0");
            Register r = b0.addRegister("0005");
            b0.addRegister("0005");

            r.setFieldValue("FANTASIA", "   TESTE FANTASIA");
            r.setFieldValue("CEP", "teste cep");
            r.setFieldValue("END", "  teste END");
            r.setFieldValue("NUM", "  teste NUM");
            r.setFieldValue("COMPL", "teste COMPL");
            r.setFieldValue("BAIRRO", "  teste BAIRRO");


            Block bc = spedFile.addBlock("C");
            r = bc.addRegister("C100");
            bc.addRegister("C100");

            Register c591 = bc.addRegister("C591");
            c591.setFieldValue("VL_FCP_OP", 2555.9933);
            c591.setFieldValue("VL_FCP_ST", 2333.09);


            Block bd = spedFile.addBlock("D");
            Block be = spedFile.addBlock("E");


            spedFile.totalize();

            spedFile.write();

            //fileWriter.close();

            System.out.println(writer.stringBuilder().toString());




        } catch (Exception e) {
            System.out.println(e);
        }
    }
}