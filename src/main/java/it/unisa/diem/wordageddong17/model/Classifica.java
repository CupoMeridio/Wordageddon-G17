package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.database.DatabaseClassifica;
import java.sql.Timestamp;

/**
 * @author cupom
 */
public class Classifica {
    private final String username;
    private final Timestamp data;
    private final float punti;
    private final LivelloPartita difficolta;
    private final DatabaseClassifica sbc = DatabaseClassifica.getInstance();

    public Classifica(String email_utente, Timestamp data, float punti, LivelloPartita difficolta) {
        this.username = email_utente;
        this.data = data;
        this.punti = punti;
        this.difficolta = difficolta;
    }
    
    public String getUsername() {
        return username;
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
        return "Classifica{" + "username=" + username + ", data=" + data + ", punti=" + punti + ", difficolta=" + difficolta + '}';
    }

    
    
    
}
