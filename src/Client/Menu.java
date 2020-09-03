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

            System.out.println("1-Download 2-Upload 3-Criar PlayList 4-Adicionar à PlayList 5-Ver todas as PlayLists 6-Ver PlayList 7-Mudar Password 8-Sair");
            String read = input.readLine();

            switch (read) {
                case "1":
                    System.out.println("Download:   ##Se quiser sair escreva \"quit\"##");
                    out.println(1);

                    System.out.println("Nome da playlist:");
                    String nomeP = input.readLine();
                    if(nomeP.equals("quit")){
                        out.println(-1);
                        out.println(-1);
                        break;
                    }
                    out.println(nomeP);
                    String idMusicaS = "";
                    int idMusica=500;
                    System.out.println("ID da música");
                    boolean ivalidId = true;
                    boolean exitDownload = false;
                    while (ivalidId){
                        try{
                            idMusicaS = input.readLine();

                            idMusica = Integer.parseInt(idMusicaS);

                            if (idMusica>0){
                                ivalidId=false;
                            }
                            else System.out.println("O ID introduzido é inválido");
                        }catch (Exception invalidID){
                            if (idMusicaS.equals("quit")){
                                out.println(-1);
                                exitDownload = true;
                                break;
                            }
                            else System.out.println("O ID introduzido é inválido");
                        }
                    }

                    if (exitDownload) break;

                    System.out.println("Insira o nome que quer dar à música");
                    String nomemusica = input.readLine();
                    boolean pathvalid=false;
                    String caminho ="";
                    while(!pathvalid){
                        System.out.println("Insira o Path para onde quer guardar o ficheiro. Ex: \"C:\\Users\\Desktop\"");
                        caminho = input.readLine();
                        File f = new File(caminho);
                        if(f.isDirectory()) {
                            pathvalid=true;
                        }
                    }
                    out.println(idMusica);
                    Socket connDownload = new Socket("127.0.0.1", port);
                    DataInputStream infile = new DataInputStream(connDownload.getInputStream());
                    Receber receber = new Receber(connDownload,infile,nomemusica,caminho);
                    Thread t2 = new Thread(receber);
                    t2.start();

                    break;
                case "2":
                    System.out.println("Upload:   ##Se quiser sair escreva \"quit\"##");
                    out.println(2);

                    System.out.println("Nome da playlist:");
                    String nomeUP = input.readLine();
                    if (nomeUP.equals("quit")){
                        out.println(-1);
                        out.println(-1);
                        break;
                    }
                    else out.println(nomeUP);

                    System.out.println("ID da Musica:");
                    String idMS = "";
                    boolean invalidId = true;
                    boolean exitUpload = false;
                    while (invalidId) {
                        try {
                            idMS = input.readLine();
                            int idM = Integer.parseInt(idMS);
                            out.println(idM);
                            if (idM > 0) {
                                invalidId = false;
                            } else System.out.println("O ID introduzido é inválido");
                        } catch (Exception fakeSize) {
                            if (idMS.equals("quit")) {
                                out.println(-1);
                                exitUpload = true;
                                System.out.println("O id introduzido é inválido");
                            }
                        }
                    }

                    if (exitUpload) break;

                    System.out.println("Insira o Nome da música no seu Pc. Ex Stairwaytoheaven");
                    String titulo = input.readLine();
                    if (titulo.equals("quit")) break;
                    boolean pathval=false;
                    boolean notfst = false;
                    String path ="";
                    while(!pathval){
                        if(notfst){
                            System.out.println("Path Inválido, tente de novo.");
                            System.out.println("Insira o Nome da música no seu Pc. Ex HotelCalifornia");
                            titulo = input.readLine();
                            if(titulo.equals("quit")) {
                                exitUpload = true;
                                break;
                            }
                        }
                        System.out.println("Insira o Path do ficheiro. Ex: \"C:\\Users\\Desktop\"");
                        path = input.readLine();
                        if(path.equals("quit")) {
                            exitUpload = true;
                            break;
                        }
                            File f = new File(path + "\\" + titulo+".mp3");
                            if(f.exists() && !f.isDirectory()) {
                                pathval=true;
                            }
                            notfst=true;
                    }
                    if (exitUpload) break;

                    Socket connUpload = new Socket("127.0.0.1", port);
                    DataOutputStream outfile = new DataOutputStream(connUpload.getOutputStream());
                    Enviar enviar = new Enviar(titulo,connUpload,outfile,path);
                    Thread t3 = new Thread(enviar);
                    t3.start();

                    break;

                case "3":
                    System.out.println("Criar PlayList");
                    out.println(3);
                    System.out.println("Nome da PlayList ##Se quiser cancelar escreva \"quit\" !!Depois do nome ser dado é impossível cancelar!!");
                    String nomePlay = input.readLine();
                    if (nomePlay.equals("quit")) break;
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
                        System.out.println("Titulo da música " + (i+1) + ":");
                        String tituloP = input.readLine();
                        out.println(tituloP);

                        System.out.println("Artista da música " + (i+1) + ":");
                        String artistaP = input.readLine();
                        out.println(artistaP);

                        System.out.println("Ano da música " + (i+1) + ":");
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
                        System.out.println("Quer adicionar já o ficheiro música?");
                        System.out.println("1-Adicionar 0-Não adicionar");
                        if (input.readLine().equals("1")){
                            out.println(1);

                            System.out.println("Insira o Nome da música no seu Pc. Ex Comeasyouare");
                            String tituloA = input.readLine();
                            boolean pathvalA=false;
                            boolean notfstA = false;
                            String pathA ="";
                            while(!pathvalA){
                                if(notfstA){
                                    System.out.println("Path Inválido, tente de novo.");
                                    System.out.println("Insira o Nome da música no seu Pc. Ex NothingElseMatters");
                                    tituloA = input.readLine();
                                }
                                System.out.println("Insira o Path do ficheiro. Ex: \"C:\\Users\\Desktop\"");
                                pathA = input.readLine();
                                File f = new File(pathA + "\\" + tituloA+".mp3");
                                if(f.exists() && !f.isDirectory()) {
                                    pathvalA=true;
                                }
                                notfstA=true;
                            }

                            Socket connUploadA = new Socket("127.0.0.1", port);
                            DataOutputStream outfileA = new DataOutputStream(connUploadA.getOutputStream());
                            Enviar enviarA = new Enviar(tituloA,connUploadA,outfileA,pathA);
                            Thread t3A = new Thread(enviarA);
                            t3A.start();
                        }
                        else out.println(0);
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
