
import it.unisa.diem.wordageddong17.database.Database;
import it.unisa.diem.wordageddong17.database.DatabaseAmministratore;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Mattia Sanzari
 */
public class TestDatabase {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /*Connection cn= Database.getInstance().getConnection();
        
        try {
            System.out.println("Sto aspettando un secondo");
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }*/ //->connessione "semplice"
        
        String email="admin@admin.it";
        String nomefile="prova";
        DatabaseAmministratore db= DatabaseAmministratore.getInstance();
        byte[] documentoDaCaricare;
        try {
            documentoDaCaricare = documentoDaCaricare = Files.readAllBytes(Paths.get("prova.txt"));
            db.CaricareStopwords(email, documentoDaCaricare, nomefile);
        } catch (IOException ex) {
            Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Verifica della modifica con un utente solo giocatore esce->org.postgresql.util.PSQLException: ERROR: L'utente deve essere un amministratore!->FUNZIONA il trigger check_admin_stopwords();
        byte[] risultatoStopwords= db.PrendiStopwords("prova");
        
        try(FileOutputStream fos= new FileOutputStream("ProvaRisultato.txt") ){
            fos.write(risultatoStopwords);
        } catch (Exception ex) {
            Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        // metodi per le Stopwords Funzionano
        db.CancellaTesto("prova");
        try {
            
            documentoDaCaricare = documentoDaCaricare = Files.readAllBytes(Paths.get("prova.txt"));
            db.CaricareTesto(email, nomefile, "facile", documentoDaCaricare);
        } catch (IOException ex) {
            Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] risultatoDocumento= db.PrendiTesto("prova");
        
        try(FileOutputStream fos= new FileOutputStream("ProvaRisultatoDocumento.txt") ){
            fos.write(risultatoDocumento);
        } catch (Exception ex) {
            Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        db.CancellaTesto("prova");
    }
    // metodi per il testo funzionano
}
