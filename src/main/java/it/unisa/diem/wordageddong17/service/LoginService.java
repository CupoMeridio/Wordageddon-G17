package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per la gestione del login di un utente.
 * Questa classe verifica la password dell'utente e, se corretta, 
 * recupera i suoi dati dal database.
 *
 * @author Mattia Sanzari
 */
public class LoginService extends Service<Utente> {

    private String email;
    private String password;
    private final DAOUtente dbu;

    /**
     * Costruttore con parametri per inizializzare il servizio di login.
     * 
     * @param email L'email dell'utente.
     * @param password La password dell'utente.
     */
    public LoginService(String email, String password) {
        this.email = email;
        this.password = password;
        this.dbu = DatabaseUtente.getInstance();
    }

    /**
     * Costruttore di default che inizializza l'accesso al database degli utenti.
     */
    public LoginService() {
        this.dbu = DatabaseUtente.getInstance();
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
     * Crea un task asincrono per la verifica del login.
     * 
     * @return Un {@link Task} che verifica la password e recupera l'utente dal database.
     */
    @Override
    protected Task<Utente> createTask() {
        return new Task<>() {
            @Override
            protected Utente call() throws Exception {
                // Verifica la password dell'utente
                if (!dbu.verificaPassword(email, password)) {
                    return null;
                }
                // Recupera i dati dell'utente dal database
                return dbu.prendiUtente(email);
            }
        };
    }
}

