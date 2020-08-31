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



    ////////////////////////////////////////// Listas


    public int novaLista(String nome, ListadeMusicas musicas){
        listaslock.writeLock();
        serverdb.addLista(nome,musicas);
        listaslock.writeUnlock();

        return 1;
    }


    public boolean nomeExistLista(String nome){
        return  serverdb.checkExistName(nome);
    }



    /////////////////////////////////////////////////////////// Notificacoes

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
        serverdb.addNotificacao(port,n);
        return port;
    }

    ////////////////////////////////////////////////////////////// Gets

    public Musica getMusicName(String nomePL, int id){
        listaslock.readLock();
        ListadeMusicas m = serverdb.getLista(nomePL);

        try {
            Musica musica = m.getMusica(id);

            return musica;
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


    ///////////////////////////////////////////////////// Strings

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
        catch (Exception e){return "Lista inválida.";}
        finally {
            listaslock.readUnlock();
        }
    }

    /////////////////////////////////////////////////////// Transferencias

    public int addFile(String nomePL, Musica musica ){
        try{
            listaslock.writeLock();
            musica.lock();
            int id = musica.getId();
            musica.setDisponivel(true);
            lock.lock();
            musiccondition.signalAll();
            lock.unlock();
            musica.unlock();
            listaslock.writeUnlock();
            notificador("PlayList: " + nomePL + ". Id da Musica: " + id);
            return 1;
        }
        catch (Exception ie){System.out.println(ie);}
        return 0;
    }

    public void upload(String nomePL, Musica id ,Socket sock, DataInputStream inFile, String nome_musica){

        int i = id.getId();
        Receber receber = new Receber(sock,inFile,Integer.toString(i),"Musica\\" + nomePL);
        Thread t1 = new Thread(receber);
        t1.start();
        WaitingThread wt = new WaitingThread(t1,this,nomePL,id);
        Thread wthread = new Thread(wt);
        wthread.start();
    }

    public void download(String nomePL, int id ,Socket conn, DataOutputStream out,PrintWriter outprint){

            listaslock.readLock();
            ListadeMusicas m = serverdb.getLista(nomePL);
            Musica musica = m.getMusica(id);
            listaslock.readUnlock();
            AwaitThread aw = new AwaitThread(lock,musiccondition,musica,"Musica\\" + nomePL,conn,out,outprint,nomePL);
            Thread t2= new Thread(aw);
            t2.start();


    }


    /////////////////////////////////////////////////////////
}
