import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    static final int PORT = 9999;
    static final String HOST = "localhost";

    public Socket connectar() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
        System.out.println("Esperant connexio...");
        Socket socket = serverSocket.accept();
        System.out.println("Connexio acceptada: " + socket.getInetAddress());
        serverSocket.close();
        return socket;
    }

    public void tancarConnexio(Socket socket) throws IOException {
        System.out.println("Tancant connexio amb el client: " + socket.getInetAddress());
        // si no es null o no esta tancat, tanca
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public void enviarFitxers(Socket socket) throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        boolean sortir = false;

        while (!sortir) {
            System.out.println("Esperant el nom del fitxer del client...");

            // llegim l'objecte del stream i ho passem a string
            String nomFitxer = (String) in.readObject();

            if (nomFitxer == null || nomFitxer.equals("sortir")) {
                System.out.println("Nom del fitxer buit o nul. Sortint...");
                sortir = true;
                continue;
            }

            System.out.println("Nomfitxer rebut: " + nomFitxer);

            // Llegim el contingut del fitxer
            Fitxer fitxer = new Fitxer(nomFitxer);
            try {
                byte[] contingut = fitxer.getContingut();
                System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");

                // Enviem el contingut com a byte
                out.writeObject(contingut);
                out.flush();
                System.out.println("Fitxer enviat al client: " + nomFitxer);

            } catch (IOException e) {
                System.out.println("Error llegint el fitxer del client: " + e.getMessage());
                // Enviem null per avisar al client que hi ha hagut un error
                out.writeObject(null);
                out.flush();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Servidor servidor = new Servidor();
        Socket socket = servidor.connectar();
        servidor.enviarFitxers(socket);
        servidor.tancarConnexio(socket);
    }
}
