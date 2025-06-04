package it.unisa.diem.wordageddong17.model;

/**
 *
 * @author cupom
 */
public class AppState {
    
    private Utente utente;

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    
    
    private static class Holder {
        private static final AppState INSTANCE = new AppState();
    }
    
    public static AppState getInstance() {
        return AppState.Holder.INSTANCE;
    }
}
