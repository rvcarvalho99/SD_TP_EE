package Client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Menu {
    private BufferedReader input;
    private DataInputStream in;
    private DataOutputStream out;

    public Menu(BufferedReader b, DataInputStream i, DataOutputStream o){
        in=i;
        out=o;
        input=b;
    }

    public void show() throws IOException {
    String read = input.readLine();
        ///////////////////////////////////////////////
        switch (read){
            case "1": //tá
                System.out.println("Download");
                out.writeInt(1);

                System.out.println("Nome playlist:");
                String nomeP = input.readLine();
                out.writeUTF(nomeP);

                //try catch e exceptions como musicaInexistente
                System.out.println("ID da música");
                int idMusica = Integer.parseInt(input.readLine());
                out.writeInt(idMusica);
                int size = in.readInt();
                byte bytearray[] = new byte[size];
                in.readFully(bytearray);
                String nomemusica = in.readUTF();
                System.out.println("Path para onde quer guardar o ficheiro");
                String caminho = input.readLine();

                File n = new File(caminho+ "//"+nomemusica+".mp3");
                FileOutputStream fos = new FileOutputStream(n);
                fos.write(bytearray);
                fos.close();

                break;
            case "2": //tá
                System.out.println("Upload");
                out.writeInt(2);

                System.out.println("Nome playlist:");
                String nomeUP = input.readLine();
                out.writeUTF(nomeUP);

                System.out.println("ID Musica:");
                //try catch
                int idM = Integer.parseInt(input.readLine());
                out.writeInt(idM);

                int sucesso = Integer.parseInt(in.readUTF());
                if (sucesso == 0)
                    System.out.println("Não tem permissão para fazer upload nesta lista");
                else {
                    String titulo = in.readUTF();
                    System.out.println("Path:");
                    String path = input.readLine();

                    byte [] array = Files.readAllBytes(Paths.get(path+"\\" + titulo + ".mp3"));
                    out.writeInt(array.length);
                    out.write(array);
                    System.out.println("Upload realizado com sucesso");
                }
                break;
            case "3": //tá
                System.out.println("Criar PlayList");
                out.writeInt(3);

                String nomePlay = input.readLine();
                out.writeUTF(nomePlay);

                //try catch size incompatível
                int sizePlay = Integer.parseInt(input.readLine());
                out.writeInt(sizePlay);

                for (int i=0; i<sizePlay; i++){
                    System.out.println("Titulo:");
                    String tituloP = input.readLine();
                    out.writeUTF(tituloP);

                    System.out.println("Artista:");
                    String artistaP = input.readLine();
                    out.writeUTF(artistaP);

                    System.out.println("Ano:");
                    //try catch caso se faça merda no ano
                    int anoP = Integer.parseInt(input.readLine());
                    out.writeInt(anoP);
                    System.out.println(in.readInt());

                    //perguntar se quer adicionar já o ficheiro
                    //se quiser temos de pedir o path e eviar
                }

                if (in.readInt()==1)
                    System.out.println("Playlist criada com sucesso");
                else
                    System.out.println("Não foi possível criar a playlist");
                break;
            case "4": //ainda não está
                System.out.println("Adicionar a PlayList");
                out.writeInt(4);

                System.out.println("Nome da PlayList");
                String nomeAP = input.readLine();
                out.writeUTF(nomeAP);

               /* if (in.readInt()==1)
                    System.out.println();*/

                break;
            case "5": //tá
                System.out.println("Ver PlayLists");
                out.writeInt(5);

                System.out.println(in.readUTF());
                break;
            case "6":
                System.out.println("Ver Playlist");
                out.writeInt(6);

                System.out.println("Nome da playlist");
                out.writeUTF(input.readLine());

                System.out.println(in.readUTF());
                break;
            case "7":
                out.writeInt(7);

                System.out.println("Alterar Password");
                String newPassword = input.readLine();
                System.out.println("Coloque novamente a nova password");
                String newPasswordConf = input.readLine();
                while (!newPassword.equals(newPasswordConf)){
                    System.out.println("Palavra passe errada coloque de novo");
                    newPassword = input.readLine();
                    System.out.println("Coloque novamente a nova password");
                    newPasswordConf = input.readLine();
                }
                out.writeUTF(newPassword);
                if (in.readInt() == 1)
                    System.out.println("Sucesso");
                else System.out.println("Insucesso");
                break;
        }
    }
}
