package Server;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ServerDB {


    private HashMap<String,String> contas;
    private HashMap<String, ListadeMusicas> listas;
    private HashMap<Integer,Notificador> notificacoes;


    public ServerDB(){
        notificacoes = new HashMap<Integer, Notificador>();
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

    public void addNotificacao(Integer port, Notificador n){
        notificacoes.put(port,n);
    }

    public HashMap<Integer,Notificador> getNotificacoes(){
        return notificacoes;
    }


    public void setNotificacoes(HashMap<Integer,Notificador> n){
        notificacoes = n;
    }

    public boolean checkValidNumber(int port){
        return !notificacoes.containsKey(port);
    }

    public boolean checkExistName(String name){
        return listas.containsKey(name);
    }

    public void addLista(String nome,ListadeMusicas mus){listas.put(nome,mus);
        new File("Musica" + File.separator + nome).mkdirs();}

    public ArrayList<String> listastoString(){
        ArrayList<String> lst= new ArrayList<>();
        String l2string="";
        int i=0;
        for (Map.Entry l : listas.entrySet()) {
            l2string=("Lista " + i + ": "+l.getKey());
            lst.add(l2string);
            i++;
        }
        return lst;
    }



}
