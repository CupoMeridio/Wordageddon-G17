package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import it.unisa.diem.wordageddong17.model.TipoUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Gestione operazioni utente come modifica del nome utente o della foto profilo.
 */
public class DatabaseUtente implements DAOUtente {

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtente.class.getName());
    private final Database db;

    private DatabaseUtente() {
        db = Database.getInstance();
    }

    /**
     * Modifica il nome utente dell'utente specificato.
     *
     * @param utente l'oggetto Utente contenente l'email e il nuovo nome utente da impostare
     * @return true se l'aggiornamento ha avuto successo, false altrimenti
     */
    @Override
    public boolean modificaUsername(Utente utente) {
        String query = "UPDATE utente SET username=? WHERE email=?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, utente.getUsername());
            pstmt.setString(2, utente.getEmail());
            int updated = pstmt.executeUpdate();
            if (updated == 0) {
                throw new DataAccessException("Nessun utente trovato con l'email: " + utente.getEmail());
            }
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore modifica username", ex);
            if ("23505".equals(ex.getSQLState())) {
                throw new DuplicateUserException("Il nome utente '" + utente.getUsername() + "' è già utilizzato.");
            }
            throw new DataAccessException("Errore durante la modifica del nome utente.", ex);
        }
    }

    /**
     * Modifica la foto profilo dell'utente specificato.
     * Se {@code fotoProfilo} è null, la foto profilo viene rimossa.
     * L'operazione è eseguita in una transazione su connessione dedicata.
     *
     * @param utente l'oggetto Utente contenente l'email e la nuova foto profilo
     * @return true se l'aggiornamento ha avuto successo
     */
    @Override
    public boolean modificaFotoProfilo(Utente utente) {
        String sql = "UPDATE utente SET foto_profilo = ? WHERE email = ?";

        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                if (utente.getFotoProfilo() != null) {
                    stmt.setBytes(1, utente.getFotoProfilo());
                } else {
                    stmt.setNull(1, Types.BINARY);
                }
                stmt.setString(2, utente.getEmail());

                int righe = stmt.executeUpdate();
                if (righe != 1) {
                    conn.rollback();
                    throw new DataAccessException("Impossibile aggiornare la foto: utente non trovato.");
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore aggiornamento foto profilo", e);
            throw new DataAccessException("Errore durante l'aggiornamento della foto profilo nel database.", e);
        }
    }

    /**
     * Inserisce un nuovo utente nel database.
     *
     * @param utente l'oggetto Utente contenente le informazioni dell'utente da inserire
     * @param password la password in chiaro dell'utente (verrà automaticamente crittografata)
     */
    @Override
    public void inserisciUtente(Utente utente, String password) {
        String passwordCriptata = this.hashPassword(password);
        String query = "INSERT INTO utente(username, email, password, foto_profilo) VALUES (?,?,?,?)";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, utente.getUsername());
            pstmt.setString(2, utente.getEmail());
            pstmt.setString(3, passwordCriptata);
            if (utente.getFotoProfilo() != null) {
                pstmt.setBytes(4, utente.getFotoProfilo());
            } else {
                pstmt.setNull(4, Types.BINARY);
            }
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore inserimento utente", ex);
            if ("23505".equals(ex.getSQLState())) {
                throw new DuplicateUserException("Email o username già registrati nel sistema.");
            }
            throw new DataAccessException("Errore di connessione o salvataggio dell'utente.", ex);
        }
    }

    /**
     * Recupera il nome utente associato a un indirizzo email.
     *
     * @param email l'indirizzo email dell'utente
     * @return il nome utente associato all'email, oppure {@code null} se non trovato
     */
    @Override
    public String prendiUsername(String email) {
        String query = "SELECT username FROM utente WHERE email = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    return result.getString("username");
                }
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore recupero username", ex);
            throw new DataAccessException("Errore nel recupero del nome utente.", ex);
        }
    }

    /**
     * Verifica se la password in chiaro fornita corrisponde alla password memorizzata per l'utente.
     *
     * @param email l'email dell'utente
     * @param password la password in chiaro da verificare
     * @return {@code true} se la password corrisponde, {@code false} se l'utente non esiste o la password è errata
     * @throws DataAccessException se il database è irraggiungibile o si verifica un errore SQL
     */
    @Override
    public boolean verificaPassword(String email, String password) {
        String query = "SELECT password FROM utente WHERE email = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    String hash = result.getString("password");
                    return hash != null && this.checkPassword(password, hash.trim());
                }
            }
            return false;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore verifica password", ex);
            throw new DataAccessException("Impossibile verificare le credenziali sul database.", ex);
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    private boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    /**
     * Recupera un utente dal database in base all'email specificata.
     *
     * @param email l'indirizzo email dell'utente da recuperare
     * @return un oggetto {@code Utente} contenente i dati dell'utente, oppure {@code null} se non trovato
     */
    @Override
    public Utente prendiUtente(String email) {
        String query = "SELECT username, email, foto_profilo, tipo FROM utente WHERE email = ?;";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    byte[] fotoProfilo = result.getBytes("foto_profilo");
                    String username = result.getString("username");
                    String emailUtente = result.getString("email");
                    TipoUtente tipo = TipoUtente.valueOf(result.getString("tipo").trim());

                    return new Utente(username, emailUtente, fotoProfilo, tipo);
                }
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore recupero utente", ex);
            throw new DataAccessException("Impossibile recuperare i dati dell'utente dal database.", ex);
        }
    }

    private static class Holder {
        private static final DatabaseUtente INSTANCE = new DatabaseUtente();
    }

    public static DatabaseUtente getInstance() {
        return Holder.INSTANCE;
    }
}
