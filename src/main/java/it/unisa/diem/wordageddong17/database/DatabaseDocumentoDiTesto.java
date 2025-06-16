package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementazione del DAO (Data Access Object) per la gestione dei documenti di testo.
 * Questa classe utilizza un'istanza singleton di {@link Database} per operazioni di accesso e manipolazione dei dati.
 */
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
     * 
     * Inserisce un nuovo documento nella tabella testo con le informazioni specificate.
     * Il documento viene associato all'amministratore che lo carica.
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
     * 
     * Cerca il documento specificato nella tabella testo e restituisce il suo contenuto
     * sotto forma di array di byte.
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
        
    /**
     * Recupera tutti i documenti testuali presenti nel database.
     * Questo metodo esegue una query sulla tabella "testo" per ottenere i seguenti campi:
     * - <code>nome_file</code>: il nome del file.
     * - <code>id_amministratore</code>: l'identificativo dell'amministratore che ha inserito il documento.
     * - <code>difficolta</code>: il livello di difficoltà del documento, convertito in un oggetto {@link LivelloPartita} tramite {@code LivelloPartita.fromDbValue()}.
     * - <code>lingua</code>: la lingua del documento, convertita in un oggetto {@link Lingua} tramite {@code Lingua.fromCodice()}.
     * Per ogni record viene creato un oggetto {@link DocumentoDiTesto} ed aggiunto a una lista.
     * Se si verifica un'eccezione {@link SQLException} durante l'esecuzione della query, questa verrà loggata
     * e il metodo restituirà una lista eventualmente vuota.
     *
     * @return una {@link ArrayList} di {@link DocumentoDiTesto} contenente tutti i documenti recuperati dal database
     */
    @Override
    public ArrayList<DocumentoDiTesto> prendiTuttiIDocumenti() {
        ArrayList<DocumentoDiTesto> L = new ArrayList<>();
        String query = "SELECT nome_file, id_amministratore, difficolta, lingua FROM testo";

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            ResultSet result = pstmt.executeQuery();
            while (result.next()) {
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

    
    /**
     * Recupera i nomi dei documenti filtrati in base al livello di difficoltà e alle lingue specificate.
     * Questo metodo esegue una query sulla tabella "testo" per selezionare il nome dei file (campo "nome_file")
     * che corrispondono a:
     * - Un determinato livello di difficoltà, ottenuto tramite {@code livello.getDbValue()}.
     * - Un insieme di lingue, passato come lista di {@link Lingua}. Per ogni lingua,
     *   viene dinamicamente costruito un segnaposto della forma "?::lingua_type" per inserirla nella clausola IN.
     * Vengono quindi impostati i parametri nel {@link PreparedStatement} in modo che il primo parametro
     * rappresenti il livello e i successivi, partendo dall'indice 2, rappresentino i codici delle lingue.
     * Se la query esegue correttamente, i nomi dei documenti vengono aggiunti a una lista, che viene poi restituita.
     * In caso di errore, l'eccezione {@link SQLException} viene loggata e il metodo restituisce la lista,
     * eventualmente vuota.
     *
     * @param livello il livello di partita (difficoltà) utilizzato come filtro (es. FACILE, MEDIO, DIFFICILE)
     * @param lingue una lista di oggetti {@link Lingua} per filtrare i documenti in base alla lingua
     * @return un {@link ArrayList} contenente i nomi dei documenti filtrati; se nessun documento viene trovato, viene restituita una lista vuota
     */

    @Override
    public Map<String,Lingua> prendiNomiDocumentiFiltrati(LivelloPartita livello, ArrayList<Lingua> lingue) {
        Map<String,Lingua> nomiDocumenti = new HashMap<>();

        // Costruisce i segnaposto per la clausola IN (?, ?, ...)
        StringBuilder placeholdersBuilder = new StringBuilder();
        for (int i = 0; i < lingue.size(); i++) {
            placeholdersBuilder.append("?::lingua_type");
            if (i < lingue.size() - 1) {
                placeholdersBuilder.append(", ");
            }
        }
        String placeholdersPerLingue = placeholdersBuilder.toString();
        String query = "SELECT nome_file,lingua FROM testo WHERE difficolta = ? AND lingua IN (" + placeholdersPerLingue + ")";

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            // Imposta il parametro relativo al livello
            pstmt.setString(1, livello.getDbValue());

            // Imposta i parametri relativi alle lingue (a partire dall'indice 2)
            for (int i = 0; i < lingue.size(); i++) {
                pstmt.setString(i + 2, lingue.get(i).getCodice());
            }

            try (ResultSet r = pstmt.executeQuery()) {
                while (r.next()) {
                   String codiceLingua = r.getString("lingua");
                   Lingua lingua = Lingua.fromCodice(codiceLingua);
                   nomiDocumenti.put(r.getString("nome_file"), lingua);
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
     * 
     * L'istanza viene creata solo quando la classe {@code Holder} viene caricata, garantendo la thread safety.
     */
    private static class Holder {
        private static final DatabaseDocumentoDiTesto INSTANCE = new DatabaseDocumentoDiTesto();
    }

    /**
     * Restituisce l'istanza Singleton della classe {@code Database}.
     * Utilizza il pattern del Lazy Holder per garantire l'inizializzazione pigra e la thread safety
     * senza la necessità di sincronizzazione esplicita.
     *
     * @return l'istanza Singleton della classe {@code Database}.
     */
    public static DatabaseDocumentoDiTesto getInstance() {
        return Holder.INSTANCE;
    }
    
}
