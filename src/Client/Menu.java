package Client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Menu {
    private BufferedReader input;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public Menu(BufferedReader b, BufferedReader i, PrintWriter o, Socket s){
        in=i;
        out=o;
        input=b;
        socket=s;
    }

    public void show() throws IOException {
        while(true) {
            System.out.println("1-Download 2-Upload 3-Criar PlayList 4-Adicionar PlayLists 5-Ver PlayLists 6-Ver PlayList 7-Mudar Password 8-Sair");
            String read = input.readLine();
            ///////////////////////////////////////////////
            switch (read) {
                case "1": //tá
                    System.out.println("Download");
                    out.println(1);

                    System.out.println("Nome playlist:");
                    String nomeP = input.readLine();
                    out.println(nomeP);

                    //try catch e exceptions como musicaInexistente
                    System.out.println("ID da música");
                    int idMusica = Integer.parseInt(input.readLine());
                    out.println(idMusica);
                    int size = Integer.parseInt(in.readLine());
                    DataInputStream infile = new DataInputStream(socket.getInputStream());
                    byte bytearray[] = new byte[size];
                    infile.readFully(bytearray);
                    infile.close();
                    String nomemusica = in.readLine();
                    System.out.println("Path para onde quer guardar o ficheiro");
                    String caminho = input.readLine();

                    File n = new File(caminho + "//" + nomemusica + ".mp3");
                    FileOutputStream fos = new FileOutputStream(n);
                    fos.write(bytearray);
                    fos.close();

                    break;
                case "2": //tá
                    System.out.println("Upload");
                    out.println(2);

                    System.out.println("Nome playlist:");
                    String nomeUP = input.readLine();
                    out.println(nomeUP);

                    System.out.println("ID Musica:");
                    //try catch
                    int idM = Integer.parseInt(input.readLine());
                    out.println(idM);

                    int sucesso = Integer.parseInt(in.readLine());
                    if (sucesso == 0)
                        System.out.println("Não tem permissão para fazer upload nesta lista");
                    else {
                        String titulo = in.readLine();
                        System.out.println("Path:");
                        String path = input.readLine();

                        byte[] array = Files.readAllBytes(Paths.get(path + "\\" + titulo + ".mp3"));
                        out.println(array.length);
                        DataOutputStream outfile = new DataOutputStream(socket.getOutputStream());
                        outfile.write(array);
                        System.out.println("Upload realizado com sucesso");
                    }
                    break;
                case "3": //tá
                    System.out.println("Criar PlayList");
                    out.println(3);

                    String nomePlay = input.readLine();
                    out.println(nomePlay);

                    //try catch size incompatível
                    int sizePlay = Integer.parseInt(input.readLine());
                    out.println(sizePlay);

                    for (int i = 0; i < sizePlay; i++) {
                        System.out.println("Titulo:");
                        String tituloP = input.readLine();
                        out.println(tituloP);

                        System.out.println("Artista:");
                        String artistaP = input.readLine();
                        out.println(artistaP);

                        System.out.println("Ano:");
                        //try catch caso se faça merda no ano
                        int anoP = Integer.parseInt(input.readLine());
                        out.println(anoP);


                        //perguntar se quer adicionar já o ficheiro
                        //se quiser temos de pedir o path e eviar
                    }

                    if (Integer.parseInt(in.readLine()) == 1)
                        System.out.println("Playlist criada com sucesso");
                    else
                        System.out.println("Não foi possível criar a playlist");
                    break;
                case "4": //ainda não está
                    System.out.println("Adicionar a PlayList");
                    out.println(4);

                    System.out.println("Nome da PlayList");
                    String nomeAP = input.readLine();
                    out.println(nomeAP);

               /* if (in.readInt()==1)
                    System.out.println();*/

                    break;
                case "5": //tá
                    System.out.println("Ver PlayLists");
                    out.println(5);

                    System.out.println(in.readLine());
                    break;
                case "6":
                    System.out.println("Ver Playlist");
                    out.println(6);

                    System.out.println("Nome da playlist");
                    out.println(input.readLine());

                    System.out.println(in.readLine());
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
                    if (Integer.parseInt(in.readLine()) == 1)
                        System.out.println("Sucesso");
                    else System.out.println("Insucesso");
                    break;
                case "8":
                    return;
            }
        }
    }
}
