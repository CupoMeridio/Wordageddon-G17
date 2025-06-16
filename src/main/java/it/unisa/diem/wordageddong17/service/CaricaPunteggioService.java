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
    private LivelloPartita difficoltà;
/**
 * Costruttore senza parametri. Inizializza l'istanza del database.
 */
    public CaricaPunteggioService() {
        this.dbC = DatabaseClassifica.getInstance();
    }
/**
* Costruttore con parametri per inizializzare le informazioni di un punteggio.
*
* @param email L'email dell'utente.
*
* @param punteggio Il punteggio ottenuto.
*
* @param difficoltà Il livello di difficoltà della partita.
*/
    public CaricaPunteggioService(String email, float punteggio, LivelloPartita difficoltà) {
        this.dbC = DatabaseClassifica.getInstance();
        this.email = email;
        this.punteggio = punteggio;
        this.difficoltà = difficoltà;
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
 * @param difficoltà Il livello di difficoltà della partita.
 */
    public void setDifficoltà(LivelloPartita difficoltà) {
        this.difficoltà = difficoltà;
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
                System.out.println(email+" "+punteggio+" "+difficoltà);
                dbC.inserisciPunteggio(email, punteggio,difficoltà);
                return null;
            }    
        };
    }
    
}
