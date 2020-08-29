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
                String nome;
                String pass;
                switch (received) {
                    case -1:
                        System.out.println("sair");
                        return 0;
                    case 1:
                        System.out.println("login");
                        nome = in.readUTF();
                        pass = in.readUTF();
                        login = serverdb.checkuser(nome, pass);
                        if (login == 1) out.writeInt(1);
                        else {
                            out.writeInt(0);
                        }
                        break;
                    case 2:
                        System.out.println("registar");
                        nome = in.readUTF();
                        pass = in.readUTF();
                        System.out.println(1);
                        out.writeInt(serverdb.novaConta(nome, pass));
                        System.out.println(2);
                        break;

                }

            } catch (Exception e) {
                return 0;
            }

        }
        return 1;
    }
}
