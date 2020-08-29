package Client;
import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 5000);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        boolean aux = true;
        while (aux) {
            System.out.println("Hello there");
            System.out.println("1-login 2-registar 3-quit");
            String  option = input.readLine();
            switch (option) {
                case "1":
                    out.writeInt(1);
                    System.out.println("Inserir username:");
                    String username = input.readLine();
                    System.out.println("Inserir password:");
                    String password = input.readLine();
                    out.writeUTF(username);
                    out.writeUTF(password);
                    int serverResponseL = in.readInt();
                    if (serverResponseL == 1) {
                        System.out.println("Login feito com sucesso");
                    }
                    else {
                        System.out.println("Não foi possível fazer login");
                    }
                    break;
                case "2":
                    out.writeInt(2);

                    int serverResponseR = in.readInt();
                    if (serverResponseR == 1) {
                        System.out.println("Registo feito com sucesso");
                    }
                    else {
                        System.out.println("Não foi possível registar a conta");
                    }
                    break;
                case "3":
                    out.writeInt(-1);
                    aux = false;
                    break;
            }
        }

        System.out.println("Até à próxima!");
        input.close();
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();

    }
}