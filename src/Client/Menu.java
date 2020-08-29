package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Menu {
    private BufferedReader input;
    private DataInputStream in;
    private DataOutputStream out;

    public Menu(BufferedReader b, DataInputStream i, DataOutputStream o){
        in=i;
        out=o;
        input=b;
    }

    public void show() throws IOException {
    String read = input.readLine();
        ///////////////////////////////////////////////
        switch (read){
            case "1":
                System.out.println("Download");
                out.writeInt(1);
                break;
            case "2":
                System.out.println("Upload");
                out.writeInt(2);

                System.out.println("Titulo:");
                String titulo = input.readLine();
                out.writeUTF(titulo);

                System.out.println("Artista:");
                String artista = input.readLine();
                out.writeUTF(artista);

                System.out.println("Ano:");
                //try catch caso se faça merda no ano
                int ano = Integer.parseInt(input.readLine());
                out.writeInt(ano);

                System.out.println("Path:");
                String path = input.readLine();

                //enviar o ficheiro

                break;
            case "3":
                System.out.println("Criar PlayList");
                out.writeInt(3);
                break;
            case "4":
                System.out.println("Adicionar a PlayList");
                out.writeInt(4);
                break;
            case "5":
                System.out.println("Ver PlayLists");
                out.writeInt(5);
                break;
            case "6":
                out.writeInt(6);
                System.out.println("Ver info de Musica");
                break;
            case "7":
                out.writeInt(7);
                System.out.println("Alterar Password");

                break;
        }
    }
}
