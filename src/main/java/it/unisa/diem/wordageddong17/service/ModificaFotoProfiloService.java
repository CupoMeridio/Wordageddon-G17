package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per la modifica della foto profilo dell'utente.
 * <p>
 * Questa classe utilizza {@link DAOUtente} per aggiornare l'immagine del profilo
 * associata ad una email specifica. Il servizio viene eseguito in modo asincrono
 * tramite un {@link Task} che effettua l'operazione lato database.
 * </p>
 * 
 * @author Mattia
 */
public class ModificaFotoProfiloService extends Service<Void> {

    private byte[] immagine;
    private String email;
    private final DAOUtente dbu;

    /**
     * Costruttore con parametri per inizializzare il servizio di modifica foto profilo.
     *
     * @param immagine L'immagine del profilo in formato byte array.
     * @param email    L'indirizzo email dell'utente per cui modificare la foto profilo.
     */
    public ModificaFotoProfiloService(byte[] immagine, String email) {
        this.immagine = immagine;
        this.email = email;
        this.dbu = DatabaseUtente.getInstance();
    }

    /**
     * Costruttore di default che inizializza l'accesso al database degli utenti.
     */
    public ModificaFotoProfiloService() {
        this.dbu = DatabaseUtente.getInstance();
    }

    /**
     * Imposta l'immagine del profilo.
     *
     * @param immagine L'immagine in formato byte array da impostare.
     */
    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    /**
     * Imposta l'indirizzo email dell'utente.
     *
     * @param email L'email da impostare.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Crea un task asincrono per l'aggiornamento della foto profilo dell'utente.
     * <p>
     * Il task utilizza il database degli utenti per eseguire la modifica della foto profilo.
     * </p>
     *
     * @return Un {@link Task} che esegue l'aggiornamento della foto profilo.
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                dbu.modificaFotoProfilo(email, immagine);
                return null;
            }
        };
    }
}
