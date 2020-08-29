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
    private HashMap<String, ListadeMusicas> listas;
    private ReentrantLock lock = new ReentrantLock();

    private Condition contasRead;
    private Condition contasWrite;
    private Condition musicascondition;


    public ServerDB(){
        contas = new ArrayList<Conta>();
        listas = new HashMap<>();
    }

    ////////////////////// CONTA

    public ArrayList<Conta> getContas(){
        return contas;
    }

    public void addConta(Conta c){
        contas.add(c);
    }

    /////////////////////// MUSICA

    public ListadeMusicas getLista(String nome){return listas.get(nome);}

    public void addLista(String nome,ListadeMusicas mus){listas.put(nome,mus);}


    public void lock(){
        this.lock.lock();
    }

    public void unlock(){
        this.lock.unlock();
    }

}
