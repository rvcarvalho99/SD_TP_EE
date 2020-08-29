package Server;

import com.sun.org.apache.bcel.internal.generic.RET;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Model {

    ServerDB serverdb;
    RWLock listaslock;
    RWLock contaslock;
    public Model(ServerDB s){
        contaslock = new RWLock();
        listaslock = new RWLock();
        serverdb=s;

    }

    /////////////////////////////////////////// Contas

    public int novaConta(String nome,String pass){

        contaslock.writeLock();
        HashMap<String,String> contas = serverdb.getContas();
        if(contas.containsKey(nome))
        {
            contaslock.writeUnlock();
            return 0;
        }
        serverdb.addConta(nome,pass);
        contaslock.writeUnlock();
        return 1;
    }

    public String checkuser(String nome, String pass){
        contaslock.readLock();
        HashMap<String,String> contas = serverdb.getContas();
        if(contas.containsKey(nome))
        {
            if(contas.get(nome).equals(pass)){
                contaslock.readUnlock();
                return nome;
            }
            contaslock.readUnlock();
            return "";
        }
        contaslock.readUnlock();
        return "";
    }





    ////////////////////////////////////////// Musicas


    public int novaLista(String nome, ListadeMusicas musicas){
        listaslock.writeLock();
        serverdb.addLista(nome,musicas);
        listaslock.writeUnlock();
        return 1;
    }


    public int addFile(String nomePL, int id, byte[] bytearray){
        try{
            listaslock.writeLock();
            ListadeMusicas m = serverdb.getLista(nomePL);
            Musica musica = m.getMusica(id);
            musica.lock();
            String nome =  musica.getTitulo();
            File n = new File(nome+".mp3");
            FileOutputStream fos = new FileOutputStream(n);
            fos.write(bytearray);
            fos.close();
            musica.setDisponivel(true);
            musica.unlock();
            listaslock.writeUnlock();
            return 1;
        }
        catch (Exception e){}
        return 0;
    }

    public String getMusicName(String nomePL, int id){
        listaslock.readLock();
        ListadeMusicas m = serverdb.getLista(nomePL);
        listaslock.writeUnlock();
        try {
            Musica musica = m.getMusica(id);

            return musica.getTitulo();
        }
        finally {
            listaslock.readUnlock();
        }
    }

    public String getOwnerName(String nomePL ) {

        try{
            listaslock.readLock();
            ListadeMusicas m = serverdb.getLista(nomePL);
            return m.getName();
        }
        finally {
            listaslock.readUnlock();
        }
    }

    public int getListaSize(){
        try{
            listaslock.readLock();
            return serverdb.listaSize();
        }
        finally {
            listaslock.readUnlock();
        }
    }

    public String listasInfo(){
        listaslock.readUnlock();
        try {
            return serverdb.listastoString();
        }
        finally {
            listaslock.readUnlock();
        }
    }

    public String music2String(String nome){
        try {
            listaslock.readLock();
            ListadeMusicas m = serverdb.getLista(nome);
            return m.lista2String();
        }
        finally {
            listaslock.readUnlock();
        }
    }

    public File download(String nomePL,int id){
        try {
            listaslock.readLock();
            ListadeMusicas m = serverdb.getLista(nomePL);
            Musica musica = m.getMusica(id);
            musica.lock();
            if(musica.getdisponivel())
            return musica.download();
            return null;
        }
        finally {
            listaslock.readUnlock();
        }
    }
}
