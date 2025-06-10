package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import it.unisa.diem.wordageddong17.model.TipoUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import java.io.ByteArrayInputStream;
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

    private final Database db;

    private DatabaseUtente() {
        db = Database.getInstance();
    }

    @Override
    public boolean modificaUsername(String email, String username) {
        String query = "UPDATE utente SET username=? WHERE email=?";
        boolean risultato = false;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            int updated = pstmt.executeUpdate();
            risultato = (updated == 1);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtente.class.getName()).log(Level.SEVERE, null, ex);
        }

        return risultato;
    }

    @Override
    public boolean modificaFotoProfilo(String email, byte[] fotoProfilo) {
        Connection conn = db.getConnection();

        try {
            conn.setAutoCommit(false); // Inizia la transazione

            String sql = "UPDATE utente SET foto_profilo = ? WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                if (fotoProfilo != null) {
                    stmt.setBytes(1, fotoProfilo); // Usa setBytes direttamente con byte[]
                } else {
                    stmt.setNull(1, Types.BINARY); // Usa Types.BINARY invece di BLOB per bytea
                }
                stmt.setString(2, email);

                int righe = stmt.executeUpdate();
                if (righe != 1) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseUtente.class.getName()).log(Level.SEVERE, "Errore durante rollback", ex);
            }
            Logger.getLogger(DatabaseUtente.class.getName()).log(Level.SEVERE, "Errore aggiornamento foto", e);
            return false;

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                Logger.getLogger(DatabaseUtente.class.getName()).log(Level.WARNING, "Errore ripristino autoCommit", e);
            }
        }
    }

    private byte[] getFotoProfilo(String email, Connection conn) throws SQLException {
        String sql = "SELECT foto_profilo FROM utente WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("foto_profilo");
            }
        }
        return null;
    }
    
    
    /**
     * Inserisce un nuovo utente nel database.
     * <p>
     * Questo metodo registra un nuovo utente nel sistema crittografando automaticamente
     * la password utilizzando BCrypt prima di salvarla nel database. La foto profilo
     * è opzionale e può essere null.
     * </p>
     * 
     * @param username il nome utente univoco per l'utente
     * @param email l'indirizzo email dell'utente 
     * @param password la password in chiaro dell'utente (verrà automaticamente crittografata)
     * @param foto l'immagine del profilo dell'utente come array di byte, può essere {@code null}
     * 
     */
    @Override
    public void inserisciUtente(String username, String email, String password, byte[] foto) {
        
        String passwordCriptata = this.hashPassword(password);
        String query= "Insert into utente(username, email, password, foto_profilo) values (?,?,?,?) ";
        
            try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, username); // inserisce l' utente nella prima posizione del preparestatment
            pstmt.setString(2, email);
            pstmt.setString(3, passwordCriptata);
            if (foto != null) {
                pstmt.setBytes(4, foto);
            } else {
                pstmt.setNull(4, java.sql.Types.BINARY);
            }
            pstmt.execute();// esegue il prepare statment
    }   catch (SQLException ex) {  
            System.getLogger(DatabaseUtente.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }  
    }
    
    /**
     * Recupera il nome utente associato a un indirizzo email.
     * <p>
     * Questo metodo esegue una query per trovare il nome utente corrispondente
     * all'email specificata. 
     * </p>
     * 
     * @param email l'indirizzo email dell'utente di cui recuperare il nome utente
     * @return il nome utente associato all'email, oppure {@code null} se non trovato
     * 
     * 
     */
    @Override
    public String prendiUsername(String email) {
             
        String query= "Select username from utente where email= ? ";
        String usernamePresa=null;
            try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email); // inserisce l' utente nella prima posizione del preparestatment
            ResultSet result = pstmt.executeQuery();
            if(result.next()){
                usernamePresa = result.getString("username");
            }
    }   catch (SQLException ex) {   
            System.getLogger(DatabaseUtente.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
            return usernamePresa;
    }
    
    /**
     * Verifica se la password in chiaro fornita corrisponde alla password memorizzata per l'utente identificato dall'email.
     * <p>
     * Il metodo esegue una query sulla tabella "utente" per recuperare la password associata all'email specificata.
     * Se viene trovato un record, il valore della password (presumibilmente un hash generato con BCrypt) viene confrontato
     * con la password in chiaro fornita utilizzando il metodo {@code checkPassword}. Se la password corrisponde, il metodo
     * restituisce {@code true}, altrimenti {@code false}. Se non viene trovato alcun record per l'email oppure la password recuperata
     * è {@code null}, viene restituito {@code false}.
     * </p>
     *
     * @param email    l'email dell'utente di cui verificare la password
     * @param password la password in chiaro da verificare
     * @return {@code true} se la password fornita corrisponde a quella presente nel database, {@code false} altrimenti
     */
    @Override
    public boolean verificaPassword(String email, String password) {
        String query = "Select password from utente where email= ? ";
        String pwPresa = null;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet result = pstmt.executeQuery();
            if (result.next()) {
                pwPresa = result.getString("password");
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.getLogger(DatabaseUtente.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        if (pwPresa == null) return false;
        return this.checkPassword(password, pwPresa);
    }

    /**
     * Genera l'hash BCrypt della password fornita.
     * <p>
     * Questo metodo utilizza la libreria BCrypt per hashare in modo sicuro la password. In particolare, viene
     * generato un salt casuale con {@code BCrypt.gensalt()} e la password viene hashata tramite {@code BCrypt.hashpw()}.
     * L'hash risultante potrà essere memorizzato nel database per verifiche future.
     * </p>
     *
     * @param password la password in chiaro da hashare
     * @return la stringa contenente l'hash BCrypt della password
     */
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    
    /**
    * 
    * @brief Verifica corrispondenza tra password criptata e password in chiaro
    * 
    * @pre  la password!= null
    * @post restituisce il risultato del confronto tra le 2 stringhe
    * 
    * 
    * @param password è la password non criptata passata alla funzione 
    * @param hashed è la password criptata passata alla funzione 
    * @return  true se la password è uguale a hashed, se sono diverse false
    */

    private boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
    
    /**
     * Recupera un utente dal database in base all'email specificata.
     * <p>
     * Esegue una query sulla tabella "utente" per recuperare le informazioni relative a:
     * <ul>
     *   <li><strong>username</strong> – il nome utente;</li>
     *   <li><strong>email</strong> – l'indirizzo email;</li>
     *   <li><strong>foto_profilo</strong> – i byte dell'immagine del profilo (possono essere {@code null});</li>
     *   <li><strong>tipo</strong> – il tipo dell'utente, che verrà convertito in un oggetto {@link TipoUtente}.</li>
     * </ul>
     * Se il campo {@code foto_profilo} risulta {@code null}, verrà creato un oggetto {@code Utente} senza immagine.
     * Altrimenti, l'oggetto {@code Utente} verrà creato includendo l'immagine.
     * Se nessun record viene trovato, il metodo restituisce {@code null}.
     * </p>
     *
     * @param email l'indirizzo email dell'utente da recuperare
     * @return un oggetto {@code Utente} contenente i dati dell'utente, oppure {@code null} se l'utente non viene trovato
     */
    @Override
    public Utente prendiUtente(String email) {
        Utente u = null;
        String query = "SELECT username, email, foto_profilo, tipo FROM utente WHERE email = ?;";

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    byte[] fotoProfilo = result.getBytes("foto_profilo");

                    if (fotoProfilo == null) {
                        u = new Utente(
                            result.getString("username"),
                            result.getString("email"),
                            TipoUtente.valueOf(result.getString("tipo").trim())
                        );
                    } else {
                        u = new Utente(
                            result.getString("username"),
                            result.getString("email"),
                            fotoProfilo,
                            TipoUtente.valueOf(result.getString("tipo").trim())
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            System.getLogger(DatabaseUtente.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return u;
    }


    // Singleton pattern con classe interna
    private static class Holder {
        private static final DatabaseUtente INSTANCE = new DatabaseUtente();
    }

    public static DatabaseUtente getInstance() {
        return Holder.INSTANCE;
    }
    
}
