package Server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    public static void main(String[] args) throws IOException {
            ServerDB serverdb = new ServerDB();
            System.out.println("Spotify da Candonga");
            ServerSocket socket = new ServerSocket(5000);
            while (true){
                Socket conn = socket.accept();
                new Thread(new ControladorClientes(conn,serverdb)).start();
                System.out.println("Client Connected");
            }
    }

    public static class ControladorClientes implements Runnable {

        private Socket conn;
        private ServerDB serverdb;

        public ControladorClientes(Socket c, ServerDB s) {
            conn = c;
            serverdb = s;
        }

        public void run() {

            try {
                PrintWriter out = new PrintWriter(conn.getOutputStream(),true);
                BufferedReader in =new BufferedReader(new InputStreamReader(conn.getInputStream()));

                /////////////////////////////////////////////// Login/registar
                Model model = new Model(serverdb);
                Autenticacao autenticacao = new Autenticacao(out,in,serverdb,conn);
                String user = autenticacao.conexao(model);
                if(user==null) return;
                while (true){
                int read = Integer.parseInt(in.readLine());
                ///////////////////////////////////////////////
                switch (read){
                    case 1:
                        System.out.println("Download");
                        ServerSocket socket = new ServerSocket(6000);
                        String plName = in.readLine();
                        int musicId = Integer.parseInt(in.readLine());
                        Socket connDownload = socket.accept();
                        String nomemusica = model.getMusicName(plName,musicId);

                        DataOutputStream outFile = new DataOutputStream(connDownload.getOutputStream());
                        model.download(plName,musicId,connDownload,outFile);
                        ///////////
                        //outFile.close();
                        out.println(nomemusica);
                        ///////
                        break;
                    case 2:
                        System.out.println("Upload");
                        String nome_PL = in.readLine();

                        int mId = Integer.parseInt(in.readLine());
                        if(!user.equals(model.getOwnerName(nome_PL))) {
                            out.println(0);
                            break;
                        }
                        out.println(1);
                        String nome_musica = model.getMusicName(nome_PL,mId);
                        out.println(nome_musica);
                        //////
                        byte bytearray[] = new byte[Integer.parseInt(in.readLine())];
                        DataInputStream inFile = new DataInputStream(conn.getInputStream());
                        inFile.readFully(bytearray);
                        model.addFile(nome_PL,mId,bytearray);
                        ///////
                        break;
                    case 3:
                        System.out.println("Criar PlayList");
                        String nomePL = in.readLine();
                        int numeromusicas = Integer.parseInt(in.readLine());
                        ArrayList<Musica> musicas = new ArrayList<Musica>();
                        for(int i= 0;i<numeromusicas;i++){
                            String titulo = in.readLine();
                            String autor = in.readLine();
                            int ano = Integer.parseInt(in.readLine());
                            Musica m = new Musica(titulo,autor,ano,i);
                            musicas.add(m);
                        }
                        ListadeMusicas l = new ListadeMusicas(musicas, user);
                        serverdb.addLista(nomePL,l);
                        out.println(1);

                        break;
                    case 4:
                        System.out.println("Adicionar a PlayList");
                        break;
                    case 5:
                        System.out.println("Ver PlayLists");
                        out.println(model.listasInfo());
                        break;
                    case 6:
                        System.out.println("Ver PlayList");
                        nomePL = in.readLine();
                        out.println(model.music2String(nomePL));
                        break;
                    case 7:
                        System.out.println("Alterar Password");
                        break;

                }

                }
            }
            catch (Exception e){}
        }

    }
}
