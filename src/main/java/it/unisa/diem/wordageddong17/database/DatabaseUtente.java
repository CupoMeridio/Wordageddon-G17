/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class DatabaseUtente implements DosUtente{

     private Database db;

    private DatabaseUtente() {
        db= Database.getInstance();
    }
    
    @Override
    public boolean modificaUsername(String email, String username) {
         String query= "UPDATE utente  SET username=? where email=?;";
        boolean risultato= false;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, email); 
                risultato=pstmt.execute();
            }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return risultato;
    }

    @Override
    public boolean modificaFotoProfilo(String email, byte[] foto) {
         String query= "UPDATE utente SET foto=? where email=?;";
        boolean risultato= false;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
                pstmt.setBytes(1, foto);
                pstmt.setString(2, email); 
            risultato=pstmt.execute();
            }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return risultato;
    }
    
    
         /**
     * Classe statica interna che contiene l'istanza Singleton della classe {@code Database}.
     * <p>
     * L'istanza viene creata solo quando la classe {@code Holder} viene caricata, garantendo la thread safety.
     * </p>
     */
    private static class Holder {
        private static final DatabaseUtente INSTANCE = new DatabaseUtente();
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
    public static DatabaseUtente getInstance() {
        return DatabaseUtente.Holder.INSTANCE;
    }
}
