package Server;

public class WaitingThread implements Runnable{
    Thread t1;
    Model model;
    String nome;
    Musica musica;
    public WaitingThread(Thread t,Model m,String nome_PL,Musica mId){
        t1=t;
        model=m;
        nome=nome_PL;
        musica=mId;
    }

    @Override
    public void run() {

        try{
            t1.join();
}
        catch (Exception e){System.out.println("500 - Internal Server Error - WaitingThread");}
        model.addFile(nome,musica);
    }
}
