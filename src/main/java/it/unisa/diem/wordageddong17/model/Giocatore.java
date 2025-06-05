package it.unisa.diem.wordageddong17.model;

public class Giocatore extends Utente{
    
    public Giocatore(String username, String email, byte[] fotoProfilo, TipoUtente tipo) {
        super(username, email, fotoProfilo, tipo);
    }
    
    public Giocatore(String username, String email, TipoUtente tipo) {
        super(username, email, tipo);
    }

}
