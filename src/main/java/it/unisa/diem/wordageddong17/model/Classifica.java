package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.database.DatabaseClassifica;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Classe che rappresenta un'entry della classifica del gioco WordAgeddon.
 * 
 * Questa classe incapsula le informazioni relative a una singola performance di 
 * un giocatore in una partita.
 * Ogni istanza rappresenta un record della classifica contenente il punteggio ottenuto,
 * la data della partita, il livello di difficoltà e l'identificativo del giocatore.
 * 
 * La classe viene utilizzata per:
 * - Memorizzare i risultati delle partite
 * - Visualizzare le classifiche dei giocatori
 * - Confrontare le performance tra diversi giocatori
 * - Mantenere lo storico dei punteggi per livello di difficoltà
 * 
 * @author cupom
 */
public class Classifica {
   
    private final String username;
    private final Timestamp data;
    private final float punti;
    private final LivelloPartita difficolta;
    private final DatabaseClassifica sbc = DatabaseClassifica.getInstance();

    /**
     * Costruttore per creare una nuova entry della classifica.
     * 
     * Inizializza tutti i campi della classifica con i valori forniti.
     * 
     * @param email_utente l'identificativo dell'utente (email o username) che ha ottenuto il punteggio
     * @param data il timestamp che indica quando è stata completata la partita
     * @param punti il punteggio numerico ottenuto dal giocatore 
     * @param difficolta il livello di difficoltà della partita giocata
     */
    public Classifica(String email_utente, Timestamp data, float punti, LivelloPartita difficolta) {
        this.username = email_utente;
        this.data = data;
        this.punti = punti;
        this.difficolta = difficolta;
    }
    
    /**
     * Restituisce il nome utente del giocatore.
     * 
     * @return lo username del giocatore associato a questo record
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la data e ora in cui è stata giocata la partita.
     * 
     * @return il timestamp che indica quando è stato ottenuto questo punteggio
     */
    public Timestamp getData() {
        return data;
    }
    /**
     * Restituisce il punteggio ottenuto dal giocatore.
     * 
     * @return il punteggio numerico della partita
     */
    public float getPunti() {
        return punti;
    }
    
    /**
     * Restituisce il livello di difficoltà della partita.
     * 
     * @return  il livello di difficoltà
     */

    public LivelloPartita getDifficolta() {
        return difficolta;
    }

    /**
     * Restituisce una rappresentazione testuale dell'oggetto Classifica.
     * 
     * @return una stringa che rappresenta questo record della classifica
     */
    @Override
    public String toString() {
        return "Classifica{" + "username=" + username + ", data=" + data + ", punti=" + punti + ", difficolta=" + difficolta + '}';
    }

    
    
    
}
