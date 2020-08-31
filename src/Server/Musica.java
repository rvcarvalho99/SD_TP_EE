package Server;


import java.util.concurrent.locks.ReentrantLock;

public class Musica {
    private String titulo;
    private String autor;
    private int ano;
    private boolean disponivel;
    private ReentrantLock lock = new ReentrantLock();
    private int id;
    private int descargas;

    public Musica(String t,String au, int an,int i){

            disponivel=false;
            titulo = t;
            autor = au ;
            ano = an;
            id=i;
            descargas=0;

    }

    public  int getId(){return id;}

    public  String getTitulo(){return titulo;}

    public String musicInfo(){
        return "Título: "+ titulo + " Autor: "+ autor + " Ano: " + ano +" Id: " + id + " Número de downloads: " + descargas + " Disponibilidade: " + disponivel;
    }

    public boolean getdisponivel(){
        return disponivel;
    }

    public void setDisponivel(boolean disp){
        disponivel=disp;
    }

    public void download(){
        lock.lock();
        descargas++;
        lock.unlock();
    }

    public void lock(){
        this.lock.lock();
    }

    public void unlock(){
        this.lock.unlock();
    }
}
