package it.unisa.diem.wordageddong17.model;

import java.util.ArrayList;

/**
 *
 * @author cupom
 */
public class AppState {
    
    /**
     * Riferimento all'utente correntemente autenticato nell'applicazione.
     */
    private Utente utente;
    private LivelloPartita difficoltà = null;
    private boolean sessionViewHomeButton = false;
    private boolean sessionViewContinuaButton = false;
    private ArrayList<Lingua> lingue = null;

    public void setSessionViewHomeButton(boolean sessionViewHomeButton) {
        this.sessionViewHomeButton = sessionViewHomeButton;
    }

    public void setSessionViewContinuaButton(boolean sessionViewContinuaButton) {
        this.sessionViewContinuaButton = sessionViewContinuaButton;
    }

    public boolean isSessionViewHomeButton() {
        return sessionViewHomeButton;
    }

    public boolean isSessionViewContinuaButton() {
        return sessionViewContinuaButton;
    }

    public LivelloPartita getDifficoltà() {
        return difficoltà;
    }

    public void setDifficoltà(LivelloPartita difficoltà) {
        this.difficoltà = difficoltà;
    }

    public ArrayList<Lingua> getLingue() {
        return lingue;
    }

    public void setLingue(ArrayList<Lingua> lingue) {
        this.lingue = lingue;
    }
    
    
    
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
