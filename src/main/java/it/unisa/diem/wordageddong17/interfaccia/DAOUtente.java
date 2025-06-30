package it.unisa.diem.wordageddong17.interfaccia;

import it.unisa.diem.wordageddong17.model.Utente;

/**
 * Interfaccia DAOUtente
 * 
 * Questa interfaccia definisce i metodi necessari per gestire le operazioni sugli utenti nel sistema.
 * Le operazioni includono la modifica del nome utente e della foto profilo, l'inserimento di un nuovo utente,
 * il recupero del nome utente, la verifica della password e il recupero completo delle informazioni di un utente.
 */
public interface DAOUtente {

    /**
     * Modifica il nome utente dell'utente specificato.
     *
     * @param utente l'oggetto Utente contenente l'email e il nuovo nome utente da impostare
     * @return {@code true} se la modifica ha avuto successo, {@code false} altrimenti
     */
    public boolean modificaUsername(Utente utente);

    /**
     * Modifica la foto profilo dell'utente specificato.
     *
     * @param utente l'oggetto Utente contenente l'email e la nuova foto profilo
     * @return {@code true} se la modifica ha avuto successo, {@code false} altrimenti
     */
    public boolean modificaFotoProfilo(Utente utente);

    /**
     * Inserisce un nuovo utente nel sistema.
     * 
     * Questo metodo registra un utente utilizzando l'oggetto Utente e la password fornita.
     * La password dovrebbe essere opportunamente criptata prima del salvataggio.
     *
     * @param utente l'oggetto Utente contenente le informazioni dell'utente da inserire
     * @param password la password in chiaro dell'utente
     */
    public void inserisciUtente(Utente utente, String password);

    /**
     * Recupera il nome utente associato all'indirizzo email specificato.
     *
     * @param email l'email dell'utente
     * @return il nome utente se presente; altrimenti, {@code null}
     */
    public String prendiUsername(String email);

    /**
     * Verifica se la password fornita corrisponde a quella memorizzata per l'utente con l'email specificata.
     *
     * @param email    l'email dell'utente
     * @param password la password da verificare
     * @return {@code true} se la password è corretta, {@code false} altrimenti
     */
    public boolean verificaPassword(String email, String password);

    /**
     * Recupera le informazioni dell'utente associato all'indirizzo email specificato.
     *
     * @param email l'email dell'utente da recuperare
     * @return un oggetto {@link Utente} contenente le informazioni dell'utente, oppure {@code null} se l'utente non esiste
     */
    public Utente prendiUtente(String email);
}
