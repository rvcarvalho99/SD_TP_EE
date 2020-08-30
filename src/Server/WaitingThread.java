package Server;

public class WaitingThread implements Runnable{
    Thread t1;
    Model model;
    String nome;
    int id;
    public WaitingThread(Thread t,Model m,String nome_PL,int mId){
        t1=t;
        model=m;
        nome=nome_PL;
        id=mId;
    }

    @Override
    public void run() {

        try{
            t1.join();

        System.out.println("acabou");
}
        catch (Exception e){}
        model.addFile(nome,id);
    }
}
