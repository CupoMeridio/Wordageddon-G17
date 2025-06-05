/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementazione dell'interfaccia {@code DosAmministratore} per la gestione delle operazioni
 * amministrative sul database.
 * <p>
 * Questa classe fornisce metodi per la gestione di stopwords e testi nel sistema,
 * permettendo agli amministratori di caricare, recuperare e cancellare documenti.
 * Implementa il pattern Singleton per garantire una sola istanza della classe.
 * </p>
 * 
 * @author Mattia Sanzari
 */
public class DatabaseAmministratore implements DosAmministratore {
    /**
    * Istanza del database utilizzata per le operazioni di accesso ai dati.
    */
    private Database db;

    /**
     * Costruttore della classe DatabaseAmministratore.
     * 
     * Inizializza l'istanza del database utilizzando il pattern Singleton.
     * 
     */
    public DatabaseAmministratore() {
        db= Database.getInstance();
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
            
            }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
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
            }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return risultato;
    }

    /**
     * Cancella un testo dal database.
     * 
     * Rimuove definitivamente il documento specificato dalla tabella testo.
     * 
     * @param nomeDocumento il nome del documento da cancellare
     * @return {@code true} se l'operazione è stata eseguita con successo, {@code false} altrimenti
     * 
     */
    @Override
    public boolean CancellaTesto(String nomeDocumento) {
       String query="DELETE FROM public.testo WHERE nome_file= ?;";
       boolean risultato=false;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeDocumento); // cancella il documento con questo nome
            risultato = pstmt.execute();
            }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return risultato;
    }


    /**
     * Carica un nuovo testo nel database.
     * <p>
     * Inserisce un nuovo documento nella tabella testo con le informazioni specificate.
     * Il documento viene associato all'amministratore che lo carica.
     * </p>
     * 
     * @param email l'email dell'amministratore che carica il testo
     * @param nomeFile il nome del file da salvare
     * @param difficolta il livello di difficoltà del testo
     * @param file il contenuto del file come array di byte
     * @return {@code true} se l'operazione è stata eseguita con successo, {@code false} altrimenti
     * 
     */
    @Override
    public boolean CaricareTesto(String email, String nomeFile, String difficolta, byte[] file, Lingua lingua) {
        String query= "INSERT INTO testo(\n" +
        "nome_file, id_amministratore, difficolta, documento, lingua)\n" +
        "VALUES (?, ?, ?, ?, ?::lingua_type)";
        
        boolean risultato= false;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeFile); // inserisce l' utente nella prima posizione del preparestatment
            pstmt.setString(2, email);
            pstmt.setString(3, difficolta);
            pstmt.setBytes(4, file);
            pstmt.setString(5,lingua.getCodice());
            return pstmt.executeUpdate() > 0; // True se almeno una riga è stata inserita
            }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Recupera il contenuto di un testo dal database.
     * <p>
     * Cerca il documento specificato nella tabella testo e restituisce il suo contenuto
     * sotto forma di array di byte.
     * </p>
     * 
     * @param nomeDocumento il nome del documento da recuperare
     * @return l'array di byte contenente il documento, oppure {@code null} se il documento non esiste
     */
    
    @Override
    public byte[] PrendiTesto(String nomeDocumento) {
        
        String query="SELECT documento\n FROM testo where nome_file= ?;";
        
        byte[] risultato=null;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            
            pstmt.setString(1, nomeDocumento); // inserisce l' utente nella prima posizione del preparestatment  
            ResultSet r= pstmt.executeQuery();
            if(r.next()){
            risultato=r.getBytes("documento");
            }
        }catch (SQLException ex) {
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
        private static final DatabaseAmministratore INSTANCE = new DatabaseAmministratore();
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
    public static DatabaseAmministratore getInstance() {
        return DatabaseAmministratore.Holder.INSTANCE;
    }

    public ArrayList<DocumentoDiTesto> prendiTuttiIDocumenti(){
        ArrayList<DocumentoDiTesto> L = new ArrayList<>();
        String query="SELECT nome_file, id_amministratore, difficolta, lingua FROM testo";
        Statement s = null;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            ResultSet result = pstmt.executeQuery();
            while(result.next()) {
                L.add(new DocumentoDiTesto(
                    result.getString("nome_file"),
                    LivelloPartita.fromDbValue(result.getString("difficolta")),
                    result.getString("id_Amministratore"),
                    Lingua.fromCodice(result.getString("lingua"))
                    ));
                    
            }
        } catch (SQLException ex) {
            System.getLogger(DatabaseAmministratore.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return L;
    }
    
}
