package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseClassifica;
import it.unisa.diem.wordageddong17.interfaccia.DAOClassifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CaricaPunteggioService extends Service<Void>{
    private final DAOClassifica dbC;
    private String email;
    private float punteggio;
    private LivelloPartita difficoltà;

    public CaricaPunteggioService() {
        this.dbC = DatabaseClassifica.getInstance();
    }

    public CaricaPunteggioService(String email, float punteggio, LivelloPartita difficoltà) {
        this.dbC = DatabaseClassifica.getInstance();
        this.email = email;
        this.punteggio = punteggio;
        this.difficoltà = difficoltà;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPunteggio(float punteggio) {
        this.punteggio = punteggio;
    }

    public void setDifficoltà(LivelloPartita difficoltà) {
        this.difficoltà = difficoltà;
    }
    
    
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                System.out.println(email+" "+punteggio+" "+difficoltà);
                dbC.inserisciPunteggio(email, punteggio,difficoltà);
                return null;
            }    
        };
    }
    
}
