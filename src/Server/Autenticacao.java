package Server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Autenticacao {
    private ServerDB serverdb;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    public Autenticacao(PrintWriter o, BufferedReader i , ServerDB server, Socket s){
        out=o;
        in=i;
        serverdb = server;
        socket = s;
    }

    public String conexao(Model model){
        int login=0;
        String c="";
        while(login==0) {
            try {
                int received = Integer.parseInt(in.readLine());
                String nome;
                String pass;
                switch (received) {
                    case -1:
                        System.out.println("sair");
                        return "";
                    case 1:
                        System.out.println("login");
                        nome = in.readLine();
                        pass = in.readLine();

                        c = model.checkuser(nome, pass);
                        if (!c.equals("")){login=1; out.println(1);}
                        else {
                            out.println(0);
                        }
                        break;
                    case 2:
                        System.out.println("registar");
                        nome = in.readLine();
                        pass = in.readLine();
                        System.out.println(1);
                        out.println(model.novaConta(nome, pass));
                        System.out.println(2);
                        break;

                }

            } catch (Exception e) {
                return "";
            }

        }
        return c;
    }
}
