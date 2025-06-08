package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import java.util.HashMap;
import java.util.Map;

public class DatabaseDocumentoDiTesto implements DAODocumentoDiTesto{
    
    Database db;
    
    private DatabaseDocumentoDiTesto(){
        db= Database.getInstance();
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
    public boolean cancellaTesto(String nomeDocumento) {
       String query="DELETE FROM public.testo WHERE nome_file= ?;";
       boolean risultato=false;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeDocumento); // cancella il documento con questo nome
            risultato = pstmt.executeUpdate()>0;
            } catch (SQLException ex) {
            System.getLogger(DatabaseDocumentoDiTesto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
    public boolean caricaTesto(String email, String nomeFile, String difficolta, byte[] file, Lingua lingua) {
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
            } catch (SQLException ex) { 
            System.getLogger(DatabaseDocumentoDiTesto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
    public byte[] prendiTesto(String nomeDocumento) {
        
        String query="SELECT documento\n FROM testo where nome_file= ?;";
        
        byte[] risultato=null;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            
            pstmt.setString(1, nomeDocumento); // inserisce l' utente nella prima posizione del preparestatment  
            ResultSet r= pstmt.executeQuery();
            if(r.next()){
            risultato=r.getBytes("documento");
            }
        } catch (SQLException ex) {
            System.getLogger(DatabaseDocumentoDiTesto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return risultato;
    }
        
    @Override
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
            System.getLogger(DatabaseDocumentoDiTesto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return L;
    }
    
    
    @Override
    public ArrayList<String> prendiNomiDocumentiFiltrati(LivelloPartita livello, ArrayList<Lingua> lingue) {
        ArrayList<String> nomiDocumenti = new ArrayList<>();

        // Costruisce i segnaposto per la clausola IN (?, ?, ...)
        StringBuilder placeholdersBuilder = new StringBuilder();
        for (int i = 0; i < lingue.size(); i++) {
            placeholdersBuilder.append("?::lingua_type");
            if (i < lingue.size() - 1) {
                placeholdersBuilder.append(", ");
            }
        }
        String placeholdersPerLingue = placeholdersBuilder.toString();
        String query = "SELECT nome_file FROM testo WHERE difficolta = ? AND lingua IN (" + placeholdersPerLingue + ")";

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, livello.getDbValue());

            for (int i = 0; i < lingue.size(); i++) {
                pstmt.setString(i + 2, lingue.get(i).getCodice());
            }

            try (ResultSet r = pstmt.executeQuery()) {
                while (r.next()) {
                    nomiDocumenti.add(r.getString("nome_file"));
                }
            }

        } catch (SQLException ex) {
            System.getLogger(DatabaseDocumentoDiTesto.class.getName())
                  .log(System.Logger.Level.ERROR, "Errore nel recupero dei nomi dei documenti filtrati", ex);
        }

        return nomiDocumenti;
    }
    
    /**
     * Classe statica interna che contiene l'istanza Singleton della classe {@code Database}.
     * <p>
     * L'istanza viene creata solo quando la classe {@code Holder} viene caricata, garantendo la thread safety.
     * </p>
     */
    private static class Holder {
        private static final DatabaseDocumentoDiTesto INSTANCE = new DatabaseDocumentoDiTesto();
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
    public static DatabaseDocumentoDiTesto getInstance() {
        return Holder.INSTANCE;
    }
    
}
