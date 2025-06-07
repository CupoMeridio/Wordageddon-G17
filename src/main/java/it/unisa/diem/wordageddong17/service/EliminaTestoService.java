package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class EliminaTestoService extends Service<Void>{
    private final DAODocumentoDiTesto dbDT;
    private List<DocumentoDiTesto> selezionati;
    
    public EliminaTestoService(List<DocumentoDiTesto> selezionati){
        dbDT = DatabaseDocumentoDiTesto.getInstance();
        this.selezionati = selezionati;
    }
    
    public EliminaTestoService(){
        dbDT = DatabaseDocumentoDiTesto.getInstance();
    }

    public void setSelezinati(List<DocumentoDiTesto> selezionati){
        this.selezionati = selezionati;
    }
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() {
                for (DocumentoDiTesto doc : selezionati) {
                    dbDT.cancellaTesto(doc.getNomeFile()); // operazione di eliminazione lato "server"
                }
                return null;
            }
        };
    }
    
}
