package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    public static void main(String[] args) throws IOException {

        Model model = new Model();
        System.out.println("Spotify da Candonga");
        ServerSocket socket = new ServerSocket(65000);
        while (true) {
            Socket conn = socket.accept();
            new Thread(new ControladorClientes(conn, model)).start();
            System.out.println("Client Connected");
        }
    }

    public static class ControladorClientes implements Runnable {

        private Socket conn;
        private int portaUD = 0;
        private Model model;

        public ControladorClientes(Socket c, Model m) {
            conn = c;
            model = m;
        }

        public void run() {

            try {
                PrintWriter out = new PrintWriter(conn.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                /////////////////////////////////////////////// Login/registar


                Autenticacao autenticacao = new Autenticacao(out, in, model);
                String user = autenticacao.conexao();
                if (user.equals("")) return;
                out.println(1);
                Notificador not = new Notificador(conn, out);
                portaUD = model.addNotificacao(not);
                out.println(portaUD);
                ServerSocket socket = new ServerSocket(portaUD);
                if (user == null) return;
                while (true) {
                    int read = Integer.parseInt(in.readLine());

                    ///////////////////////////////////////////////
                    switch (read) {
                        case 1:
                            System.out.println("Download");
                            boolean pass=true;
                            String plName = in.readLine();
;                            if(plName.equals("quit")) break;

                            int musicId = Integer.parseInt(in.readLine());
                            if(musicId==-1) pass=false;
                            if (model.nomeExistLista(plName)) {
                                try {

                                    Musica musica = model.getMusicName(plName, musicId);
                                    Socket connDownload = socket.accept();
                                    System.out.println("Conectado sucesso.");
                                    DataOutputStream outFile = new DataOutputStream(connDownload.getOutputStream());

                                    model.download(plName, musicId, connDownload, outFile, out);
                                } catch (Exception e) {
                                    if(musicId>=0)
                                    out.println("Forneceu um id de musica inválido");
                                }
                            } else if(pass) out.println("Esta PlayList não existe");
                            break;
                        case 2:
                            System.out.println("Upload");
                            String nome_PL = in.readLine();
                            if(nome_PL.equals("quit")) break;
                            boolean pas=true;
                            int mId = Integer.parseInt(in.readLine());
                            if(mId==-1) pas=false;
                            if (model.nomeExistLista(nome_PL)) {
                                if (!user.equals(model.getOwnerName(nome_PL))) {
                                    out.println("Esta PlayList não lhe pertence, logo não pode fazer upload do seu conteúdo");

                                } else {
                                    try {
                                        Musica musica = model.getMusicName(nome_PL, mId);
                                        Socket sock = socket.accept();
                                        System.out.println("Conectado sucesso.");
                                        DataInputStream inFile = new DataInputStream(sock.getInputStream());
                                        model.upload(nome_PL, musica, sock, inFile, musica.getTitulo());
                                    } catch (Exception e) {
                                        if(mId>=0)
                                            out.println("Forneceu um id de musica inválido");
                                    }
                                }
                            } else {
                                if(pas)out.println("Esta PlayList não existe");
                            }
                            break;
                        case 3:
                            System.out.println("Criar PlayList");
                            String nomePL = in.readLine();
                            if(nomePL.equals("quit")) break;
                            if (model.nomeExistLista(nomePL)) {
                                int numeromusicas = Integer.parseInt(in.readLine());
                                for (int i = 0; i < numeromusicas; i++) {
                                    in.readLine();
                                    in.readLine();
                                    in.readLine();
                                    in.readLine();
                                }
                                out.println("Nome já usado. Escolha outro nome.");
                            } else {
                                int numeromusicas = Integer.parseInt(in.readLine());
                                ArrayList<Musica> musicas = new ArrayList<>();
                                HashMap<Musica, Socket> toupload = new HashMap<>();

                                ListadeMusicas l = new ListadeMusicas(musicas, user);
                                model.novaLista(nomePL, l);
                                for (int i = 0; i < numeromusicas; i++) {
                                    String titulo = in.readLine();
                                    String autor = in.readLine();
                                    int ano = Integer.parseInt(in.readLine());

                                    Musica m = model.addMusica(new Musica(titulo, autor, ano, i),nomePL);
                                    int upload = Integer.parseInt(in.readLine());
                                    if (upload == 1) {
                                        Socket sock = socket.accept();
                                        System.out.println("Conectado sucesso.");
                                        DataInputStream inFile = new DataInputStream(sock.getInputStream());
                                        model.upload(nomePL, musicas.get(i), sock, inFile, titulo);
                                    }
                                }

                            }
                            break;
                        case 4:
                            System.out.println("Adicionar a PlayList");
                            String nomepl = in.readLine();
                            if(nomepl.equals("quit")) break;
                            if (model.nomeExistLista(nomepl)) {
                                if (!user.equals(model.getOwnerName(nomepl))) {
                                    in.readLine();
                                    in.readLine();
                                    in.readLine();
                                    out.println("Esta PlayList não lhe pertence, logo não pode fazer upload do seu conteúdo");

                                } else {
                                    String titulo = in.readLine();
                                    String autor = in.readLine();
                                    int ano = Integer.parseInt(in.readLine());
                                    model.addMusictoList(nomepl, titulo, autor, ano);
                                    out.println("Musica adicionada com Sucesso");
                                }
                            } else {
                                in.readLine();
                                in.readLine();
                                in.readLine();
                                out.println("Esta PlayList não existe");
                            }
                            break;
                        case 5:
                            System.out.println("Ver PlayLists");

                            ArrayList<String> lst = model.listasInfo();
                            for (String l : lst) {
                                out.println(l);
                            }
                            break;
                        case 6:
                            System.out.println("Ver PlayList");
                            nomePL = in.readLine();
                            ArrayList<String> mc = model.music2String(nomePL);
                            for (String l : mc) {
                                out.println(l);
                            }
                            break;
                        case 7:
                            System.out.println("Alterar Password");
                            model.mudarPass(user, in.readLine());
                            break;

                    }

                }
            } catch (Exception e) {
                try {
                    conn.shutdownOutput();
                    conn.shutdownInput();
                    conn.close();
                    model.notificador("");
                    System.out.println("Client out");
                } catch (Exception es) {
                    System.out.println(" 500 - Internal server error");
                }
            }
        }

    }
}
