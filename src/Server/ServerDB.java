package Server;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ServerDB {

    RWLock lock;
    private HashMap<String,String> contas;
    private HashMap<String, ListadeMusicas> listas;



    public ServerDB(){
        lock = new RWLock();
        contas = new HashMap<>();
        listas = new HashMap<>();
    }

    ////////////////////// CONTA

    public HashMap<String,String> getContas(){
        return contas;
    }

    public void addConta(String nome, String pass){
        contas.put(nome,pass);
    }

    /////////////////////// MUSICA

    public ListadeMusicas getLista(String nome){return listas.get(nome);}

    public int listaSize(){
        return listas.size();
    }

    public void addLista(String nome,ListadeMusicas mus){listas.put(nome,mus);}

    public String listastoString(){
        String l2string="";
        int i=0;
        for (Map.Entry l : listas.entrySet()) {
            l2string+=("Lista " + i + ": "+l.getKey()) + "«»";
            i++;
        }
        return l2string;
    }



}
