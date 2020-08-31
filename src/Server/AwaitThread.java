package Server;

import Transferencias.Enviar;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitThread implements Runnable{
    ReentrantLock lock;
    Condition musiccondition;
    Musica musica;
    String nome;
    String path;
    Socket conn;
    DataOutputStream out;
    PrintWriter outprint;
    String npl;
    public AwaitThread(ReentrantLock l, Condition c, Musica m, String p, Socket s, DataOutputStream o, PrintWriter ou, String np){
        lock=l;
        musiccondition=c;
        musica=m;
        path=p;
        conn=s;
        out=o;
        outprint=ou;
        npl=np;
    }
    @Override
    public void run() {
        lock.lock();
        try {
            Boolean firsttime = true;
            while (!musica.getdisponivel()) {
                if(firsttime){
                    firsttime=false;
                    outprint.println("O seu Download está em lista de espera (Conteúdo não disponivel).");
                }
                musiccondition.await();
            }

            lock.unlock();
            outprint.println("O seu Download vai continuar");
            String nome = Integer.toString(musica.getId());
            Enviar enviar = new Enviar(nome, conn, out, path);
            Thread t1 = new Thread(enviar);
            t1.start();
        }
        catch (InterruptedException ie){}

    }
}
