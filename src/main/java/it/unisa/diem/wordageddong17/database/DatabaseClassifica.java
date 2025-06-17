package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.interfaccia.DAOClassifica;
import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementazione dell'interfaccia {@code DAOClassifica} per la gestione delle classifiche dei giocatori nel database.
 *
 * Questa classe fornisce metodi per recuperare e gestire le classifiche dei giocatori
 * raggruppate per livello di difficoltà. Implementa il pattern Singleton per garantire
 * una sola istanza della classe e utilizza query SQL per calcolare i migliori punteggi per ogni utente.
 */
public class DatabaseClassifica implements DAOClassifica {
    
    /**
     * Istanza del database utilizzata per le operazioni di accesso ai dati.
     */
    private Database db;  
    
    /**
     * Costruttore privato della classe DatabaseClassifica.
     * Inizializza l'istanza del database utilizzando il pattern Singleton.
     * Il costruttore è privato per garantire l'implementazione del pattern Singleton.
     */
    private DatabaseClassifica(){
        db= Database.getInstance();
    }
    
    /**
     * Recupera la classifica dei giocatori per un determinato livello di difficoltà.
     * Questo metodo esegue una query SQL che raggruppa i punteggi per utente
     * e difficoltà, selezionando il miglior punteggio e la data più recente per ogni giocatore.
     * La classifica viene ordinata in ordine decrescente per punteggio.
     *
     * @param difficolta il livello di difficoltà per cui recuperare la classifica
     * @return una lista di oggetti {@code Classifica} ordinata per punteggio decrescente.
     *         Restituisce una lista vuota se non ci sono punteggi per la difficoltà specificata
     */
    @Override
    public List<Classifica> prendiClassifica(LivelloPartita difficolta) {
        List<Classifica> classifica = new ArrayList<>();
        String query = """
            SELECT 
                u.username,
                p.data,
                p.punti AS miglior_punteggio,
                p.difficolta
            FROM punteggio p
            JOIN utente u ON u.email = p.email_utente
            WHERE p.difficolta = ?
              AND p.punti = (
                  SELECT MAX(p2.punti)
                  FROM punteggio p2
                  WHERE p2.email_utente = p.email_utente
                    AND p2.difficolta = p.difficolta
              )
              AND p.data = (
                  SELECT MAX(p3.data)
                  FROM punteggio p3
                  WHERE p3.email_utente = p.email_utente
                    AND p3.difficolta = p.difficolta
                    AND p3.punti = p.punti
              )
            ORDER BY miglior_punteggio DESC;
            """;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, difficolta.getDbValue());
            try (ResultSet result = pstmt.executeQuery()) {
                while (result.next()) {
                    classifica.add(new Classifica(
                        result.getString("username"),
                        result.getTimestamp("data"),
                        result.getFloat("miglior_punteggio"),
                        LivelloPartita.fromDbValue(result.getString("difficolta"))
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseClassifica.class.getName()).log(Level.SEVERE, "Error loading leaderboard", ex);
        }

        return classifica;
    }


    
    /**
     * Classe statica interna che contiene l'istanza Singleton della classe {@code Database}.
     * L'istanza viene creata solo quando la classe {@code Holder} viene caricata, garantendo la thread safety.
     */
    private static class Holder {
        private static final DatabaseClassifica INSTANCE = new DatabaseClassifica();
    }
    
    /**
     * Restituisce l'istanza Singleton della classe {@code Database}.
     * Utilizza il pattern del Lazy Holder per garantire l'inizializzazione pigra e la thread safety
     * senza la necessità di sincronizzazione esplicita.
     *
     * @return l'istanza Singleton della classe {@code Database}.
     */
    public static DatabaseClassifica getInstance() {
        return DatabaseClassifica.Holder.INSTANCE;
    }
    
    /**
     * Recupera la cronologia delle partite per l'utente specificato.
     * 
     * Esegue una query sul database per ottenere i record relativi alle partite giocate dall'utente identificato dall'email.
     * La query effettua un JOIN tra le tabelle "punteggio" e "utente" per recuperare l'username, la data, il punteggio ed
     * la difficoltà della partita, ordinando i record in ordine decrescente per data.
     * Ogni risultato viene mappato in un oggetto {@link Classifica} e aggiunto a una lista.
     *
     * @param email l'email dell'utente per cui si vuole recuperare la cronologia delle partite
     * @return una lista di oggetti {@link Classifica} contenente i dati della cronologia delle partite;
     *         se nessun record viene trovato, ritorna una lista vuota
     */
    @Override
    public List<Classifica> recuperaCronologiaPartite(String email) {
        List<Classifica> classifiche = new ArrayList<>();
        String query = """
            SELECT utente.username, data, punti, difficolta
            FROM punteggio
            JOIN utente ON utente.email = punteggio.email_utente
            WHERE punteggio.email_utente = ?
            ORDER BY data DESC;
            """;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet result = pstmt.executeQuery()) {
                while (result.next()) {
                    classifiche.add(new Classifica(
                        result.getString("username"),
                        result.getTimestamp("data"),
                        result.getFloat("punti"),
                        LivelloPartita.fromDbValue(result.getString("difficolta"))
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseClassifica.class.getName()).log(Level.SEVERE, "Errore nel recupero della cronologia delle partite.", ex);
        }

        return classifiche;
    }


    /**
     * Recupera il numero di partite giocate da un utente per una determinata difficoltà.
     * Esegue una query sul database che conta il numero di record nella tabella "punteggio"
     * per l'utente identificato dall'email e per il livello di difficoltà specificato.
     *
     * @param email l'email dell'utente
     * @param difficoltà la difficoltà per cui contare le partite (ad esempio, "facile", "medio", "difficile")
     * @return il numero di partite giocate che soddisfano i criteri specificati; se nessun record viene trovato, ritorna 0
     */
    @Override
    public int recuperaNumeroPartite(String email, String difficoltà) {
        int n = 0;
        String query = """
            SELECT COUNT(*) AS n_p
            FROM punteggio
            WHERE email_utente = ?
              AND difficolta = ?
            """;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, difficoltà);

            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    n = result.getInt("n_p");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseClassifica.class.getName())
                  .log(Level.SEVERE, "Errore nel recupero del numero di partite", ex);
        }
        return n;
    }

    /**
     * Recupera il miglior punteggio ottenuto da un utente per una specifica difficoltà.
     * Esegue una query che seleziona il massimo punteggio (usando la funzione aggregate MAX)
     * dalla tabella "punteggio" per l'utente identificato dall'email e in base al livello di difficoltà.
     *
     * @param email l'email dell'utente
     * @param difficoltà la difficoltà per cui recuperare il miglior punteggio (ad esempio, "facile", "medio", "difficile")
     * @return il miglior punteggio ottenuto; se nessun record viene trovato, ritorna 0
     */
    @Override
    public float recuperaMigliorPunteggio(String email, String difficoltà) {
        float punteggio = 0;
        String query = """
            SELECT MAX(punti) AS max_p
            FROM punteggio
            WHERE email_utente = ?
              AND difficolta = ?
            """;
        
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, difficoltà);

            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    punteggio = result.getFloat("max_p");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseClassifica.class.getName()).log(Level.SEVERE, "Errore nel recupero del miglior punteggio", ex);
        }
        return punteggio;
    }

    /**
     * Inserisce un nuovo punteggio nel database per un determinato utente.
     * Esegue una query di inserimento (INSERT) nella tabella "punteggio" utilizzando i parametri:
     * l'email dell'utente, il punteggio ottenuto e il livello di difficoltà della partita.
     * Il livello di difficoltà viene convertito nel suo valore di database tramite {@code getDbValue()}.
     *
     * @param email l'email dell'utente che ha ottenuto il punteggio
     * @param punteggio il punteggio da inserire nel database
     * @param difficoltà il livello di difficoltà della partita (rappresentato come {@link LivelloPartita})
     */
    @Override
    public void inserisciPunteggio(String email, float punteggio, LivelloPartita difficoltà) {
        String query = "INSERT INTO punteggio(email_utente, punti, difficolta) VALUES (?,?,?);";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setFloat(2, punteggio);
            pstmt.setString(3, difficoltà.getDbValue());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseClassifica.class.getName()).log(Level.SEVERE, "Errore nell'inserimento del punteggio", ex);
        }  
    }

}
