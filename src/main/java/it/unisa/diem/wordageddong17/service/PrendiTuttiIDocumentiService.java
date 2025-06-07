package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import java.util.ArrayList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PrendiTuttiIDocumentiService extends Service<ArrayList<DocumentoDiTesto>>{
    private final DAODocumentoDiTesto dbDT;

    public PrendiTuttiIDocumentiService() {
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
    }
    
    
    @Override
    protected Task<ArrayList<DocumentoDiTesto>> createTask() {
        
        return new Task<ArrayList<DocumentoDiTesto>>(){
            @Override
            protected ArrayList<DocumentoDiTesto> call() throws Exception {
                return dbDT.prendiTuttiIDocumenti();
            }    
        };       
    }
    
}
