package it.unisa.diem.wordageddong17.model;

import java.util.ArrayList;

/**
 * Questa classe rappresenta lo stato globale dell'applicazione e mantiene
 * le informazioni rilevanti relative all'utente autenticato, alle impostazioni della sessione,
 * al livello di difficoltà della partita e alle lingue disponibili.
 * 
 * La classe adotta il pattern Singleton, garantendo così che in tutta l'applicazione
 * esista una sola istanza di AppState. L'istanza viene creata in maniera lazy e in modo thread-safe 
 * grazie all'uso di una classe interna statica (Holder Pattern).
 * 
 */
public class AppState {
    
    /**
     * Riferimento all'utente correntemente autenticato nell'applicazione.
     */
    private Utente utente;
    
    /**
     * Livello di difficoltà selezionato per la partita corrente.
     */
    private LivelloPartita difficolta = null;
    
    /**
     * Lista delle lingue disponibili o selezionate.
     */
    private ArrayList<Lingua> lingue = null;
    
    /**
     * Flag che indica se la sessione corrente è stata salvata.
     */
    private boolean sessioneSalvata = false;

    /**
     * Resetta completamente lo stato globale dell'applicazione (usato al logout).
     */
    public void reset() {
        this.utente = null;
        this.difficolta = null;
        this.lingue = null;
        this.sessioneSalvata = false;
    }

    /**
     * Imposta lo stato di salvataggio della sessione corrente.
     *
     * @param sessioneSalvata {@code true} se la sessione è stata salvata, {@code false} altrimenti
     */
    public void setSessioneSalvata(boolean sessioneSalvata) {
        this.sessioneSalvata = sessioneSalvata;
    }

    /**
     * Restituisce lo stato di salvataggio della sessione corrente.
     *
     * @return {@code true} se la sessione è stata salvata, {@code false} altrimenti
     */
    public boolean isSessioneSalvata() {
        return sessioneSalvata;
    }

    /**
     * Restituisce il livello di difficoltà selezionato per la partita corrente.
     *
     * @return il {@link LivelloPartita} selezionato oppure {@code null} se non è stato impostato
     */
    public LivelloPartita getDifficolta() {
        return difficolta;
    }

    /**
     * Imposta il livello di difficoltà per la partita corrente.
     *
     * @param difficolta il {@link LivelloPartita} da impostare
     */
    public void setDifficolta(LivelloPartita difficolta) {
        this.difficolta = difficolta;
    }

    /**
     * Restituisce la lista delle lingue disponibili o selezionate.
     *
     * @return un {@link ArrayList} di oggetti {@link Lingua} oppure {@code null} se non è stata impostata
     */
    public ArrayList<Lingua> getLingue() {
        return lingue;
    }

    /**
     * Imposta la lista delle lingue disponibili o selezionate.
     *
     * @param lingue un {@link ArrayList} di oggetti {@link Lingua}
     */
    public void setLingue(ArrayList<Lingua> lingue) {
        this.lingue = lingue;
    }
    
    /**
     * Restituisce l'utente correntemente autenticato nell'applicazione.
     *
     * @return l'oggetto {@link Utente} dell'utente corrente oppure {@code null} se non è stato impostato
     */
    public Utente getUtente() {
        return utente;
    }
    
    /**
     * Imposta l'utente correntemente autenticato nell'applicazione.
     *
     * @param utente l'oggetto {@link Utente} da impostare come utente corrente
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    
    /**
     * Classe interna statica per l'implementazione thread-safe del pattern Singleton.
     * <p>
     * L'istanza di {@code AppState} viene creata solo quando la classe {@code Holder} viene caricata,
     * garantendo così un'inizializzazione lazy e sicura anche in contesti multi-thread.
     * </p>
     */
    private static class Holder {
        /**
         * L'unica istanza di {@link AppState}.
         */
        private static final AppState INSTANCE = new AppState();
    }
    
    /**
     * Restituisce l'unica istanza di {@link AppState} (pattern Singleton).
     *
     * @return l'unica istanza di {@link AppState}
     */
    public static AppState getInstance() {
        return AppState.Holder.INSTANCE;
    }
    
    /**
     * Costruttore di default.
     * <p>
     * Questo costruttore è privato per impedire la creazione di istanze multiple,
     * garantendo il pattern Singleton.
     * </p>
     */
    private AppState() {
        // eventuale inizializzazione se serve
    }
}
