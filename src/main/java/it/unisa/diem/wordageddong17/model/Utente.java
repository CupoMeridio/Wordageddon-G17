package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import java.io.Serializable;

/**
 *
 * @author Mattia Sanzari
 */
public class Utente implements Serializable {
    
    private String username;
    private final String email;
    private byte[] fotoProfilo;
    private TipoUtente tipo; // -> presente enumerazione

    public Utente(String username, String email, byte[] fotoProfilo, TipoUtente tipo) {
        this.username = username;
        this.email = email;
        this.fotoProfilo = fotoProfilo;
        this.tipo = tipo;
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
            this.username = username;     
    }

    public void setFotoProfilo(byte[] fotoProfilo) {   
            this.fotoProfilo = fotoProfilo;
    }
    
    
    
    @Override
    public String toString() {
        return "Utente{" + "username=" + username + ", email=" + email + ", fotoProfilo=" + fotoProfilo + ", tipo=" + tipo + '}';
    }
    
    
    
}
