package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import it.unisa.diem.wordageddong17.model.TipoUtente;
import it.unisa.diem.wordageddong17.model.Utente;
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
    private Utente utente;
    private String password;

    /**
     * Costruttore con parametri per inizializzare il servizio di inserimento utente.
     * 
     * @param username Il nome utente.
     * @param email L'email dell'utente.
     * @param password La password dell'utente.
     * @param immagineBytes L'immagine del profilo dell'utente in formato byte array.
     */
    public InserisciUtenteService(String username, String email, String password, byte[] immagineBytes) {
        this.utente = new Utente(username, email, immagineBytes, TipoUtente.giocatore);
        this.password = password;
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
        if (this.utente == null) {
            this.utente = new Utente(username, "", null, TipoUtente.giocatore);
        } else {
            this.utente = new Utente(username, this.utente.getEmail(), this.utente.getFotoProfilo(), this.utente.getTipo());
        }
    }

    /**
     * Imposta l'email dell'utente.
     * 
     * @param email L'email da impostare.
     */
    public void setEmail(String email) {
        if (this.utente == null) {
            this.utente = new Utente("", email, null, TipoUtente.giocatore);
        } else {
            this.utente = new Utente(this.utente.getUsername(), email, this.utente.getFotoProfilo(), this.utente.getTipo());
        }
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
        if (this.utente == null) {
            this.utente = new Utente("", "", immagineBytes, TipoUtente.giocatore);
        } else {
            this.utente = new Utente(this.utente.getUsername(), this.utente.getEmail(), immagineBytes, this.utente.getTipo());
        }
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
                dbu.inserisciUtente(utente, password);
                return null;
            }
        };
    }
}
