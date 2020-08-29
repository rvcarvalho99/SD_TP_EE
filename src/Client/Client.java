package Client;
import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 5000);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

        boolean aux = true;
        while (aux) {
            System.out.println("Hello there");
            System.out.println("1-login 2-registar 3-quit");
            String  option = input.readLine();
            String username;
            String password;
            switch (option) {
                case "1":
                    out.println(1);
                    System.out.println("Inserir username:");
                    username = input.readLine();
                    System.out.println("Inserir password:");
                    password = input.readLine();
                    out.println(username);
                    out.println(password);
                    int serverResponseL = Integer.parseInt(in.readLine());
                    if (serverResponseL == 1) {
                        System.out.println("Login feito com sucesso");
                        Menu menu = new Menu(input,in,out,socket);
                        menu.show();
                    }
                    else {
                        System.out.println("Não foi possível fazer login");
                    }
                    break;
                case "2":
                    out.println(2);
                    System.out.println("Inserir username:");
                    username = input.readLine();
                    System.out.println("Inserir password:");
                    password = input.readLine();
                    out.println(username);
                    out.println(password);
                    int serverResponseR = Integer.parseInt(in.readLine());
                    if (serverResponseR == 1) {
                        System.out.println("Registo feito com sucesso");
                    }
                    else {
                        System.out.println("Não foi possível registar a conta");
                    }
                    break;
                case "3":
                    out.println(-1);
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