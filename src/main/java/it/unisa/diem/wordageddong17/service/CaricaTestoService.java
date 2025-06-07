package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CaricaTestoService extends Service<Void>{
    private String email;
    private String nomeFile;
    private String difficoltà;
    private byte[] documento;
    private Lingua lingua;
    private final DAODocumentoDiTesto dbDT;

    public CaricaTestoService(String email, String nomeFile, String difficoltà, byte[] documento, Lingua lingua) {
        this.email = email;
        this.nomeFile = nomeFile;
        this.difficoltà = difficoltà;
        this.documento = documento;
        this.lingua = lingua;
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
    }

    public CaricaTestoService() {
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public void setDifficoltà(String difficoltà) {
        this.difficoltà = difficoltà;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }
    
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                dbDT.caricaTesto(email, nomeFile, difficoltà, documento, lingua);               
                return null;
            }  
        };
    }
    
}
