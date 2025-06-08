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
 * Implementazione dell'interfaccia {@code DAOClassifica} per la gestione delle operazioni
 * di classifica nel database.
 * <p>
 * Questa classe fornisce metodi per recuperare e gestire le classifiche dei giocatori
 * raggruppate per livello di difficoltà. Implementa il pattern Singleton per garantire
 * una sola istanza della classe e utilizza query SQL per calcolare
 * i migliori punteggi per ogni utente.
 * </p>
 * 
 */

public class DatabaseClassifica implements DAOClassifica {
    
    /**
     * Istanza del database utilizzata per le operazioni di accesso ai dati.
     */
    private Database db;  
    
    
    /**
     * Costruttore privato della classe DatabaseClassifica.
     * <p>
     * Inizializza l'istanza del database utilizzando il pattern Singleton.
     * Il costruttore è privato per garantire l'implementazione del pattern Singleton.
     * </p>
     */
    
    private DatabaseClassifica(){
        db= Database.getInstance();
    }
    
    
    /**
     * Recupera la classifica dei giocatori per un determinato livello di difficoltà.
     * <p>
     * Questo metodo esegue una query SQL che raggruppa i punteggi per utente
     * e difficoltà, selezionando il miglior punteggio e la data più recente per ogni giocatore.
     * La classifica viene ordinata in ordine decrescente per punteggio.
     * </p>
     * 
     * @param difficolta il livello di difficoltà per cui recuperare la classifica.
     *                 
     * @return una lista di oggetti {@code Classifica} ordinata per punteggio decrescente.
     *         Restituisce una lista vuota se non ci sono punteggi per la difficoltà specificata
     */

    
    @Override
    public List<Classifica> prendiClassifica(LivelloPartita difficolta) {
        List<Classifica> L = new ArrayList<>();
        String query = "SELECT utente.username, data, MAX(punti) AS miglior_punteggio, difficolta\n"
                + "FROM punteggio\n"
                + "JOIN utente ON utente.email = punteggio.email_utente\n"
                + "WHERE difficolta = ?\n"
                + "GROUP BY utente.username, difficolta, data\n"
                + "ORDER BY miglior_punteggio DESC;";
    
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, difficolta.getDbValue());
        
            ResultSet result = pstmt.executeQuery();
            while(result.next()) {
                L.add(new Classifica(
                    result.getString("username"),
                    result.getTimestamp("data"),
                    result.getFloat("miglior_punteggio"),
                    LivelloPartita.fromDbValue(result.getString("difficolta"))));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseClassifica.class.getName()).log(Level.SEVERE, "Error loading leaderboard", ex);
        }
        return L;
    }
    
    /**
     * Classe statica interna che contiene l'istanza Singleton della classe {@code Database}.
     * <p>
     * L'istanza viene creata solo quando la classe {@code Holder} viene caricata, garantendo la thread safety.
     * </p>
     */
    private static class Holder {
        private static final DatabaseClassifica INSTANCE = new DatabaseClassifica();
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
    public static DatabaseClassifica getInstance() {
        return DatabaseClassifica.Holder.INSTANCE;
    }
    
    @Override
    public List<Classifica> recuperaCronologiaPartite(String email){
        List<Classifica> L = new ArrayList<>();
        String query = "SELECT utente.username, data, punti , difficolta\n"
                + "FROM punteggio\n"
                + "JOIN utente ON utente.email = punteggio.email_utente\n"
                + "WHERE punteggio.email_utente= ?\n"
                + "ORDER BY data DESC;";
    
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email);
        
            ResultSet result = pstmt.executeQuery();
            while(result.next()) {
                L.add(new Classifica(
                    result.getString("username"),
                    result.getTimestamp("data"),
                    result.getFloat("punti"),
                    LivelloPartita.fromDbValue(result.getString("difficolta"))));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseClassifica.class.getName()).log(Level.SEVERE, "Errore nel recupero della cronologia delle partite.", ex);
        }
        return L;
    }
    
    @Override
    public int recuperaNumeroPartite(String email, String difficoltà){
        int n = 0;
        String query = "SELECT COUNT(*) AS n_p\n"
                + "FROM punteggio\n"
                + "WHERE email_utente = ?\n"
                + "AND difficolta = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, difficoltà);
        
            ResultSet result = pstmt.executeQuery();
            if(result.next())
                n = result.getInt("n_p");
            }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseClassifica.class.getName()).log(Level.SEVERE, "Errore nel recupero del numero di partite", ex);
        }
        return n;
    }
    
    @Override
    public float recuperaMigliorPunteggio(String email, String difficoltà){
        float punteggio = 0;
        String query = "SELECT MAX(punti) AS max_p\n"
                + "FROM punteggio\n"
                + "WHERE email_utente= ?\n"
                + "AND difficolta = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, difficoltà);
        
            ResultSet result = pstmt.executeQuery();
            if(result.next())
                punteggio = result.getFloat("max_p");
            }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseClassifica.class.getName()).log(Level.SEVERE, "Errore nel recupero del numero di partite", ex);
        }
        return punteggio;
    }
    
    @Override
    public void inserisciPunteggio(String email, float punteggio, LivelloPartita difficoltà){
        String query = "INSERT INTO punteggio(email_utente, punti, difficolta) VALUES (?,?,?);";
        try(PreparedStatement pstmt = db.getConnection().prepareStatement(query)){
            pstmt.setString(1,email);
            pstmt.setFloat(2, punteggio);
            pstmt.setString(3,difficoltà.getDbValue());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.getLogger(DatabaseClassifica.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
