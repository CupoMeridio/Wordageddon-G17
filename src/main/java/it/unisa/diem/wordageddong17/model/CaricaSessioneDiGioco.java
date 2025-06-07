package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.database.DatabaseStopWords;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
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
    DAOListaStopWords dbSW;
    DAODocumentoDiTesto dbDT;

    public CaricaSessioneDiGioco(SessioneDiGioco sessione, LivelloPartita livello) {
        this.sessione = sessione;
        this.livello = livello;
        dbSW = DatabaseStopWords.getInstance();
        dbDT = DatabaseDocumentoDiTesto.getInstance();
        
    }

    
    

    @Override
    protected Task<List<GeneratoreDomande.Domanda>> createTask() {
       
        return new Task<List<GeneratoreDomande.Domanda>>(){
            @Override
            protected List<GeneratoreDomande.Domanda> call() throws Exception {
               
                updateProgress(0, 100);
                CaricaSessioneDiGioco.this.sessione.generaDocumenti(CaricaSessioneDiGioco.this.livello,dbDT.prendiDocumentiPerDifficolta(livello));
                updateProgress(20, 100);
                CaricaSessioneDiGioco.this.sessione.getAnalisi().setStopWords(dbSW.PrendiStopwords("ListaStopwords"));
                updateProgress(60, 100);
                CaricaSessioneDiGioco.this.sessione.generaDomande();
                updateProgress(100, 100);
                System.out.println("Nel service :"+ CaricaSessioneDiGioco.this.sessione.getDomande());
                return  CaricaSessioneDiGioco.this.sessione.getDomande();
            }  
        };
    } 
}
