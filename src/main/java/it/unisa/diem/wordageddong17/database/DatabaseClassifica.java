package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.interfaccia.DAOClassifica;
import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestione delle classifiche e della cronologia dei punteggi nel database.
 */
public class DatabaseClassifica implements DAOClassifica {

    private static final Logger LOGGER = Logger.getLogger(DatabaseClassifica.class.getName());
    private final Database db;

    private DatabaseClassifica() {
        this.db = Database.getInstance();
    }

    /**
     * Recupera la classifica dei migliori punteggi per una determinata difficoltà.
     *
     * @param difficolta il livello di partita
     * @return List con le entry di classifica ordinate decrescenti per miglior punteggio
     */
    @Override
    public List<Classifica> prendiClassifica(LivelloPartita difficolta) {
        List<Classifica> classifica = new ArrayList<>();

        String query = """
            SELECT classifica.username,
                   classifica.miglior_punteggio,
                   classifica.data,
                   classifica.difficolta
            FROM (
                SELECT DISTINCT ON (punteggio.email_utente)
                       utente.username,
                       punteggio.punti AS miglior_punteggio,
                       punteggio.data,
                       punteggio.difficolta
                FROM punteggio
                JOIN utente ON punteggio.email_utente = utente.email
                WHERE punteggio.difficolta = ?
                ORDER BY punteggio.email_utente, punteggio.punti DESC, punteggio.data ASC
            ) AS classifica
            ORDER BY classifica.miglior_punteggio DESC;
            """;

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, difficolta.getDbValue());

            try (ResultSet result = pstmt.executeQuery()) {
                while (result.next()) {
                    classifica.add(new Classifica(
                            result.getString("username"),
                            result.getTimestamp("data"),
                            result.getFloat("miglior_punteggio"),
                            LivelloPartita.fromDbValue(result.getString("difficolta").trim())
                    ));
                }
            }
            return classifica;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore recupero classifica", ex);
            throw new DataAccessException("Impossibile recuperare la classifica dal database.", ex);
        }
    }

    /**
     * Recupera la cronologia delle partite di un determinato utente.
     *
     * @param email l'email dell'utente
     * @return lista ordinata per data decrescente
     */
    @Override
    public List<Classifica> recuperaCronologiaPartite(String email) {
        List<Classifica> classifiche = new ArrayList<>();

        String query = """
            SELECT punteggio.punti,
                   punteggio.data,
                   punteggio.difficolta
            FROM punteggio
            WHERE punteggio.email_utente = ?
            ORDER BY data DESC;
            """;

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet result = pstmt.executeQuery()) {
                while (result.next()) {
                    classifiche.add(new Classifica(
                            result.getTimestamp("data"),
                            result.getFloat("punti"),
                            LivelloPartita.fromDbValue(result.getString("difficolta").trim())
                    ));
                }
            }
            return classifiche;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore recupero cronologia partite", ex);
            throw new DataAccessException("Impossibile recuperare la cronologia partite dal database.", ex);
        }
    }

    /**
     * Recupera il miglior punteggio di un utente per un dato livello di difficoltà.
     *
     * @param email l'email dell'utente
     * @param difficolta la difficoltà richiesta
     * @return il miglior punteggio, oppure 0 se assente
     */
    @Override
    public float recuperaMigliorPunteggio(String email, String difficolta) {
        String query = """
            SELECT MAX(punti) AS miglior_punteggio
            FROM punteggio
            WHERE email_utente = ?
              AND difficolta = ?
            """;

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, difficolta);

            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    return result.getFloat("miglior_punteggio");
                }
            }
            return 0f;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore recupero miglior punteggio", ex);
            throw new DataAccessException("Impossibile recuperare il miglior punteggio dal database.", ex);
        }
    }

    /**
     * Conta il numero di partite disputate da un utente per un livello.
     *
     * @param email l'email dell'utente
     * @param difficolta il livello di difficoltà
     * @return numero di partite
     */
    @Override
    public int recuperaNumeroPartite(String email, String difficolta) {
        String query = """
            SELECT COUNT(*) AS numero_partite
            FROM punteggio
            WHERE email_utente = ?
              AND difficolta = ?
            """;

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, difficolta);

            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    return result.getInt("numero_partite");
                }
            }
            return 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore conteggio partite", ex);
            throw new DataAccessException("Impossibile recuperare il numero di partite dal database.", ex);
        }
    }

    /**
     * Inserisce un nuovo punteggio nel database.
     *
     * @param email email dell'utente
     * @param punteggio punteggio totalizzato
     * @param difficolta livello di gioco
     */
    @Override
    public void inserisciPunteggio(String email, float punteggio, LivelloPartita difficolta) {
        String query = "INSERT INTO punteggio(email_utente, punti, difficolta) VALUES (?, ?, ?);";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setFloat(2, punteggio);
            pstmt.setString(3, difficolta.getDbValue());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore salvataggio punteggio", ex);
            throw new DataAccessException("Impossibile salvare il punteggio nel database.", ex);
        }
    }

    private static class Holder {
        private static final DatabaseClassifica INSTANCE = new DatabaseClassifica();
    }

    /**
     * Restituisce l'istanza singleton di {@link DatabaseClassifica}.
     *
     * @return istanza condivisa
     */
    public static DatabaseClassifica getInstance() {
        return Holder.INSTANCE;
    }
}
