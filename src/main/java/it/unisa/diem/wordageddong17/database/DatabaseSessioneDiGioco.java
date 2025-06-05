/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class DatabaseSessioneDiGioco implements DosSessioneDiGioco{
    
    Database db;

    private DatabaseSessioneDiGioco(){
         db= Database.getInstance();
    }
    
    

    @Override
    public byte[] prendiStopwords(String nomeFile) {
         String query= "SELECT documento FROM stopwords where nome_file=?;";
        byte[] risultato=null;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeFile); // inserisce il nome  nella prima posizione del preparestatment
            ResultSet r = pstmt.executeQuery();
            if(r.next()){
                risultato = r.getBytes("documento");
            }
        }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return risultato;
    }

    @Override
    public List<byte[]> prendiDocumenti(LivelloPartita livello) {
        List<byte[]> documenti = new ArrayList<>();
        String query="SELECT documento FROM testo where difficolta= ?;";
        
        try(PreparedStatement pstmt = db.getConnection().prepareStatement(query)){
            pstmt.setString(1, livello.name());
            ResultSet r=pstmt.executeQuery();
            byte[] documentoInBytes;
            while(r.next()){
               documentoInBytes=r.getBytes("documento");
               documenti.add(documentoInBytes);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseSessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return documenti;
    }
    
    
    /**
     * Classe statica interna che contiene l'istanza Singleton della classe {@code Database}.
     * <p>
     * L'istanza viene creata solo quando la classe {@code Holder} viene caricata, garantendo la thread safety.
     * </p>
     */
    private static class Holder {
        private static final DatabaseSessioneDiGioco INSTANCE = new DatabaseSessioneDiGioco();
    }
    
    /**
     * Restituisce l'istanza Singleton della classe {@code Database}.
     * <p>
     * Utilizza il pattern del Lazy Holder per garantire l'inizializzazione pigra e la thread safety
     * senza la necessità di sincronizzazione esplicita.
     * </p>
     *
     * @return l'istanza Singleton della classe {@code Database}.
     */
    public static DatabaseSessioneDiGioco getInstance() {
        return DatabaseSessioneDiGioco.Holder.INSTANCE;
    }
}
