/**
Servizio asincrono per la gestione del caricamento dei punteggi nel database.

Questa classe utilizza JavaFX {@link Service} per eseguire operazioni di scrittura

sui punteggi in background.
*/
package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseClassifica;
import it.unisa.diem.wordageddong17.interfaccia.DAOClassifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Classe per la gestione del caricamento dei punteggi nel database in maniera asincrona.
 */
public class CaricaPunteggioService extends Service<Void>{
    private final DAOClassifica dbC;
    private String email;
    private float punteggio;
    private LivelloPartita difficolta;
/**
 * Costruttore senza parametri. Inizializza l'istanza del database.
 */
    public CaricaPunteggioService() {
        this(DatabaseClassifica.getInstance());
    }
/**
* Costruttore con parametri per inizializzare le informazioni di un punteggio.
*
* @param email L'email dell'utente.
*
* @param punteggio Il punteggio ottenuto.
*
* @param difficolta Il livello di difficoltà della partita.
*/
    public CaricaPunteggioService(String email, float punteggio, LivelloPartita difficolta) {
        this(DatabaseClassifica.getInstance(), email, punteggio, difficolta);
    }

/**
* Costruttore con iniezione del DAO, utilizzabile nei test.
*
* @param dbC Il DAO delle classifiche da utilizzare.
*/
    CaricaPunteggioService(DAOClassifica dbC) {
        this.dbC = dbC;
    }

/**
* Costruttore con iniezione del DAO, utilizzabile nei test.
*
* @param dbC Il DAO delle classifiche da utilizzare.
*
* @param email L'email dell'utente.
*
* @param punteggio Il punteggio ottenuto.
*
* @param difficolta Il livello di difficoltà della partita.
*/
    CaricaPunteggioService(DAOClassifica dbC, String email, float punteggio, LivelloPartita difficolta) {
        this.dbC = dbC;
        this.email = email;
        this.punteggio = punteggio;
        this.difficolta = difficolta;
    }

/**
 * Imposta l'email dell'utente.
 * @param email L'email dell'utente.
 */
    public void setEmail(String email) {
        this.email = email;
    }
/**
 * Imposta il punteggio ottenuto.
 * @param punteggio Il punteggio ottenuto.
 */
    public void setPunteggio(float punteggio) {
        this.punteggio = punteggio;
    }

/**
 * Imposta il livello di difficoltà della partita.
 *
 * @param difficolta Il livello di difficoltà della partita.
 */
    public void setDifficolta(LivelloPartita difficolta) {
        this.difficolta = difficolta;
    }
    
/**
 * Crea un task per inserire il punteggio nel database.
 *
 * @return Un task che esegue l'inserimento dei dati.
 */    
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                dbC.inserisciPunteggio(email, punteggio,difficolta);
                return null;
            }    
        };
    }
    
}
