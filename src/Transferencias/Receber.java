package Transferencias;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Receber implements Runnable {
    String nome_musica;
    Socket sock;
    DataInputStream inFile;
    String path;

    public Receber(Socket so, DataInputStream i, String n,String p){
        path=p;
        nome_musica=n;
        sock=so;
        inFile=i;
    }
    public  void receber() throws IOException {
        if(!path.equals(""))path+="\\";
        FileOutputStream fos = new FileOutputStream(path + nome_musica + ".mp3");
        try {


            byte bytearray[] = new byte[1000];
            int lido;
            while ((lido = inFile.read(bytearray, 0, 1000)) > 0) {
                fos.write(bytearray, 0, lido);
            }

        }
        catch (Exception e){System.out.println("Erro receber");}
        fos.close();
    }

    @Override
    public void run() {


        try{
            receber();
            System.out.println("Fim Download");
        }
        catch (Exception e) {System.out.println("Erro receber");}


    }
}
