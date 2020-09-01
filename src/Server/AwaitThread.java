package Server;

import Transferencias.Enviar;

import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitThread implements Runnable{
    ReentrantLock lock;
    Condition musiccondition;
    Musica musica;
    String path;
    Socket conn;
    DataOutputStream out;
    PrintWriter outprint;
    String npl;
    Model model;
    RWLock listas;
    public AwaitThread(ReentrantLock l, Condition c, Musica m, String p, Socket s, DataOutputStream o, PrintWriter ou, String np, Model mo,RWLock ll){
        lock=l;
        musiccondition=c;
        musica=m;
        path=p;
        conn=s;
        out=o;
        outprint=ou;
        npl=np;
        model=mo;
        listas=ll;
    }
    @Override
    public void run() {

        listas.readLock();
        try {
            Boolean firsttime = true;
            while (!musica.getdisponivel()) {
                if(firsttime){
                    firsttime=false;
                    outprint.println("O seu Download está em lista de espera (Conteúdo não disponivel).");
                }
                listas.readUnlock();
                lock.lock();
                musiccondition.await();
                lock.unlock();
                listas.readLock();
            }


            String nome = Integer.toString(musica.getId());
            if(!firsttime)
            outprint.println("O seu Download da musica de id " + nome + " vai continuar");

            listas.readUnlock();
            Enviar enviar = new Enviar(nome, conn, out, path);
            Thread t1 = new Thread(enviar);
            t1.start();
            t1.join();
            model.downloaddone(npl,musica.getId());
        }
        catch (InterruptedException ie){System.out.println("500 - Internal Server Error - AwaitThread.java");}

    }
}
