package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseClassifica;
import it.unisa.diem.wordageddong17.interfaccia.DAOClassifica;
import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.InfoGiocatore;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Questo servizio esegue il recupero in background delle informazioni relative a un utente,
 * ottenendo la cronologia delle partite, il numero di partite giocate e il punteggio migliore 
 * raggiunto per ciascun livello di difficoltà (Facile, Medio e Difficile).
 * <p>
 * Il recupero dei dati avviene tramite l'interfaccia {@link it.unisa.diem.wordageddong17.interfaccia.DAOClassifica},
 * utilizzando l'istanza unica fornita da 
 * {@link it.unisa.diem.wordageddong17.database.DatabaseClassifica}. Il risultato dell'elaborazione
 * è restituito come oggetto {@link it.unisa.diem.wordageddong17.model.InfoGiocatore} che aggrega tutte le 
 * informazioni rilevanti per l'utente.
 * </p>
 *
 * @see javafx.concurrent.Service
 * @see it.unisa.diem.wordageddong17.database.DatabaseClassifica
 * @see it.unisa.diem.wordageddong17.model.InfoGiocatore
 */
public class PrendiInfoUtenteService extends Service<InfoGiocatore> {
    
    /** Lista contenente la cronologia delle partite giocate dall'utente. */
    List<Classifica> cronologiaPartiteList;
    
    /** Numero di partite giocate al livello Facile. */
    private int facileCount;
    
    /** Numero di partite giocate al livello Medio. */
    private int medioCount;
    
    /** Numero di partite giocate al livello Difficile. */
    private int difficileCount;
    
    /** Punteggio migliore ottenuto al livello Facile. */
    private float facilePunteggio;
    
    /** Punteggio migliore ottenuto al livello Medio. */
    private float medioPunteggio;
    
    /** Punteggio migliore ottenuto al livello Difficile. */
    private float difficilePunteggio;
    
    /** Riferimento all'interfaccia per l'accesso ai dati delle classifiche. */
    private final DAOClassifica dbC;
    
    /** Indirizzo email dell'utente per il quale vengono recuperate le informazioni. */
    private String email;
    
    /**
     * Costruisce una nuova istanza del servizio impostando l'email dell'utente.
     *
     * @param email l'indirizzo email dell'utente di cui recuperare le informazioni
     */
    public PrendiInfoUtenteService(String email) {
        this.dbC = DatabaseClassifica.getInstance();
        this.email = email;
    }
    
    /**
     * Costruisce una nuova istanza del servizio senza impostare l'email.
     * È necessario impostare successivamente l'email tramite il metodo {@link #setEmail(String)} 
     * prima di avviare il servizio.
     */
    public PrendiInfoUtenteService() {
        this.dbC = DatabaseClassifica.getInstance();
    }

    /**
     * Imposta l'indirizzo email dell'utente per il recupero delle informazioni.
     *
     * @param email l'indirizzo email dell'utente
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Crea un task che viene eseguito in background per il recupero delle informazioni dell'utente.
     * All'interno del task vengono effettuate le seguenti operazioni:
     * <ul>
     *   <li>Recupero della cronologia delle partite tramite {@code dbC.recuperaCronologiaPartite(email)}</li>
     *   <li>Recupero del numero di partite giocate per ogni livello
     *       (Facile, Medio, Difficile) tramite {@code dbC.recuperaNumeroPartite(email, livello)}</li>
     *   <li>Recupero del miglior punteggio per ogni livello tramite
     *       {@code dbC.recuperaMigliorPunteggio(email, livello)}</li>
     * </ul>
     * Il task restituisce un oggetto {@link it.unisa.diem.wordageddong17.model.InfoGiocatore} contenente
     * i dati aggregati.
     *
     * @return un {@link Task} che, una volta completato, restituisce le informazioni del giocatore
     */
    @Override
    protected Task<InfoGiocatore> createTask() {
        return new Task<InfoGiocatore>() {
            @Override
            protected InfoGiocatore call() throws Exception {
                cronologiaPartiteList = dbC.recuperaCronologiaPartite(email);
                facileCount = dbC.recuperaNumeroPartite(email, LivelloPartita.FACILE.getDbValue());
                medioCount = dbC.recuperaNumeroPartite(email, LivelloPartita.MEDIO.getDbValue());
                difficileCount = dbC.recuperaNumeroPartite(email, LivelloPartita.DIFFICILE.getDbValue());
                facilePunteggio = dbC.recuperaMigliorPunteggio(email, LivelloPartita.FACILE.getDbValue());
                medioPunteggio = dbC.recuperaMigliorPunteggio(email, LivelloPartita.MEDIO.getDbValue());
                difficilePunteggio = dbC.recuperaMigliorPunteggio(email, LivelloPartita.DIFFICILE.getDbValue());
                
                return new InfoGiocatore(cronologiaPartiteList, facileCount, medioCount, difficileCount,
                                          facilePunteggio, medioPunteggio, difficilePunteggio);
            }
        };
    }
}
