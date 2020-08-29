package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Musica {
    private String titulo;
    private String autor;
    private String ano;
    //private ArrayList <String> etiquetas= new ArrayList<String>();
    private ReentrantLock lock = new ReentrantLock();
    private int id;
    private int descargas=0;

    public Musica(String t,String au, String an,ArrayList<String> et,int i){
        try{

            titulo = t;
            autor = au ;
            ano = an;
            id=i;
        }
        catch (Exception e){}
    }

    public  int getId(){return id;}

    public  String getTitulo(){return titulo;}

    public String musicInfo(){
        String info = "Título: "+ titulo + "\n Autor: "+ autor + "\n Ano: " + ano +"\n Id: " + id + "\n Número de downloads: " + descargas + "\n";
        System.out.println("->" + info);
        return info;
    }

    public File download(){
        lock.lock();
        descargas++;
        lock.unlock();
        //////////////////////// apenas carregar parte do file x bytes em x bytes (bonus)
        return new File(titulo+".mp3");
    }
}
