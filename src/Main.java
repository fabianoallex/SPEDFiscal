import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {
        try {
            FileWriter fileWriter = new FileWriter("c:/executaveis/teste2.txt");

            SPEDFile spedFile = new SPEDFile(fileWriter);

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

            Block bd = spedFile.addBlock("D");
            Block be = spedFile.addBlock("E");


            spedFile.totalize();

            spedFile.writeToFile();

            fileWriter.close();




        } catch (Exception e) {
            System.out.println(e);
        }
    }
}