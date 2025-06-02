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
    private byte[] fotoProfilo;
    private TipoUtente tipo; // -> presente enumerazione

    public Utente(String username, String email, byte[] fotoProfilo, TipoUtente tipo) {
        this.username = username;
        this.email = email;
        this.fotoProfilo = fotoProfilo;
        this.tipo = tipo;
        db= DatabaseUtente.getInstance();
    }

    public Utente(String username, String email, TipoUtente tipo) {
        this.username = username;
        this.email = email;
        this.tipo = tipo;
        fotoProfilo = null;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }


    public byte[] getFotoProfilo() {
        return fotoProfilo;
    }

    public TipoUtente getTipo() {
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
        return "Utente{" + "username=" + username + ", email=" + email + ", fotoProfilo=" + fotoProfilo + ", tipo=" + tipo + '}';
    }
    
    
    
}
