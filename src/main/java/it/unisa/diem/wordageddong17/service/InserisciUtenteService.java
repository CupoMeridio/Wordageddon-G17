package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per l'inserimento di un nuovo utente nel database.
 * Questa classe utilizza {@link DAOUtente} per memorizzare le informazioni 
 * dell'utente, inclusi username, email, password e immagine del profilo.
 *
 * @author Mattia Sanzari
 */
public class InserisciUtenteService extends Service<Void> {
    
    private final DAOUtente dbu;
    private String username;
    private String email;
    private String password;
    private byte[] immagineBytes;

    /**
     * Costruttore con parametri per inizializzare il servizio di inserimento utente.
     * 
     * @param username Il nome utente.
     * @param email L'email dell'utente.
     * @param password La password dell'utente.
     * @param immagineBytes L'immagine del profilo dell'utente in formato byte array.
     */
    public InserisciUtenteService(String username, String email, String password, byte[] immagineBytes) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.immagineBytes = immagineBytes;
        this.dbu = DatabaseUtente.getInstance();
    }

    /**
     * Costruttore di default che inizializza l'accesso al database degli utenti.
     */
    public InserisciUtenteService() {
        this.dbu = DatabaseUtente.getInstance();
    }

    /**
     * Imposta il nome utente.
     * 
     * @param username Il nome utente da impostare.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Imposta l'email dell'utente.
     * 
     * @param email L'email da impostare.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Imposta la password dell'utente.
     * 
     * @param password La password da impostare.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Imposta l'immagine del profilo dell'utente.
     * 
     * @param immagineBytes Il contenuto dell'immagine in formato byte array.
     */
    public void setImmagineBytes(byte[] immagineBytes) {
        this.immagineBytes = immagineBytes;
    }

    /**
     * Crea un task asincrono per l'inserimento dell'utente nel database.
     * 
     * @return Un {@link Task} che esegue l'inserimento dell'utente.
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                dbu.inserisciUtente(username, email, password, immagineBytes);
                return null;
            }
        };
    }
}
