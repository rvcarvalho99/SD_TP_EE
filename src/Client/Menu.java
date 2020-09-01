package Client;

import Transferencias.Enviar;
import Transferencias.Receber;

import java.io.*;
import java.net.Socket;

public class Menu {
    private BufferedReader input;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private int port;

    public Menu(BufferedReader b, BufferedReader i, PrintWriter o, Socket s){
        in=i;
        out=o;
        input=b;
        socket=s;
    }


    public void show() throws IOException {
        port = Integer.parseInt(in.readLine());
        Listener lis = new Listener(in,socket);
        Thread t1 = new Thread(lis);
        t1.start();
        while(true) {

            System.out.println("1-Download 2-Upload 3-Criar PlayList 4-Adicionar à PlayList 5-Ver PlayLists 6-Ver PlayList 7-Mudar Password 8-Sair");
            String read = input.readLine();

            switch (read) {
                case "1": //tá
                    System.out.println("Download");
                    out.println(1);

                    System.out.println("Nome playlist:");
                    String nomeP = input.readLine();
                    out.println(nomeP);

                    System.out.println("ID da música");
                    boolean ivalidId = true;
                    while (ivalidId){
                        try{
                            int idMusica = Integer.parseInt(input.readLine());
                            out.println(idMusica);
                        }catch (Exception invalidID){
                            System.out.println("O ID introduzido é inválido");
                        }
                    }

                    System.out.println("Insira o nome que quer dar à música");
                    String nomemusica = input.readLine();
                    System.out.println("Path para onde quer guardar o ficheiro");
                    boolean pathvalid=false;
                    String caminho ="";
                    while(!pathvalid){
                        System.out.println("Path:");
                        caminho = input.readLine();
                        File f = new File(caminho);
                        if(f.isDirectory()) {
                            pathvalid=true;
                        }
                    }
                    Socket connDownload = new Socket("127.0.0.1", port);
                    DataInputStream infile = new DataInputStream(connDownload.getInputStream());
                    Receber receber = new Receber(connDownload,infile,nomemusica,caminho);
                    Thread t2 = new Thread(receber);
                    t2.start();

                    break;
                case "2":
                    System.out.println("Upload");
                    out.println(2);

                    System.out.println("Nome playlist:");
                    String nomeUP = input.readLine();
                    out.println(nomeUP);

                    System.out.println("ID Musica:");
                    boolean invalidId = true;
                    while (invalidId){
                        try {
                            int idM = Integer.parseInt(input.readLine());
                            out.println(idM);
                            invalidId = false;
                        }catch (Exception fakeSize){
                            System.out.println("O id introduzido é inválido");
                        }
                    }
                    System.out.println("Nome da música");
                    String titulo = input.readLine();
                    boolean pathval=false;
                    boolean notfst = false;
                    String path ="";
                    while(!pathval){
                        if(notfst){
                            System.out.println("Path Inválido, tente de novo.");
                            System.out.println("Nome da música");
                            titulo = input.readLine();
                        }
                        System.out.println("Path:");
                        path = input.readLine();
                            File f = new File(path + "\\" + titulo+".mp3");
                            if(f.exists() && !f.isDirectory()) {
                                pathval=true;
                            }
                            notfst=true;
                    }


                    Socket connUpload = new Socket("127.0.0.1", port);
                    DataOutputStream outfile = new DataOutputStream(connUpload.getOutputStream());
                    Enviar enviar = new Enviar(titulo,connUpload,outfile,path);
                    Thread t3 = new Thread(enviar);
                    t3.start();

                    break;
                case "3":
                    System.out.println("Criar PlayList");
                    out.println(3);
                    System.out.println("Nome da PlayList");
                    String nomePlay = input.readLine();
                    out.println(nomePlay);
                    System.out.println("Número de músicas da PlayList");
                    int sizePlay = 0;
                    boolean invalidSize = true;
                    while (invalidSize){
                        try {
                            sizePlay = Integer.parseInt(input.readLine());
                            out.println(sizePlay);
                            invalidSize = false;
                        }catch (Exception fakeSize){
                            System.out.println("O tamanho introduzido é inválido");
                        }
                    }

                    for (int i = 0; i < sizePlay; i++) {
                        System.out.println("Titulo:");
                        String tituloP = input.readLine();
                        out.println(tituloP);

                        System.out.println("Artista:");
                        String artistaP = input.readLine();
                        out.println(artistaP);

                        System.out.println("Ano:");
                        boolean invalidYear = true;
                        while (invalidYear){
                            try {
                                int anoP = Integer.parseInt(input.readLine());
                                out.println(anoP);
                                invalidYear = false;
                            }catch (Exception fakeYear){
                                System.out.println("O ano introduzido é inválido");
                            }
                        }
                        //perguntar se quer adicionar já o ficheiro
                        //se quiser temos de pedir o path e eviar
                    }
                    break;
                case "4":
                    System.out.println("Adicionar à PlayList");
                    out.println(4);

                    System.out.println("Nome da PlayList");
                    String nomeAP = input.readLine();
                    out.println(nomeAP);

                    System.out.println("Titulo:");
                    String tituloP = input.readLine();
                    out.println(tituloP);

                    System.out.println("Artista:");
                    String artistaP = input.readLine();
                    out.println(artistaP);

                    System.out.println("Ano:");
                    boolean invalidYear = true;
                    while (invalidYear){
                        try {
                            int anoP = Integer.parseInt(input.readLine());
                            out.println(anoP);
                            invalidYear = false;
                        }catch (Exception fakeYear){
                            System.out.println("O ano introduzido é inválido");
                        }
                    }
                    break;
                case "5":
                    System.out.println("Ver PlayLists");
                    out.println(5);

                    break;
                case "6":
                    System.out.println("Ver Playlist");
                    out.println(6);

                    System.out.println("Nome da playlist");
                    out.println(input.readLine());

                    break;
                case "7":
                    out.println(7);

                    System.out.println("Alterar Password");
                    String newPassword = input.readLine();
                    System.out.println("Coloque novamente a nova password");
                    String newPasswordConf = input.readLine();
                    while (!newPassword.equals(newPasswordConf)) {
                        System.out.println("Palavra passe errada coloque de novo");
                        newPassword = input.readLine();
                        System.out.println("Coloque novamente a nova password");
                        newPasswordConf = input.readLine();
                    }
                    out.println(newPassword);
                    break;
                case "8":
                    return;
                default:
                    break;
            }
        }
    }
}
