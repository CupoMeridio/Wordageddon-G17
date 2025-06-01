package it.unisa.diem.wordageddong17.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author cupom
 */
public class Classifica {
    private final String email_utente;
    private final Timestamp data;
    private final float punti;
    private final LivelloPartita difficolta;

    public Classifica(String email_utente, Timestamp data, float punti, LivelloPartita difficolta) {
        this.email_utente = email_utente;
        this.data = data;
        this.punti = punti;
        this.difficolta = difficolta;
    }

    public String getEmail_utente() {
        return email_utente;
    }

    public Timestamp getData() {
        return data;
    }

    public float getPunti() {
        return punti;
    }

    public LivelloPartita getDifficolta() {
        return difficolta;
    }

    @Override
    public String toString() {
        return "Classifica{" + "email_utente=" + email_utente + ", data=" + data + ", punti=" + punti + ", difficolta=" + difficolta + '}';
    }
    
    
    
}
