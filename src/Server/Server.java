import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

                Autenticacao autenticacao = new Autenticacao(out,in,serverdb);
                int complete = autenticacao.conexao();
                if(complete==0) return;

                in.readInt();
                ///////////////////////////////////////////////

            }
            catch (Exception e){}
        }

    }
}
