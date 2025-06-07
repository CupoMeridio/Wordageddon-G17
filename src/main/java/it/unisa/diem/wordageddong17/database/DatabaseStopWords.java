package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseStopWords implements DAOListaStopWords {
    
    private Database db;
    
    private DatabaseStopWords(){
        db = Database.getInstance();
    }
    /**
     * Carica o aggiorna un file di stopwords nel database.
     * <p>
     * Se un file con lo stesso nome esiste già, viene aggiornato con il nuovo contenuto.
     * Utilizza la clausola ON CONFLICT per gestire i duplicati.
     * </p>
     * 
     * @param email l'email dell'amministratore che carica il file
     * @param documentoStopwords il contenuto del file di stopwords come array di byte
     * @param nomeFile il nome del file da salvare nel database
     * @return {@code true} se l'operazione è stata eseguita con successo, {@code false} altrimenti
     * 
     */
    @Override
    public boolean CaricareStopwords(String email, byte[] documentoStopwords, String nomeFile) {
        String query= "INSERT INTO stopwords(\n" +
        "nome_file, documento, id_amministratore)\n" +
        "VALUES (?, ?, ?) ON conflict(nome_file) DO UPDATE SET documento=?,id_amministratore=?";
        
        boolean risultato= false;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeFile); // inserisce il nome  nella prima posizione del preparestatment
            pstmt.setBytes(2, documentoStopwords);
            pstmt.setString(3, email);
            pstmt.setBytes(4, documentoStopwords);
            pstmt.setString(5, email);
            risultato= pstmt.execute();// esegue il prepare statment
            
            } catch (SQLException ex) { 
            System.getLogger(DatabaseStopWords.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return risultato;
    }
    
    /**
     * Recupera il contenuto di un file di stopwords dal database.
     * <p>
     * Cerca il file specificato nella tabella stopwords e restituisce il suo contenuto
     * sotto forma di array di byte.
     * </p>
     * 
     * @param nomeFile il nome del file di stopwords da recuperare
     * @return l'array di byte contenente il documento, oppure {@code null} se il file non esiste
     * 
     */

    @Override
    public byte[] PrendiStopwords(String nomeFile) {
        String query= "SELECT documento FROM stopwords where nome_file=?;";
        byte[] risultato=null;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeFile); // inserisce il nome  nella prima posizione del preparestatment
            ResultSet r = pstmt.executeQuery();
            
            if(r.next()){
            risultato = r.getBytes("documento");
            }
            } catch (SQLException ex) { 
            System.getLogger(DatabaseStopWords.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return risultato;
    }
    
    private static class Holder {
        private static final DatabaseStopWords INSTANCE = new DatabaseStopWords();
    }
    
    
    public static DatabaseStopWords getInstance() {
        return DatabaseStopWords.Holder.INSTANCE;
    }
    
    
}
