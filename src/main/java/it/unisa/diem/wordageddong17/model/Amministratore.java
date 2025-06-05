package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.database.DatabaseAmministratore;
import it.unisa.diem.wordageddong17.database.DosAmministratore;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe che rappresenta un utente amministratore.
 * Estende la classe Utente e fornisce funzionalità specifiche per la gestione
 * di testi e stopwords nel database.
 * 
 * L'amministratore può:
 * - Caricare testi con diversi livelli di difficoltà
 * - Gestire le stopwords
 * - Recuperare documenti dal database
 * - Cancellare testi esistenti
 * 
 * @author Mattia Sanzari
 */
public class Amministratore extends Utente {
    
    /**
     * Riferimento al Data Access Object per le operazioni di database
     * specifiche dell'amministratore.
     * Nota: dovrebbe dipendere dall'interfaccia per rispettare il principio
     * di inversione delle dipendenze.
     */
    DosAmministratore db;// deve dipendere dalla interfaccia 
    
    /**
     * Costruttore della classe Amministratore.
     * Inizializza un nuovo amministratore con i parametri specificati
     * e ottiene l'istanza del database amministratore.
     * 
     * @param username il nome utente dell'amministratore
     * @param email l'indirizzo email dell'amministratore
     * @param fotoProfilo l'immagine del profilo come array di byte
     * @param tipo il tipo di utente (dovrebbe essere TipoUtente.AMMINISTRATORE)
     */
    public Amministratore(String username, String email, byte[] fotoProfilo, TipoUtente tipo) {
        super(username, email, fotoProfilo, tipo);
        db= DatabaseAmministratore.getInstance();
    }
    
    /**
     * Carica un nuovo testo nel database con le informazioni specificate.
     * Il metodo legge il file dal percorso fornito, lo converte in array di byte
     * e lo salva nel database associandolo all'amministratore corrente.
     * 
     * @param Nomefile il nome da assegnare al file nel database
     * @param Difficolta il livello di difficoltà del testo (es. "Facile", "Medio", "Difficile")
     * @param path il percorso del file da caricare nel file system
     * @param lingua la lingua del testo 
     */
    public void caricaTesto(String Nomefile, String Difficolta, String path, Lingua lingua){
    
        byte[] documentoDaCaricare=trasformaDocumento(path);
       
       if(db.CaricareTesto(this.getEmail(), Nomefile, Difficolta, documentoDaCaricare, lingua)){
           System.out.println("Documento caricato");
       }else{
           System.out.println("Documento non caricato");
       }
    }
    
     /**
     * Carica un file di stopwords nel database.
     * Le stopwords sono parole comuni che vengono filtrate
     * durante l'elaborazione del testo affinché non compaiano 
     * nelle domande del gioco.
     * 
     * @param nomefile il nome da assegnare al file di stopwords nel database
     * @param path il percorso del file di stopwords nel file system
     */
    public void CaricareStopwords(String nomefile, String path){
        
        byte[] documentoStopwords=trasformaDocumento(path);
        if(db.CaricareStopwords(this.getEmail(), documentoStopwords, nomefile)){
             System.out.println("Stopwords caricate");
        }else{
             System.out.println("Stopwords non caricate");
        }
    }
    
    /**
     * Metodo privato di utilità che converte un file in un array di byte.
     * Legge tutti i byte del file specificato dal percorso e li restituisce
     * come array di byte. 
     * 
     * @param path il percorso del file da convertire
     * @return array di byte contenente il contenuto del file, null in caso di errore
     */
    private byte[] trasformaDocumento(String path){
        
        byte[] documentoDaCaricare=null;
        try {
            documentoDaCaricare = Files.readAllBytes(Paths.get(path));
        } catch (IOException ex) {
            Logger.getLogger(Amministratore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documentoDaCaricare;
    }
    
    /**
     * Recupera un documento di stopwords dal database.
     * 
     * @param nomeFile il nome del file di stopwords da recuperare
     * @return array di byte contenente il documento di stopwords
     *      
     */
    public byte[] PrendiDocumentoStopwords(String nomeFile){
        byte[] risultato= db.PrendiStopwords(nomeFile);
        return risultato; 
    }
    
    /**
     * Recupera un testo dal database.
     * 
     * @param nomeDocumento il nome del documento da recuperare
     * @return array di byte contenente il documento richiesto
     *         
     */
    public byte[] PrendiTesto(String nomeDocumento){
        byte[] risultato = db.PrendiTesto(nomeDocumento);
        return risultato; 
    }
    
    /**
     * Cancella un testo dal database.
     * Rimuove definitivamente il documento specificato dal database
     * 
     * @param nomeDocumento il nome del documento da cancellare
     */
    public void CancellaTesto(String nomeDocumento){
        
        boolean risultato = db.CancellaTesto(nomeDocumento);
        
        if(risultato){
            System.out.println("Cancellazione testo effettuata");
        }else{
            System.out.println("Cancellazione testo non effettuata");
        }
    }
}
