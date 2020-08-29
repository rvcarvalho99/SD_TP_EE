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
                break;
            case "2":
                System.out.println("Uplaod");
                break;
            case "3":
                System.out.println("Criar PlayList");
                break;
            case "4":
                System.out.println("Adicionar a PlayList");
                break;
            case "5":
                System.out.println("Ver PlayLists");
                break;
            case "6":
                System.out.println("Ver info de Musica");
                break;
            case "7":
                System.out.println("Alterar Password");
                break;
        }
    }
}
