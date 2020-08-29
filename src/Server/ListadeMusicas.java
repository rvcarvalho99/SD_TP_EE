package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ListadeMusicas {
    private ArrayList<Musica> musicas;
    private ReentrantLock lock;
    public ListadeMusicas(ArrayList<Musica> m){
        musicas=m;
        lock = new ReentrantLock();
    }

    public Musica getMusica(int id){
        return musicas.get(id);
    }

}
