package Server;

import Transferencias.Enviar;
import Transferencias.Receber;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Model {

    ServerDB serverdb;
    RWLock listaslock;
    RWLock contaslock;
    RWLock musiclock;
    RWLock notificacoeslock;
    ReentrantLock lock;
    Condition musiccondition;
    //condition para musica
    public Model(ServerDB s){
        lock = new ReentrantLock();
        musiccondition = lock.newCondition();
        contaslock = new RWLock();
        listaslock = new RWLock();
        notificacoeslock = new RWLock();
        serverdb=s;
        musiclock = new RWLock();
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


    public int addFile(String nomePL, int id ){
        try{
            lock.lock();
            listaslock.writeLock();System.out.println("1ad");
            ListadeMusicas m = serverdb.getLista(nomePL);
            Musica musica = m.getMusica(id);
            musica.lock();
            musica.setDisponivel(true);
            musica.unlock();System.out.println("2ad");

            listaslock.writeUnlock();System.out.println("2.5ad");
            musiccondition.signalAll();System.out.println("3ad");
            //musiccondition.notifyAll();
            lock.unlock();System.out.println("4ad");
            return 1;
        }
        catch (Exception ie){System.out.println(ie);}
        return 0;
    }

    public void notificador(String message){
        notificacoeslock.readLock();
        HashMap<Integer,Notificador> notificador = serverdb.getNotificacoes();

        for(Integer n : notificador.keySet()){
            if(!notificador.get(n).getSocket().isBound()) notificador.remove(n);

            notificador.get(n).getPrintwriter().println("!! NOTIFICACAO: " + message + "!!");
        }
        notificacoeslock.readUnlock();
        notificacoeslock.writeLock();
        serverdb.setNotificacoes(notificador);
        notificacoeslock.writeUnlock();
    }

    public int addNotificacao(Notificador n){
        int port=0;
        boolean valid=false;
        while (!valid) {
            port = 1000 + (new Random()).nextInt(3999); // de forma a nao calhar na porta 5000 ( porta do server)

            valid = serverdb.checkValidNumber(port);
        }
        return port;
    }

    public String getMusicName(String nomePL, int id){
        listaslock.readLock();
        ListadeMusicas m = serverdb.getLista(nomePL);

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
        listaslock.readLock();
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

    public void upload(String nomePL, int id ,Socket sock, DataInputStream inFile, String nome_musica){
        Receber receber = new Receber(sock,inFile,nome_musica,"");
        Thread t1 = new Thread(receber);
        t1.start();
        /*WaitingThread wt = new WaitingThread(t1,this,nomePL,id);
        Thread t2 = new Thread(wt);
        t2.start();*/
        try {

            t1.join();
        }
        catch (Exception e){System.out.println(e);}
        addFile(nomePL,id);
    }

    public void download(String nomePL, int id ,Socket conn, DataOutputStream out){
        try {lock.lock();
            listaslock.readLock();System.out.println("inicio download");
            ListadeMusicas m = serverdb.getLista(nomePL);
            Musica musica = m.getMusica(id);
            listaslock.readUnlock();
            //musica.lock();
            while (!musica.getdisponivel()) {
                System.out.println("nao devias estar aqui");
                musiccondition.await();
                System.out.println("Tentando again");
                musica = m.getMusica(id);
            }
            lock.unlock();
            //musica.unlock();
            System.out.println("sai...");
            String nome = musica.download();

            Enviar enviar = new Enviar(nome,conn,out,"");
            Thread t1 = new Thread(enviar);
            t1.start();
        }
        catch (InterruptedException e){}
        finally {
            try{listaslock.readUnlock();}
            catch (Exception e){}
        }
    }
}
