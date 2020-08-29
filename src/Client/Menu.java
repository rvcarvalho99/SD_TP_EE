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
            case "1":
                System.out.println("Download");
                out.writeInt(1);

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
            case "2":
                System.out.println("Upload");
                out.writeInt(2);

                System.out.println("Titulo:");
                String titulo = input.readLine();
                out.writeUTF(titulo);

                System.out.println("Artista:");
                String artista = input.readLine();
                out.writeUTF(artista);

                System.out.println("Ano:");
                //try catch caso se faça merda no ano
                int ano = Integer.parseInt(input.readLine());
                out.writeInt(ano);
                System.out.println(in.readInt());
                System.out.println("Path:");
                String path = input.readLine();
                byte [] array = Files.readAllBytes(Paths.get(path+"\\" + titulo + ".mp3"));
                out.writeInt(array.length);
                out.write(array);
                System.out.println("Upload realizado com sucesso");
                //enviar o ficheiro

                break;
            case "3":
                System.out.println("Criar PlayList");
                out.writeInt(3);
                break;
            case "4":
                System.out.println("Adicionar a PlayList");
                out.writeInt(4);

                System.out.println("Nome da PlayList");
                String nomePlay = input.readLine();
                out.writeUTF(nomePlay);

                break;
            case "5":
                System.out.println("Ver PlayLists");
                out.writeInt(5);
                break;
            case "6":
                out.writeInt(6);
                System.out.println("Ver info de Musica");
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
