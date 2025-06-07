package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoginService extends Service<Utente> {
    private String email;
    private String password;
    private final DAOUtente dbu; // Supponendo che questo sia il tuo service esistente

    public LoginService(String email, String password) {
        this.email = email;
        this.password = password;
        this.dbu = DatabaseUtente.getInstance();
    }
    
    public LoginService(){
        this.dbu = DatabaseUtente.getInstance();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected Task<Utente> createTask() {
        return new Task<>() {
            @Override
            protected Utente call() throws Exception {
                // Prima verifica la password
                if (!dbu.verificaPassword(email, password)) {
                    return null;
                }
                // Poi recupera l'utente usando il service esistente
                return dbu.prendiUtente(email);
            }
        };
    }
}
