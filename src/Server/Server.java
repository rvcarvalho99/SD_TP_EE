package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
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
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                DataInputStream in = new DataInputStream((conn.getInputStream()));

                /////////////////////////////////////////////// Login/registar
                Model model = new Model(serverdb);
                Autenticacao autenticacao = new Autenticacao(out,in,serverdb);
                Conta user = autenticacao.conexao(model);
                if(user==null) return;

                int read = in.readInt();
                ///////////////////////////////////////////////
                switch (read){
                    case 1:
                        System.out.println("Download");
                        String plName = in.readUTF();
                        int musicId = in.readInt();
                        String nomemusica = model.getMusicName(plName,musicId);
                        File f = model.download(plName,musicId);
                        byte [] array = Files.readAllBytes(f.toPath());
                        out.writeInt(array.length);
                        out.write(array);
                        out.writeUTF(nomemusica);
                        break;
                    case 2:
                        System.out.println("Upload");
                        String nome_PL = in.readUTF();

                        int mId = in.readInt();
                        if(!user.getName().equals(model.getOwnerName(nome_PL))) {
                            out.writeInt(0);
                            break;
                        }
                        out.writeInt(1);
                        String nome_musica = model.getMusicName(nome_PL,mId);
                        out.writeUTF(nome_musica);
                        byte bytearray[] = new byte[in.readInt()];
                        in.readFully(bytearray);
                        model.addFile(nome_PL,mId,bytearray);

                        break;
                    case 3:
                        System.out.println("Criar PlayList");
                        String nomePL = in.readUTF();
                        int numeromusicas = in.readInt();
                        ArrayList<Musica> musicas = new ArrayList<Musica>();
                        for(int i= 1;i<=numeromusicas;i++){
                            String titulo = in.readUTF();
                            String autor = in.readUTF();
                            int ano = in.readInt();
                            Musica m = new Musica(titulo,autor,ano,i);
                            musicas.add(i,m);
                        }
                        ListadeMusicas l = new ListadeMusicas(musicas, user.getName());
                        serverdb.lock();
                        serverdb.addLista(nomePL,l);
                        serverdb.unlock();
                        out.writeInt(1);

                        break;
                    case 4:
                        System.out.println("Adicionar a PlayList");
                        break;
                    case 5:
                        System.out.println("Ver PlayLists");
                        out.writeUTF(model.listasInfo());
                        break;
                    case 6:
                        System.out.println("Ver PlayList");
                        nomePL = in.readUTF();
                        out.writeUTF(model.music2String(nomePL));
                        break;
                    case 7:
                        System.out.println("Alterar Password");
                        break;

                }
            }
            catch (Exception e){}
        }

    }
}
