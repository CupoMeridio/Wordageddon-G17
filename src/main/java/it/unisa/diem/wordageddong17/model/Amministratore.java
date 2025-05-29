/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.database.DatabaseAmministratore;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;

/**
 *
 * @author Mattia Sanzari
 */
public class Amministratore extends Utente {
    
    DatabaseAmministratore db;
    
    public Amministratore(String username, String email, float punteggioMigliore, byte[] fotoProfilo, String tipo) {
        super(username, email, punteggioMigliore, fotoProfilo, tipo);
        db= DatabaseAmministratore.getInstance();
    }
    
    public void caricaTesto(String Nomefile, String Difficolta, String path){
    
        byte[] documentoDaCaricare=trasformaDocumento(path);
       
       if(db.CaricareTesto(this.getEmail(), Nomefile, Difficolta, documentoDaCaricare)){
           System.out.println("Documento caricato");
       }else{
            System.out.println("Documento non caricato");
       }
    }
    
    public void CaricareStopwords(String nomefile, String path){
        
        byte[] documentoStopwords=trasformaDocumento(path);
        if(db.CaricareStopwords(this.getEmail(), documentoStopwords, nomefile)){
             System.out.println("Stopwords caricate");
        }else{
             System.out.println("Stopwords non caricate");
        }
    }
    
    private byte[] trasformaDocumento(String path){
        
        byte[] documentoDaCaricare=null;
        try {
            documentoDaCaricare = Files.readAllBytes(Paths.get(path));
        } catch (IOException ex) {
            Logger.getLogger(Amministratore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documentoDaCaricare;
    }
    
    
    public byte[] PrendiDocumentoStopwords(String nomeFile){
        byte[] risultato= db.PrendiStopwords(nomeFile);
        return risultato; 
    }
    public byte[] PrendiTesto(String nomeDocumento){
        byte[] risultato = db.PrendiTesto(nomeDocumento);
        return risultato; 
    }
    public void CancellaTesto(String nomeDocumento){
        
        boolean risultato = db.CancellaTesto(nomeDocumento);
        
        if(risultato){
            System.out.println("Cancellazione testo effettuata");
        }else{
            System.out.println("Cancellazione testo non effettuata");
        }
    }
}
