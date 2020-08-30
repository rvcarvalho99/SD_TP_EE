package Transferencias;

import Server.ListadeMusicas;
import Server.Musica;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Enviar implements Runnable {
    String nome;
    Socket conn;
    DataOutputStream out;
    String path;
    public Enviar(String n, Socket c, DataOutputStream o, String p){
        path = p;
        nome=n;
        conn=c;
        out=o;
    }

    public void enviar() throws IOException {
        if (!path.equals("")) path += "\\";
        FileInputStream fil = new FileInputStream(path + nome + ".mp3");
        try {


            byte[] bytearray = new byte[100000];
            int lido;
            int count = 0;
            while ((lido = fil.read(bytearray, 0, 100000)) > 0) {
                count = count + lido;
                out.write(bytearray, 0, lido);
            }

        }
        catch (Exception e){System.out.println("Erro a enviar");}
        finally {
            fil.close();
            conn.shutdownOutput();
            conn.shutdownInput();
            conn.close();
        }

    }

    @Override
    public void run() {
        System.out.println(11);
        try{
        enviar();}
        catch (Exception e) {System.out.println("Erro enviar");}
        System.out.println(22);
    }
}
