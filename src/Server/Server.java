package Server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static void main(String[] args) throws IOException {
            ServerDB serverdb = new ServerDB();
            Model model = new Model(serverdb);
            System.out.println("Spotify da Candonga");
            ServerSocket socket = new ServerSocket(5000);
            while (true){
                Socket conn = socket.accept();
                new Thread(new ControladorClientes(conn,serverdb,model)).start();
                System.out.println("Client Connected");
            }
    }

    public static class ControladorClientes implements Runnable {

        private Socket conn;
        private ServerDB serverdb;
        private int portaUD=0;
        private Model model;
        public ControladorClientes(Socket c, ServerDB s,Model m) {
            conn = c;
            serverdb = s;
            model=m;
        }

        public void run() {

            try {
                PrintWriter out = new PrintWriter(conn.getOutputStream(),true);
                BufferedReader in =new BufferedReader(new InputStreamReader(conn.getInputStream()));

                /////////////////////////////////////////////// Login/registar



                Autenticacao autenticacao = new Autenticacao(out,in,serverdb,conn);
                String user = autenticacao.conexao(model);
                System.out.println(1);
                Notificador not = new Notificador(conn, out);
                System.out.println(2);
                portaUD= model.addNotificacao(not);
                System.out.println(3);
                out.println(portaUD);
                ServerSocket socket = null;
                socket = new ServerSocket(portaUD);
                if(user==null) return;
                while (true){
                int read = Integer.parseInt(in.readLine());
                ///////////////////////////////////////////////
                switch (read){
                    case 1:
                        System.out.println("Download");

                        String plName = in.readLine();
                        int musicId = Integer.parseInt(in.readLine());
                        if(model.nomeExistLista(plName)) {

                                Socket connDownload = null;
                                connDownload = socket.accept();
                                System.out.println("Conectado sucesso.");
                                DataOutputStream outFile = new DataOutputStream(connDownload.getOutputStream());

                                model.download(plName,musicId,connDownload,outFile,out);

                        }
                        else out.println("Esta PlayList não existe");
                        break;
                    case 2:
                        System.out.println("Upload");
                        String nome_PL = in.readLine();
                        int mId = Integer.parseInt(in.readLine());
                        if(model.nomeExistLista(nome_PL)) {
                            if (!user.equals(model.getOwnerName(nome_PL))) {
                                out.println("Esta PlayList não lhe pertence, logo não pode fazer upload do seu conteúdo");

                            } else {
                                try {
                                    Musica musica = model.getMusicName(nome_PL, mId);
                                    out.println(musica.getTitulo());
                                    Socket sock = null;
                                    sock = socket.accept();
                                    System.out.println("Conectado sucesso.");
                                    DataInputStream inFile = new DataInputStream(sock.getInputStream());
                                    model.upload(nome_PL, musica, sock, inFile, musica.getTitulo());
                                }
                                catch (Exception e){
                                    out.println("Forneceu um id de musica inválido");
                                }
                            }
                        }
                        else{
                            out.println("Esta PlayList não existe");
                        }
                        break;
                    case 3:
                        System.out.println("Criar PlayList");
                        String nomePL = in.readLine();
                        if(model.nomeExistLista(nomePL)){
                            int numeromusicas = Integer.parseInt(in.readLine());
                            for (int i = 0; i < numeromusicas; i++) {
                                String titulo = in.readLine();
                                String autor = in.readLine();
                                int ano = Integer.parseInt(in.readLine());
                            }
                            out.println("Nome já usado. Escolha outro nome.");
                        }
                        else {
                            int numeromusicas = Integer.parseInt(in.readLine());
                            ArrayList<Musica> musicas = new ArrayList<Musica>();
                            Musica m;
                            for (int i = 0; i < numeromusicas; i++) {
                                String titulo = in.readLine();
                                String autor = in.readLine();
                                int ano = Integer.parseInt(in.readLine());
                                m = new Musica(titulo, autor, ano, i);
                                musicas.add(i, m);
                            }

                            ListadeMusicas l = new ListadeMusicas(musicas, user);

                            serverdb.addLista(nomePL, l);
                            out.println(musicas.size() + " <-tamanho");
                        }
                        break;
                    case 4:
                        System.out.println("Adicionar a PlayList");
                        String nomepl = in.readLine();
                        if(model.nomeExistLista(nomepl)) {
                            if (!user.equals(model.getOwnerName(nomepl))) {
                                in.readLine();
                                in.readLine();
                                in.readLine();
                                out.println("Esta PlayList não lhe pertence, logo não pode fazer upload do seu conteúdo");

                            } else {
                                String titulo = in.readLine();
                                String autor = in.readLine();
                                int ano = Integer.parseInt(in.readLine());
                                model.addMusictoList(nomepl,titulo,autor,ano);
                                out.println("Musica adicionada com Sucesso");
                            }
                            }
                        else {
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
                        model.mudarPass(user,in.readLine());
                        break;

                }

                }
            }
            catch (Exception e){}
        }

    }
}
