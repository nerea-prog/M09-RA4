import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {

    // Directori on es guardaran els fitxers rebuts
    static final String DIR_ARRIBADA = "C:\\tmp";
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;

    public void connectar() throws Exception {
        socket = new Socket(Servidor.HOST, Servidor.PORT);
        System.out.println("Connectant a -> " + Servidor.HOST + ":" + Servidor.PORT);
        System.out.println("Connexio acceptada: " + socket.getInetAddress());

        // Inicialitzem els streams del socket
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void rebreFitxers() throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean sortir = false;

        while (!sortir) {
            System.out.print("Nom del fitxer a rebre ('sortir' per sortir): ");
            String nomFitxer = scanner.nextLine();

            out.writeObject(nomFitxer);
            out.flush();

            if (nomFitxer.equals("sortir")) {
                System.out.println("Sortint...");
                sortir = true;
                continue;
            }

            // Rebem el contingut com a byte
            byte[] contingut = (byte[]) in.readObject();

            if (contingut == null) {
                System.out.println("Error: el servidor no ha pogut enviar el fitxer.");
                continue;
            }

            
            String nomBase = new File(nomFitxer).getName();  // extreu només el nom del fitxer sense la ruta d'origen
            String rutaDesti = DIR_ARRIBADA + "/" + nomBase;
            System.out.println("Nom del fitxer a guardar: " + rutaDesti);
            Files.write(Paths.get(rutaDesti), contingut); // escriu els bytes rebuts al fitxer de destí
            System.out.println("Fitxer rebut i guardat com: " + rutaDesti);
        }
    }

    public void tancarConnexio() throws Exception {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        System.out.println("Connexio tancada.");
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connectar();
        client.rebreFitxers();
        client.tancarConnexio();
    }
}