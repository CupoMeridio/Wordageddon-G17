package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseUtente;
import it.unisa.diem.wordageddong17.interfaccia.DAOUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PrendiUtenteService extends Service<Utente>{
    private final DAOUtente dbu;
    private  String email;
    
    public PrendiUtenteService(String email){
        this.email = email;
        dbu = DatabaseUtente.getInstance();
    }
    
    public PrendiUtenteService(){
        dbu = DatabaseUtente.getInstance();
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
    @Override
    protected Task<Utente> createTask() {
        return new Task<Utente>(){
            @Override
            protected Utente call() throws Exception {
                return dbu.prendiUtente(email); 
            }
        };
    }
    
}
