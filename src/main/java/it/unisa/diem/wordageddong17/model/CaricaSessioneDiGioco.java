/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.model;

import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Mattia Sanzari
 */
public class CaricaSessioneDiGioco extends Service<List<GeneratoreDomande.Domanda>>  {
    
    SessioneDiGioco sessione;
    LivelloPartita livello;

    public CaricaSessioneDiGioco(SessioneDiGioco sessione, LivelloPartita livello) {
        this.sessione = sessione;
        this.livello = livello;
    }

    
    

    @Override
    protected Task<List<GeneratoreDomande.Domanda>> createTask() {
       
        return new Task<List<GeneratoreDomande.Domanda>>(){
            @Override
            protected List<GeneratoreDomande.Domanda> call() throws Exception {
               
                updateProgress(0, 100);
                CaricaSessioneDiGioco.this.sessione.generaDocumenti(CaricaSessioneDiGioco.this.livello);
                
                updateProgress(60, 100);
                CaricaSessioneDiGioco.this.sessione.generaDomande();
                updateProgress(100, 100);
                System.out.println("Nel service :"+ CaricaSessioneDiGioco.this.sessione.getDomande());
                return  CaricaSessioneDiGioco.this.sessione.getDomande();
            }  
        };
    } 
}
