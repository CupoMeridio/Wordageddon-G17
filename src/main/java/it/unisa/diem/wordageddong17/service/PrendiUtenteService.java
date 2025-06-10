package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Questo servizio esegue il recupero in background delle informazioni di un utente
 * identificato dal suo indirizzo email.
 * <p>
 * La classe estende {@link javafx.concurrent.Service} parametrizzata con {@link it.unisa.diem.wordageddong17.model.Utente}.
 * Attraverso l'utilizzo di un {@link Task}, il servizio invoca il metodo
 * {@link it.unisa.diem.wordageddong17.interfaccia.DAOUtente#prendiUtente(String)} della
 * classe {@link it.unisa.diem.wordageddong17.database.DatabaseUtente} per ottenere i dettagli dell'utente.
 * </p>
 *
 * @see javafx.concurrent.Service
 * @see javafx.concurrent.Task
 * @see it.unisa.diem.wordageddong17.database.DatabaseUtente
 * @see it.unisa.diem.wordageddong17.interfaccia.DAOUtente
 * @see it.unisa.diem.wordageddong17.model.Utente
 */
public class PrendiUtenteService extends Service<Utente> {

    /**
     * Interfaccia per il recupero dei dati di un utente.
     */
    private final DAOUtente dbu;

    /**
     * Indirizzo email dell'utente da recuperare.
     */
    private String email;

    /**
     * Costruisce una nuova istanza del servizio impostando l'email dell'utente.
     *
     * @param email l'indirizzo email dell'utente da recuperare
     */
    public PrendiUtenteService(String email) {
        this.email = email;
        dbu = DatabaseUtente.getInstance();
    }

    /**
     * Costruisce una nuova istanza del servizio.
     * <p>
     * In questo caso, l'email deve essere impostata successivamente tramite il metodo
     * {@link #setEmail(String)} prima di avviare il servizio.
     * </p>
     */
    public PrendiUtenteService() {
        dbu = DatabaseUtente.getInstance();
    }

    /**
     * Imposta l'indirizzo email dell'utente da recuperare.
     *
     * @param email l'indirizzo email dell'utente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Crea ed esegue un task in background per il recupero dei dati dell'utente.
     * <p>
     * Il task invoca il metodo {@link it.unisa.diem.wordageddong17.interfaccia.DAOUtente#prendiUtente(String)}
     * passando l'indirizzo email e restituisce un'istanza di {@link it.unisa.diem.wordageddong17.model.Utente}.
     * </p>
     *
     * @return un {@link Task} che, una volta completato, fornirà i dati dell'utente
     */
    @Override
    protected Task<Utente> createTask() {
        return new Task<Utente>() {
            @Override
            protected Utente call() throws Exception {
                return dbu.prendiUtente(email);
            }
        };
    }
}
