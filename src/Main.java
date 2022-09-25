import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try {
            FileWriter fileWriter = new FileWriter("c:/executaveis/teste2.txt");

            SPEDFile spedFile = new SPEDFile(fileWriter);

            Block b0 = spedFile.addBlock("0");
            Register r = b0.addRegister("0005");
            b0.addRegister("0005");

            r.getStringField("FANTASIA").setValue("  teste fantasia  ");
            r.getStringField("CEP").setValue("teste cep");
            r.getStringField("END").setValue("  teste END");
            r.getStringField("NUM").setValue("  teste NUM");
            r.getStringField("COMPL").setValue("teste COMPL");
            r.getStringField("BAIRRO").setValue("  teste BAIRRO");


            Block bc = spedFile.addBlock("C");
            r = bc.addRegister("C100");
            bc.addRegister("C100");


            spedFile.totalize();

            spedFile.writeToFile();

            fileWriter.close();




        } catch (Exception e) {
            System.out.println(e);
        }
    }
}