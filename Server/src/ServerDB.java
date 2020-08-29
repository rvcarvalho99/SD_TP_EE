
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ServerDB {
    private ArrayList<Conta> contas;
   // private ArrayList<Musica> musicas = new ArrayList<Musica>();
    private ReentrantLock lock = new ReentrantLock();

    private Condition contasRead;
    private Condition contasWrite;
    private Condition musicascondition;

    public ServerDB(){
        contas = new ArrayList<Conta>();
        /*contasRead  = lock.newCondition();
        contasWrite = lock.newCondition();*/
        musicascondition = lock.newCondition();
    }

    ////////////////////// CONTA

    public int novaConta(String nome,String pass){
        lock.lock();
        for(Conta c : contas)
        {
            if(c.getName().equals(nome)){
                lock.unlock();
                return 0;
            }
        }
        Conta c = new Conta(nome,pass);

        contas.add(c);
        lock.unlock();
        return 1;
    }

    public int checkuser(String nome, String pass){

        lock.lock();
        for(Conta c : contas)
        {
            if(c.checkuserinfo(nome,pass)){
                lock.unlock();
                return 1;
            }
        }
        lock.unlock();
        return 0;
    }


    /////////////////////// MUSICA

    /*public int novamusica(String t,String au, String an,ArrayList<String> et,byte[] bytearray){
        /*lock.lock();
        musicas.size();
        Musica c = new Musica(t,au,an,et,bytearray,musicas.size()+1);

        musicas.add(c);
        lock.unlock();
        return c.getId();
    }*/

    /*public String musicInfo(int x){
        Musica m = musicas.get(x-1);
        return m.musicInfo();
    }*/

    /*public File download(int x){
        Musica m = musicas.get(x-1);
        return m.download();
    }*/


}
