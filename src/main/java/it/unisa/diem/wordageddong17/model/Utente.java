package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.database.DosUtente;

/**
 *
 * @author Mattia Sanzari
 */
public class Utente {
    
    DosUtente db;
    private String username;
    private final String email;
    private final float punteggioMigliore;
    private byte[] fotoProfilo;
    private final String tipo; // -> futura enumerazione

    public Utente(String username, String email, float punteggioMigliore, byte[] fotoProfilo, String tipo) {
        this.username = username;
        this.email = email;
        this.punteggioMigliore = punteggioMigliore;
        this.fotoProfilo = fotoProfilo;
        this.tipo = tipo;
        db= DatabaseUtente.getInstance();
    }

    public Utente(String username, String email, float punteggio, String giocatore) {
        this.username = username;
        this.email = email;
        this.punteggioMigliore = punteggio;
        this.tipo = giocatore;
        fotoProfilo = null;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public float getPunteggioMigliore() {
        return punteggioMigliore;
    }

    public byte[] getFotoProfilo() {
        return fotoProfilo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setUsername(String username) {
        if(db.modificaUsername(email, username)){
            this.username = username;
        }else{
            System.out.println("User name non cambiato");
        }
    }

    public void setFotoProfilo(byte[] fotoProfilo) {
        if(db.modificaFotoProfilo(email, fotoProfilo)){
            this.fotoProfilo = fotoProfilo;
        }else{
            System.out.println("Foto name non cambiato");
        }
    }
    
    
    
    @Override
    public String toString() {
        return "Utente{" + "username=" + username + ", email=" + email + ", punteggioMigliore=" + punteggioMigliore + ", fotoProfilo=" + fotoProfilo + ", tipo=" + tipo + '}';
    }
    
    
    
}
