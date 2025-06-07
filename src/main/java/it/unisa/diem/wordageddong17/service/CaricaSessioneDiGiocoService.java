package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.database.DatabaseStopWords;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import it.unisa.diem.wordageddong17.model.GeneratoreDomande;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.model.SessioneDiGioco;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Mattia Sanzari
 */
public class CaricaSessioneDiGiocoService extends Service<List<GeneratoreDomande.Domanda>>  {
    
    private SessioneDiGioco sessione;
    private LivelloPartita livello;
    private DAOListaStopWords dbSW;
    private final DAODocumentoDiTesto dbDT;;

    public CaricaSessioneDiGiocoService(SessioneDiGioco sessione, LivelloPartita livello) {
        this.sessione = sessione;
        this.livello = livello;
        this.dbDT= DatabaseDocumentoDiTesto.getInstance();
        this.dbSW= DatabaseStopWords.getInstance();
    }

    public SessioneDiGioco getSessione() {
        return sessione;
    }

    public LivelloPartita getLivello() {
        return livello;
    }

    public DAOListaStopWords getDbSW() {
        return dbSW;
    }

    public DAODocumentoDiTesto getDbDT() {
        return dbDT;
    }
   

    @Override
    protected Task<List<GeneratoreDomande.Domanda>> createTask() {
       
        return new Task<List<GeneratoreDomande.Domanda>>(){
            @Override
            protected List<GeneratoreDomande.Domanda> call() throws Exception {
               
                updateProgress(0, 100);
                CaricaSessioneDiGiocoService.this.sessione.generaDocumenti(CaricaSessioneDiGiocoService.this.livello,dbDT.prendiDocumentiPerDifficolta(livello));
                updateProgress(20, 100);
                CaricaSessioneDiGiocoService.this.sessione.getAnalisi().setStopWords(dbSW.PrendiStopwords("ListaStopwords"));
                updateProgress(60, 100);
                CaricaSessioneDiGiocoService.this.sessione.generaDomande();
                updateProgress(70, 100);
                CaricaSessioneDiGiocoService.this.sessione.salvaSessioneDiGioco();
                updateProgress(100, 100);
                System.out.println("Nel service :"+ CaricaSessioneDiGiocoService.this.sessione.getDomande());
                return  CaricaSessioneDiGiocoService.this.sessione.getDomande();
            }  
        };
    } 
}
