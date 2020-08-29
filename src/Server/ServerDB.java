package Server;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ServerDB {
    private ArrayList<Conta> contas;
    // ArrayList<Integer> idsremovidos;
    private HashMap<Integer,Musica> musicas;
    private ReentrantLock lock = new ReentrantLock();

    private Condition contasRead;
    private Condition contasWrite;
    private Condition musicascondition;


    public ServerDB(){
        contas = new ArrayList<Conta>();
        musicas = new HashMap<>();
    }

    ////////////////////// CONTA

    public ArrayList<Conta> getContas(){
        return contas;
    }

    public void addConta(Conta c){
        contas.add(c);
    }

    /////////////////////// MUSICA




    public Musica getMusica(int id){
        try {
        return musicas.get(id);
        }
        catch (Exception e){return null;}
    }

    public int getMusicasSize(){
        return musicas.size();
    }

    public int addMusica(int id, Musica m){
        musicas.put(id,m);
        return 1;
    }

    public void lock(){
        this.lock.lock();
    }

    public void unlock(){
        this.lock.unlock();
    }

}
