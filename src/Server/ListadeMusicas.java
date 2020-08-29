package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ListadeMusicas {
    private ArrayList<Musica> musicas;
    private String criador;
    private ReentrantLock lock;
    public ListadeMusicas(ArrayList<Musica> m,String nome){
        criador=nome;
        musicas=m;
        lock = new ReentrantLock();
    }

    public Musica getMusica(int id){
        return musicas.get(id);
    }

    public String getName(){
        return criador;
    }

    public String lista2String(){
        String l2string = "";
        for ( Musica m : musicas){
            l2string+= m.musicInfo()+"--";
        }
        return l2string;
    }
}
