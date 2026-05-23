import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Fitxer  { 

    private String nom;
    private byte[] contingut;

    public Fitxer(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public byte[] getContingut() throws Exception {
        File fitxer = new File(nom);

        if (!fitxer.exists()) {
            throw new IOException("El fitxer no existeix: " + nom); // llancem excepció per gestionar l'error
        }

        contingut = Files.readAllBytes(fitxer.toPath()); //  llegeix tots els bytes d'un fitxer
        return contingut;
    }
}