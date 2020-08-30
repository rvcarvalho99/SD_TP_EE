package Client;

import java.io.BufferedReader;
import java.net.Socket;

public class Listener implements Runnable {
    BufferedReader conn;
    Socket socket;
    public Listener(BufferedReader c,Socket s) {
        conn = c;
        socket = s;
    }

    public void run() {
        try {
            String r;
            while(socket.isBound()) {
                r = conn.readLine();

                while ((r.isEmpty() != true)) {
                    System.out.println(r);
                    r = conn.readLine();
                }
            }
        }
        catch (Exception e){}
    }
}
