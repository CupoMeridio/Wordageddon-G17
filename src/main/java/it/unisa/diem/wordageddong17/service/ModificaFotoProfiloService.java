package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import it.unisa.diem.wordageddong17.model.TipoUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per la modifica della foto profilo dell'utente.
 * <p>
 * Questa classe utilizza {@link DAOUtente} per aggiornare l'immagine del profilo
 * associata ad una email specifica. Il servizio viene eseguito in modo asincrono
 * tramite un {@link Task} che effettua l'operazione lato database.
 * 
 */
public class ModificaFotoProfiloService extends Service<Void> {

    private Utente utente;
    private final DAOUtente dbu;

    /**
     * Costruttore con parametri per inizializzare il servizio di modifica foto profilo.
     *
     * @param immagine L'immagine del profilo in formato byte array.
     * @param email    L'indirizzo email dell'utente per cui modificare la foto profilo.
     */
    public ModificaFotoProfiloService(byte[] immagine, String email) {
        this.utente = new Utente("", email, immagine, TipoUtente.giocatore);
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
        if (this.utente == null) {
            this.utente = new Utente("", "", immagine, TipoUtente.giocatore);
        } else {
            this.utente.setFotoProfilo(immagine);
        }
    }

    /**
     * Imposta l'indirizzo email dell'utente.
     *
     * @param email L'email da impostare.
     */
    public void setEmail(String email) {
        if (this.utente == null) {
            this.utente = new Utente("", email, null, TipoUtente.giocatore);
        } else {
            // Poiché l'email è final nel modello Utente, dobbiamo creare un nuovo oggetto
            this.utente = new Utente(this.utente.getUsername(), email, this.utente.getFotoProfilo(), this.utente.getTipo());
        }
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
                dbu.modificaFotoProfilo(utente);
                return null;
            }
        };
    }
}
