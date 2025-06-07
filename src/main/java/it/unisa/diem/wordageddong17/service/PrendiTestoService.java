package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PrendiTestoService extends Service<byte[]>{
    private String nomeFile;
    private final DAODocumentoDiTesto dbDT;

    public PrendiTestoService(String nomeFile) {
        this.nomeFile = nomeFile;
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
    }

    public PrendiTestoService() {
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }
    
    
    
    

    @Override
    protected Task<byte[]> createTask() {
        return new Task<byte[]>(){
            @Override
            protected byte[] call() throws Exception {
                return dbDT.prendiTesto(nomeFile);
            }
            
        };
    }
    
}
