package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class InserisciUtenteService extends Service<Void>{
    private final DAOUtente dbu;
    private String username;
    private String email;
    private String password;
    private byte[] immagineBytes;
    
    public InserisciUtenteService(String username, String email, String password, byte[] immagineBytes){
        this.email = email;
        this.username = username;
        this.password = password;
        this.immagineBytes = immagineBytes;
        this.dbu = DatabaseUtente.getInstance();
    }
    
    public InserisciUtenteService(){
        this.dbu = DatabaseUtente.getInstance();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImmagineBytes(byte[] immagineBytes) {
        this.immagineBytes = immagineBytes;
    }

    
    @Override
    protected Task<Void> createTask() {
        dbu.inserisciUtente(username, email, password, immagineBytes);
        return null;
    }
    



    
}
