package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import it.unisa.diem.wordageddong17.model.ListaStopWords;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Implementazione del DAO per la gestione della lista di stop words.
 * 
 * Questa classe si occupa di fornire l'accesso ai dati relativi alle stop words,
 * utilizzate per filtrare parole non significative nei documenti di testo.
 * Adotta il pattern Singleton per garantire una singola istanza condivisa
 * durante l'esecuzione dell'applicazione.
 * 
 * @see DAOListaStopWords
 */
public class DatabaseStopWords implements DAOListaStopWords {
    
    private Database db;
    
    private DatabaseStopWords(){
        db = Database.getInstance();
    }
    /**
     * Carica o aggiorna un file di stopwords nel database.
     * 
     * Se un file con lo stesso nome esiste già, viene aggiornato con il nuovo contenuto.
     * Utilizza la clausola ON CONFLICT per gestire i duplicati.
     * 
     * @param listaStopWords il modello contenente le informazioni della lista di stopwords
     * @param documentoStopwords il contenuto del file di stopwords come array di byte
     * @return {@code true} se l'operazione è stata eseguita con successo, {@code false} altrimenti
     * 
     */
    @Override
    public boolean caricareStopwords(ListaStopWords listaStopWords, byte[] documentoStopwords) {
        String query= "INSERT INTO stopwords(\n" +
        "nome_file, documento, id_amministratore)\n" +
        "VALUES (?, ?, ?) ON conflict(nome_file) DO UPDATE SET documento=?,id_amministratore=?";
        
        boolean risultato= false;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, listaStopWords.getNomeFile()); // inserisce il nome nella prima posizione del preparestatment
            pstmt.setBytes(2, documentoStopwords);
            pstmt.setString(3, listaStopWords.getAmministratore());
            pstmt.setBytes(4, documentoStopwords);
            pstmt.setString(5, listaStopWords.getAmministratore());
            risultato= pstmt.executeUpdate()==1;// esegue il prepare statment
            
            } catch (SQLException ex) { 
            System.getLogger(DatabaseStopWords.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return risultato;
    }
    
    /**
     * Recupera il contenuto di un file di stopwords dal database.
     * 
     * Cerca il file specificato nella tabella stopwords e restituisce il suo contenuto
     * sotto forma di array di byte.
     * 
     * @param nomeFile il nome del file di stopwords da recuperare
     * @return l'array di byte contenente il documento, oppure {@code null} se il file non esiste
     * 
     */

    @Override
    public byte[] prendiStopwords(String nomeFile) {
        String query = "SELECT documento FROM stopwords WHERE nome_file = ?;";
        byte[] risultato = null;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeFile);
            try (ResultSet r = pstmt.executeQuery()) {
                if (r.next()) {
                    return r.getBytes("documento");
                }
            }
        } catch (SQLException ex) {
            System.getLogger(DatabaseStopWords.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return null;
    }

    /**
     * Recupera le informazioni sulla lista di stopwords dal database.
     * 
     * @param nomeFile il nome del file contenente le stopwords da recuperare
     * @return un oggetto ListaStopWords contenente le informazioni sulla lista di stopwords oppure {@code null} se non viene trovato
     */
    @Override
    public ListaStopWords prendiInfoStopwords(String nomeFile) {
        String query = "SELECT nome_file, id_amministratore, data_ultima_modifica FROM stopwords WHERE nome_file = ?;";
        ListaStopWords risultato = null;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeFile);
            try (ResultSet r = pstmt.executeQuery()) {
                if (r.next()) {
                    risultato = new ListaStopWords(
                        r.getString("nome_file"),
                        r.getString("id_amministratore"),
                        r.getTimestamp("data_ultima_modifica")
                    );
                }
            }
        } catch (SQLException ex) {
            System.getLogger(DatabaseStopWords.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return risultato;
    }

    private static class Holder {
        private static final DatabaseStopWords INSTANCE = new DatabaseStopWords();
    }
    
    
    /**
     * Restituisce l'unica istanza di {@code DatabaseStopWords} secondo il pattern Singleton.
     * <p>
     * Il metodo garantisce che venga creata una sola istanza di {@code DatabaseStopWords} durante
     * l'intera esecuzione dell'applicazione. L'istanza viene gestita tramite una classe interna statica,
     * assicurando un'inizializzazione lazy e thread-safe.
     * </p>
     *
     * @return l'istanza singleton di {@code DatabaseStopWords}
     */
    public static DatabaseStopWords getInstance() {
        return DatabaseStopWords.Holder.INSTANCE;
    }
    
    
}
