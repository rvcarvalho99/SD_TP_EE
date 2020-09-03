package Server;

import Transferencias.Receber;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Model {

    private ServerDB serverdb;
    private RWLock listaslock;
    private RWLock contaslock;
    private RWLock notificacoeslock;
    private ReentrantLock lock;
    private Condition musiccondition;

    //condition para musica
    public Model() {
        lock = new ReentrantLock();
        musiccondition = lock.newCondition();
        contaslock = new RWLock();
        listaslock = new RWLock();
        notificacoeslock = new RWLock();
        serverdb = new ServerDB();
    }

    /////////////////////////////////////////// contas

    public int novaConta(String nome, String pass) {

        contaslock.writeLock();
        HashMap<String, String> contas = serverdb.getContas();
        if (contas.containsKey(nome)) {
            contaslock.writeUnlock();
            return 0;
        }
        serverdb.addConta(nome, pass);
        contaslock.writeUnlock();
        return 1;
    }

    public String checkuser(String nome, String pass) {
        contaslock.readLock();
        HashMap<String, String> contas = serverdb.getContas();
        if (contas.containsKey(nome)) {
            if (contas.get(nome).equals(pass)) {
                contaslock.readUnlock();
                return nome;
            }
            contaslock.readUnlock();
            return "";
        }
        contaslock.readUnlock();
        return "";
    }

    public void mudarPass(String nome, String pass) {
        contaslock.writeLock();
        serverdb.addConta(nome, pass);
        contaslock.writeUnlock();
    }


    ////////////////////////////////////////// listas

    public void addMusictoList(String nomepl, String titulo, String autor, int ano) {
        listaslock.writeLock();
        ListadeMusicas m = serverdb.getLista(nomepl);
        m.addMusic(titulo, autor, ano);
        listaslock.writeUnlock();
    }

    public int novaLista(String nome, ListadeMusicas musicas) {
        listaslock.writeLock();
        serverdb.addLista(nome, musicas);
        listaslock.writeUnlock();

        return 1;
    }


    public boolean nomeExistLista(String nome) {
        try {
            listaslock.readLock();
            return serverdb.checkExistName(nome);
        } finally {
            listaslock.readUnlock();
        }
    }


    public Musica addMusica(Musica m, String nomepl){
        listaslock.writeLock();
        ListadeMusicas l = serverdb.getLista(nomepl);
        l.addM(m);
        listaslock.writeUnlock();
        return m;
    }
    ////////////////////////////////////////// notificacoes
    public void notificador(String message) {

        notificacoeslock.writeLock();
        HashMap<Integer, Notificador> notificador = serverdb.getNotificacoes();
        ArrayList<Integer> i = new ArrayList<>();
        if (!message.equals("")) {
            for (Integer n : notificador.keySet()) {
                if (!notificador.get(n).getSocket().isConnected() || notificador.get(n).getSocket().isClosed()) {
                    i.add(n);
                } else
                    notificador.get(n).getPrintwriter().println("!! NOTIFICACAO: " + message + "!!");
            }
            for (Integer n : i) {
                notificador.remove(n);
            }

            serverdb.setNotificacoes(notificador);

        }
        notificacoeslock.writeUnlock();

    }

    public int addNotificacao(Notificador n) {
        notificacoeslock.writeLock();
        int port = 0;
        boolean valid = false;
        while (!valid) {
            port = 1000 + (new Random()).nextInt(63999);
            if(port!=5000)
            valid = serverdb.checkValidNumber(port);
        }
        serverdb.addNotificacao(port, n);
        notificacoeslock.writeUnlock();
        return port;
    }

    ////////////////////////////////////////// gets

    public Musica getMusicName(String nomePL, int id) {
        listaslock.readLock();
        ListadeMusicas m = serverdb.getLista(nomePL);

        try {
            Musica musica = m.getMusica(id);

            return musica;
        } finally {
            listaslock.readUnlock();
        }
    }

    public String getOwnerName(String nomePL) {

        try {
            listaslock.readLock();
            ListadeMusicas m = serverdb.getLista(nomePL);
            return m.getName();
        } finally {
            listaslock.readUnlock();
        }
    }

    ////////////////////////////////////////// info

    public ArrayList<String> listasInfo() {//////////////////alterar
        listaslock.readLock();
        try {
            return serverdb.listastoString();
        } finally {
            listaslock.readUnlock();
        }
    }

    public ArrayList<String> music2String(String nome) {//////////////////alterar
        try {
            listaslock.readLock();
            ListadeMusicas m = serverdb.getLista(nome);
            return m.lista2String();
        } catch (Exception e) {
            ArrayList<String> s = new ArrayList<>();
            s.add("PlayList invalida");
            return s;
        } finally {
            listaslock.readUnlock();
        }
    }

    ////////////////////////////////////////// transferencias

    public void downloaddone(String nmp, int id) {
        listaslock.writeLock();
        ListadeMusicas m = serverdb.getLista(nmp);
        Musica musica = m.getMusica(id);
        musica.download();
        listaslock.writeUnlock();
    }

    public int addFile(String nomePL, Musica musica) {
        try {
            listaslock.writeLock();
            musica.lock();
            int id = musica.getId();
            musica.setDisponivel(true);
            lock.lock();
            musiccondition.signalAll();
            lock.unlock();
            musica.unlock();
            listaslock.writeUnlock();
            notificador("PlayList: " + nomePL + ". Id da Musica: " + id);
            return 1;
        } catch (Exception ie) {
            System.out.println(ie);
        }
        return 0;
    }

    public void upload(String nomePL, Musica id, Socket sock, DataInputStream inFile, String nome_musica) {
        listaslock.readLock();
        int i = id.getId();
        listaslock.readUnlock();
        Receber receber = new Receber(sock, inFile, Integer.toString(i), "Musica\\" + nomePL);
        Thread t1 = new Thread(receber);

        t1.start();
        WaitingThread wt = new WaitingThread(t1, this, nomePL, id);
        Thread wthread = new Thread(wt);
        wthread.start();
    }

    public void download(String nomePL, int id, Socket conn, DataOutputStream out, PrintWriter outprint) {

        listaslock.readLock();
        ListadeMusicas m = serverdb.getLista(nomePL);
        Musica musica = m.getMusica(id);
        listaslock.readUnlock();
        AwaitThread aw = new AwaitThread(lock, musiccondition, musica, "Musica\\" + nomePL, conn, out, outprint, nomePL, this, listaslock);
        Thread t2 = new Thread(aw);
        t2.start();


    }
}
