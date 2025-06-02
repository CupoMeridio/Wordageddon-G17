
package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.model.TipoUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementazione delle interfacce {@code DosRegistrazione} e {@code DosLogin} per la gestione
 * delle operazioni di registrazione e autenticazione degli utenti nel database.
 * <p>
 * Questa classe fornisce funzionalità complete per:
 * <ul>
 * <li>Registrazione di nuovi utenti con crittografia delle password</li>
 * <li>Autenticazione degli utenti esistenti</li>
 * <li>Recupero di informazioni utente dal database</li>
 * <li>Gestione sicura delle password utilizzando BCrypt</li>
 * </ul>
 * </p>
 * <p>
 * Implementa il pattern Singleton per garantire una sola istanza della classe
 * e utilizza BCrypt per la crittografia sicura delle password.
 * </p>
 * 
 * @author Mattia Sanzari
 */
public class DatabaseRegistrazioneLogin implements DosRegistrazione, DosLogin{

    /**
     * Istanza del database utilizzata per le operazioni di accesso ai dati.
     */
    
    private Database db;  
    
    /**
     * Costruttore privato della classe DatabaseRegistrazioneLogin.
     * <p>
     * Inizializza l'istanza del database utilizzando il pattern Singleton.
     * Il costruttore è privato per garantire l'implementazione del pattern Singleton.
     * </p>
     */

    private DatabaseRegistrazioneLogin() {
        db= Database.getInstance();
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
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
            return usernamePresa;
    }

    @Override
    public boolean verificaPassword(String email, String password) {
    
        String query= "Select password from utente where email= ? ";
        String pwPresa=null;
            try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email); 
            ResultSet result = pstmt.executeQuery();
            if(result.next()){
                pwPresa = result.getString("password");
            }else{
                return false;
            }
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (pwPresa == null) return false;
        return this.checkPassword(password, pwPresa);
    }
        
    
        /**
    * 
    * @brief Cripta la password passata dall' utente 
    * 
    * @pre  la password non può essere vuota o nulla
    * @post viene criptata la password inserita
    * 
    * @param password è la password da criptare
    * @return  Una stringa criptata
    */

    
    private  String hashPassword(String password) {
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

    @Override
    public Utente prendiUtente(String email) {
        ResultSet result=null;
        Utente u=null;
        String query= "SELECT username, email, foto_profilo, tipo\n" +
"	FROM utente where email= ?;";
         try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email); 
            result = pstmt.executeQuery();
            if(result.next()){
                byte[] fotoProfilo = result.getBytes("foto_profilo");
                if(fotoProfilo == null){
                u=new Utente(result.getString("username"),
                        result.getString("email"),
                        TipoUtente.valueOf(result.getString("tipo").trim())
                        );
                }else{
                    u=new Utente(
                        result.getString("username"),
                        result.getString("email"),
                        fotoProfilo,
                        TipoUtente.valueOf(result.getString("tipo").trim())
                    );
                } 
            }
         }catch(SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }       
         return u;
    }
    
     /**
     * Classe statica interna che contiene l'istanza Singleton della classe {@code Database}.
     * <p>
     * L'istanza viene creata solo quando la classe {@code Holder} viene caricata, garantendo la thread safety.
     * </p>
     */
    private static class Holder {
        private static final DatabaseRegistrazioneLogin INSTANCE = new DatabaseRegistrazioneLogin();
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
    public static DatabaseRegistrazioneLogin getInstance() {
        return DatabaseRegistrazioneLogin.Holder.INSTANCE;
    }

   

      
}
