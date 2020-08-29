package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Autenticacao {
    private ServerDB serverdb;
    private DataOutputStream out;
    private DataInputStream in;

    public Autenticacao(DataOutputStream o,DataInputStream i ,ServerDB server){
        out=o;
        in=i;
        serverdb = server;
    }

    public int conexao(){
        int login=0;
        while(login==0) {
            try {
                int received = in.readInt();

                switch (received) {
                    case -1:
                        return 0;
                    case 1:
                        String nome = in.readUTF();
                        String pass = in.readUTF();
                        login = serverdb.checkuser(nome, pass);
                        if (login == 1) out.writeInt(1);
                        else {
                            out.writeInt(0);
                        }
                        break;
                    case 2:
                        nome = in.readUTF();
                        pass = in.readUTF();
                        out.writeInt(serverdb.novaConta(nome, pass));
                        break;

                }

            } catch (Exception e) {
                return 0;
            }

        }
        return 1;
    }
}
