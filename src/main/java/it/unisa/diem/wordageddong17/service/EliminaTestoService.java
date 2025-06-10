package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per l'eliminazione di documenti di testo dal database.
 * Questa classe utilizza {@link DAODocumentoDiTesto} per cancellare documenti 
 * selezionati dall'utente.
 *
 * @author Mattia Sanzari
 */
public class EliminaTestoService extends Service<Void> {
    
    private final DAODocumentoDiTesto dbDT;
    private List<DocumentoDiTesto> selezionati;

    /**
     * Costruttore con parametri per inizializzare il servizio di eliminazione dei testi.
     * 
     * @param selezionati Lista dei documenti di testo da eliminare.
     */
    public EliminaTestoService(List<DocumentoDiTesto> selezionati) {
        dbDT = DatabaseDocumentoDiTesto.getInstance();
        this.selezionati = selezionati;
    }

    /**
     * Costruttore di default che inizializza l'accesso al database dei documenti di testo.
     */
    public EliminaTestoService() {
        dbDT = DatabaseDocumentoDiTesto.getInstance();
    }

    /**
     * Imposta la lista dei documenti di testo da eliminare.
     * 
     * @param selezionati La lista di documenti da cancellare.
     */
    public void setSelezionati(List<DocumentoDiTesto> selezionati) {
        this.selezionati = selezionati;
    }

    /**
     * Crea un task asincrono per l'eliminazione dei documenti di testo dal database.
     * 
     * @return Un {@link Task} che esegue la cancellazione dei documenti selezionati.
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                for (DocumentoDiTesto doc : selezionati) {
                    dbDT.cancellaTesto(doc.getNomeFile()); // operazione di eliminazione lato server
                }
                return null;
            }
        };
    }
}
