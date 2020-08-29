package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

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
                Conta complete = autenticacao.conexao(model);
                if(complete==null) return;

                int read = in.readInt();
                ///////////////////////////////////////////////
                switch (read){
                    case 1:
                        System.out.println("Download");
                        int musicId = in.readInt();
                        File f = model.download(musicId);
                        byte [] array = Files.readAllBytes(f.toPath());
                        out.writeInt(array.length);
                        out.write(array);
                        break;
                    case 2:
                        System.out.println("Upload");
                        String titulo = in.readUTF();
                        String autor = in.readUTF();
                        int ano = in.readInt();
                        int id = model.novamusica(titulo,autor,ano);
                        out.writeInt(id);
                        byte bytearray[] = new byte[in.readInt()];
                        in.readFully(bytearray);
                        model.addFile(id,bytearray);

                        break;
                    case 3:
                        System.out.println("Criar PlayList");
                        break;
                    case 4:
                        System.out.println("Adicionar a PlayList");
                        break;
                    case 5:
                        System.out.println("Ver PlayLists");
                        break;
                    case 6:
                        System.out.println("Ver info de Musica");
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
