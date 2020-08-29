package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Model {

    ServerDB serverdb;

    public Model(ServerDB s){
        serverdb=s;
    }

    /////////////////////////////////////////// Contas

    public int novaConta(String nome,String pass){
        serverdb.lock();
        ArrayList<Conta> contas = serverdb.getContas();
        for(Conta c : contas)
        {
            if(c.getName().equals(nome)){
                serverdb.unlock();
                return 0;
            }
        }
        Conta c = new Conta(nome,pass);

        serverdb.addConta(c);
        serverdb.unlock();
        return 1;
    }

    public Conta checkuser(String nome, String pass){

        serverdb.lock();
        ArrayList<Conta> contas = serverdb.getContas();
        for(Conta c : contas)
        {
            if(c.checkuserinfo(nome,pass)){
                serverdb.unlock();
                return c;
            }
        }
        serverdb.unlock();
        return null;
    }





    ////////////////////////////////////////// Musicas


    public int novaLista(String nome, ListadeMusicas musicas){
        serverdb.lock();
        serverdb.addLista(nome,musicas);
        serverdb.unlock();
        return 1;
    }

    /*public int novamusica(String t, String au, int an){
        serverdb.lock();
        int size = serverdb.getMusicasSize();
        Musica c = new Musica(t,au,an,size+1);
        c.lock();
        serverdb.addMusica(size+1,c);
        serverdb.unlock();
        return c.getId();
    }*/
    public int addFile(String nomePL, int id, byte[] bytearray){
        try{
            ListadeMusicas m = serverdb.getLista(nomePL);
            Musica musica = m.getMusica(id);
            musica.lock();
            String nome =  musica.getTitulo();
            File n = new File(nome+".mp3");
            FileOutputStream fos = new FileOutputStream(n);
            fos.write(bytearray);
            fos.close();
            musica.unlock();
        }
        catch (Exception e){}
        return 0;
    }

    public String getMusicName(String nomePL, int id){

        ListadeMusicas m = serverdb.getLista(nomePL);
        Musica musica = m.getMusica(id);
        return musica.getTitulo();
    }

    /*public String musicInfo(int x){
        Musica m = serverdb.getMusica(x);
        return m.musicInfo();
    }*/

    public File download(String nomePL,int id){
        ListadeMusicas m = serverdb.getLista(nomePL);
        Musica musica = m.getMusica(id);
        return musica.download();
    }
}
