package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ModificaFotoProfiloService extends Service<Void>{

    private byte[] immagine;
    private String email;
    private final DAOUtente dbu;

    public ModificaFotoProfiloService(byte[] immagine, String email) {
        this.immagine = immagine;
        this.email = email;
        this.dbu = DatabaseUtente.getInstance();
    }

    public ModificaFotoProfiloService() {
        this.dbu = DatabaseUtente.getInstance();
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                dbu.modificaFotoProfilo(email, immagine);
                return null;
            }
            
        };
    }
    
}
