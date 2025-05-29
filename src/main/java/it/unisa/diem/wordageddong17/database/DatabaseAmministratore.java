/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mattia Sanzari
 */
public class DatabaseAmministratore implements DosAmministratore {

    private Database db;

    public DatabaseAmministratore() {
        db= Database.getInstance();
    }
    
    
    @Override
    public boolean CaricareStopwords(String email, byte[] documentoStopwords, String nomeFile) {
        String query= "INSERT INTO stopwords(\n" +
        "nome_file, documento, id_amministratore)\n" +
        "VALUES (?, ?, ?) ON conflict(nome_file) DO UPDATE SET documento=?,id_amministratore=?";
        
        boolean risultato= false;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeFile); // inserisce il nome  nella prima posizione del preparestatment
            pstmt.setBytes(2, documentoStopwords);
            pstmt.setString(3, email);
            pstmt.setBytes(4, documentoStopwords);
            pstmt.setString(5, email);
            risultato= pstmt.execute();// esegue il prepare statment
            
            }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return risultato;
    }

  

    @Override
    public byte[] PrendiStopwords(String nomeFile) {
        String query= "SELECT documento FROM stopwords where nome_file=?;";
        byte[] risultato=null;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeFile); // inserisce il nome  nella prima posizione del preparestatment
            ResultSet r = pstmt.executeQuery();
            risultato = r.getBytes("documento");
            }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return risultato;
    }

    @Override
    public boolean CancellaTesto(String nomeDocumento) {
       String query="DELETE FROM public.testo WHERE nome_file= ?;";
       boolean risultato=false;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeDocumento); // cancella il documento con questo nome
            risultato = pstmt.execute();
            }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return risultato;
    }


    @Override
    public boolean CaricareTesto(String email, String nomeFile, String difficolta, byte[] file) {
        String query= "INSERT INTO testo(\n" +
        "nome_file, id_amministratore, difficolta, documento)\n" +
        "VALUES (?, ?, ?, ?)";
        
        boolean risultato= false;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nomeFile); // inserisce l' utente nella prima posizione del preparestatment
            pstmt.setString(2, email);
            pstmt.setString(3, difficolta);
            pstmt.setBytes(4, file);
            risultato= pstmt.execute();// esegue il prepare statment
            
            }   catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return risultato;
    }

    @Override
    public byte[] PrendiTesto(String nomeDocumento) {
        
        String query="SELECT documento\n FROM testo where nome_file= ?;";
        
        byte[] risultato=null;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            
            pstmt.setString(1, nomeDocumento); // inserisce l' utente nella prima posizione del preparestatment  
            ResultSet r= pstmt.executeQuery();
            risultato=r.getBytes("documento");
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return risultato;
    }
    
    
         /**
     * Classe statica interna che contiene l'istanza Singleton della classe {@code Database}.
     * <p>
     * L'istanza viene creata solo quando la classe {@code Holder} viene caricata, garantendo la thread safety.
     * </p>
     */
    private static class Holder {
        private static final DatabaseAmministratore INSTANCE = new DatabaseAmministratore();
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
    public static DatabaseAmministratore getInstance() {
        return DatabaseAmministratore.Holder.INSTANCE;
    }

    
}
