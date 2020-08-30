package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Musica {
    private String titulo;
    private String autor;
    private int ano;
    private boolean disponivel;
    //private ArrayList <String> etiquetas= new ArrayList<String>();
    private ReentrantLock lock = new ReentrantLock();
    private int id;
    private int descargas;

    public Musica(String t,String au, int an,int i){
        try{
            disponivel=false;
            titulo = t;
            autor = au ;
            ano = an;
            id=i;
            descargas=0;
        }
        catch (Exception e){}
    }

    public  int getId(){return id;}

    public  String getTitulo(){return titulo;}

    public String musicInfo(){
        String info = "Título: "+ titulo + " Autor: "+ autor + " Ano: " + ano +" Id: " + id + " Número de downloads: " + descargas + " Disponibilidade: " + disponivel;
        return info;
    }

    public boolean getdisponivel(){
        return disponivel;
    }

    public void setDisponivel(boolean disp){
        disponivel=disp;
    }

    public String download(){
        lock.lock();
        descargas++;
        lock.unlock();
        //////////////////////// apenas carregar parte do file x bytes em x bytes (bonus)
        return titulo;
    }

    public void lock(){
        this.lock.lock();
    }

    public void unlock(){
        this.lock.unlock();
    }
}
