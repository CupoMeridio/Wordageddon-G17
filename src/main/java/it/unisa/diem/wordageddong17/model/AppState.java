package it.unisa.diem.wordageddong17.model;

/**
 *
 * @author cupom
 */
public class AppState {
    
    /**
     * Riferimento all'utente correntemente autenticato nell'applicazione.
     */
    private Utente utente;
    
    /**
     * Restituisce l'utente correntemente autenticato nell'applicazione.
     * 
     * @return l'oggetto Utente correntemente autenticato
     */
    public Utente getUtente() {
        return utente;
    }
    
    /**
     * Imposta l'utente correntemente autenticato nell'applicazione
     * 
     * @param utente l'oggetto Utente da impostare come utente corrente,
     *               
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    
    /**
     * Classe interna statica per l'implementazione thread-safe del Singleton.
     * 
     * L'istanza viene creata solo quando la classe Holder viene caricata.
     */
    private static class Holder {
        private static final AppState INSTANCE = new AppState();
    }
    
     /**
     * Restituisce l'unica istanza di AppState (pattern Singleton).
     * @return l'unica istanza di AppState
     */
    public static AppState getInstance() {
        return AppState.Holder.INSTANCE;
    }
    

}
