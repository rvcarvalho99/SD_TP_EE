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

    public void addMusic(String titulo, String autor, int ano){
        int m = musicas.size();
        Musica musica = new Musica(titulo,autor,ano,m);
        musicas.add(m,musica);
    }

    public ArrayList<String> lista2String(){
        String l2string = "";
        ArrayList<String> lst= new ArrayList<>();
        for ( Musica m : musicas){
            l2string= m.musicInfo();
            lst.add(l2string);
        }
        return lst;
    }
}
