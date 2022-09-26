import java.io.FileWriter;
import java.io.PrintWriter;

class MyWriter implements Writer {
    //private final FileWriter fileWriter;
    private final PrintWriter printWriter;

    public MyWriter(FileWriter fileWriter) {
        //this.fileWriter = fileWriter;
        this.printWriter = new PrintWriter(fileWriter);
    }

    @Override
    public void write(String string) {
        this.printWriter.println(string);
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            FileWriter fileWriter = new FileWriter("c:/executaveis/teste2.txt");

            MyWriter writer = new MyWriter(fileWriter);
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

            //Register c591 = bc.addRegister("C591");
            //c591.setFieldValue("VL_FCP_OP", 2555.9933);
            //c591.setFieldValue("VL_FCP_ST", 2333.09);


            Block bd = spedFile.addBlock("D");
            Block be = spedFile.addBlock("E");


            spedFile.totalize();

            spedFile.write();

            fileWriter.close();




        } catch (Exception e) {
            System.out.println(e);
        }
    }
}