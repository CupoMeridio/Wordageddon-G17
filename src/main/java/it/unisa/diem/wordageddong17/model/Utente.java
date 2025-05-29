/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.model;

/**
 *
 * @author Mattia Sanzari
 */
public class Utente {
    
    private final String username;
    private final String email;
    private final float punteggioMigliore;
    private final byte[] fotoProfilo;
    private final String tipo; // -> futura enumerazione

    public Utente(String username, String email, float punteggioMigliore, byte[] fotoProfilo, String tipo) {
        this.username = username;
        this.email = email;
        this.punteggioMigliore = punteggioMigliore;
        this.fotoProfilo = fotoProfilo;
        this.tipo = tipo;
    }

    public Utente(String username, String email, int punteggio, String giocatore) {
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

    @Override
    public String toString() {
        return "Utente{" + "username=" + username + ", email=" + email + ", punteggioMigliore=" + punteggioMigliore + ", fotoProfilo=" + fotoProfilo + ", tipo=" + tipo + '}';
    }
    
    
    
}
