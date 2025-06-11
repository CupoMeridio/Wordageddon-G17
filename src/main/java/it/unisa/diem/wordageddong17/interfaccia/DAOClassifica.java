package it.unisa.diem.wordageddong17.interfaccia;

import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.util.List;

/**
 * Interfaccia DAOClassifica.
 * Questa interfaccia definisce i metodi necessari per le operazioni di accesso
 * ai dati relativi alle classifiche e alla cronologia delle partite degli utenti.
 * 
 * I metodi dichiarati includono:
 * - Recuperare la classifica per un determinato livello di difficoltà.
 * - Recuperare la cronologia delle partite di un utente specifico.
 * - Recuperare il numero di partite giocate da un utente per un certo livello di difficoltà.
 * - Recuperare il miglior punteggio ottenuto da un utente per un dato livello di difficoltà.
 * - Inserire un nuovo punteggio per una partita dell'utente.
 *
 * @see it.unisa.diem.wordageddong17.model.Classifica
 * @see it.unisa.diem.wordageddong17.model.LivelloPartita
 */
public interface DAOClassifica {

    /**
     * Recupera la classifica per il livello di difficoltà specificato.
     *
     * @param difficolta il livello di difficoltà per il quale si desidera ottenere la classifica
     * @return una lista di oggetti {@link Classifica} che rappresentano la classifica per il livello indicato
     */
    public List<Classifica> prendiClassifica(LivelloPartita difficolta);

    /**
     * Recupera la cronologia delle partite per un utente.
     *
     * @param email l'email dell'utente
     * @return una lista di oggetti {@link Classifica} che rappresentano la cronologia delle partite dell'utente
     */
    public List<Classifica> recuperaCronologiaPartite(String email);

    /**
     * Recupera il numero di partite giocate da un utente per un determinato livello di difficoltà.
     *
     * @param email l'email dell'utente
     * @param difficoltà una stringa che rappresenta il livello di difficoltà (es. "facile", "medio", "difficile")
     * @return il numero di partite giocate dall'utente a quel livello di difficoltà
     */
    public int recuperaNumeroPartite(String email, String difficoltà);

    /**
     * Recupera il miglior punteggio ottenuto da un utente per un determinato livello di difficoltà.
     *
     * @param email l'email dell'utente
     * @param difficoltà una stringa che rappresenta il livello di difficoltà
     * @return il miglior punteggio ottenuto dall'utente per il livello specificato
     */
    public float recuperaMigliorPunteggio(String email, String difficoltà);

    /**
     * Inserisce un nuovo punteggio per una partita dell'utente.
     *
     * @param email l'email dell'utente
     * @param punteggio il punteggio ottenuto nella partita
     * @param difficolta il livello di difficoltà della partita
     */
    public void inserisciPunteggio(String email, float punteggio, LivelloPartita difficolta);
}
