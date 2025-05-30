/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Mattia Sanzari
 */
public class DatabaseRegistrazioneLogin implements DosRegistrazione, DosLogin{

    private Database db;  
    
    
    private DatabaseRegistrazioneLogin() {
        db= Database.getInstance();
    }

    /**
    * La classe inserisce l' utente
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
            pstmt.setBytes(4, foto);
            pstmt.execute();// esegue il prepare statment
    }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
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
           return this.checkPassword(password, pwPresa); // controlla se la password è corretta 
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
    public List<Object> prendiUtente(String email) {
        ResultSet result=null;
        List<Object> L= new ArrayList<>();
        String query= "SELECT username, email, punteggio_migliore, foto_profilo, tipo\n" +
"	FROM utente where email= ?;";
         try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email); 
            result = pstmt.executeQuery();
            if(result.next()){
                L.add(result.getString("email"));
                L.add(result.getString("username"));
                L.add(result.getFloat("punteggio_migliore"));
                L.add(result.getBytes("foto_profilo"));
                L.add(result.getString("tipo"));
            }
         }catch(SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
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
