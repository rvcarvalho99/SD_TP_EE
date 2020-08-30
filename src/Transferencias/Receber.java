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


            byte bytearray[] = new byte[100000];
            int lido;
            int count = 0;
            Boolean alldone = true;
            while ((lido = inFile.read(bytearray, 0, 100000)) > 0) {
                count = count + lido;
                fos.write(bytearray, 0, lido);
            }
            System.out.println("Fim de escrita no ficheiro");



        }
        catch (Exception e){System.out.println("Erro receber");}
        fos.close();
    }

    @Override
    public void run() {
        System.out.println("receber inicio");

        try{
            receber();}
        catch (Exception e) {System.out.println("Erro receber");}
        System.out.println("receber fim");

    }
}
