package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import java.util.ArrayList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Questo servizio recupera in background tutti i documenti di testo presenti nel database.
 * <p>
 * La classe estende {@link javafx.concurrent.Service} parametrizzata con {@link ArrayList} di
 * {@link it.unisa.diem.wordageddong17.model.DocumentoDiTesto}. Il recupero dei documenti viene eseguito
 * tramite un {@link Task} che invoca il metodo {@code prendiTuttiIDocumenti()} sul DAO
 * ottenuto da {@link it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto}.
 * </p>
 *
 * @see javafx.concurrent.Service
 * @see it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto
 * @see it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto
 * @see it.unisa.diem.wordageddong17.model.DocumentoDiTesto
 */
public class PrendiTuttiIDocumentiService extends Service<ArrayList<DocumentoDiTesto>> {

    /**
     * Istanza del Data Access Object per il documento di testo.
     */
    private final DAODocumentoDiTesto dbDT;

    /**
     * Costruisce una nuova istanza del servizio utilizzando l'istanza singleton 
     * di {@link it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto}.
     */
    public PrendiTuttiIDocumentiService() {
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
    }
    
    /**
     * Crea ed esegue un {@link Task} in background per il recupero di tutti i documenti di testo.
     * <p>
     * Questo metodo sovrascrive il metodo {@link Service#createTask()} e restituisce un task che,
     * una volta completato, restituisce un {@link ArrayList} di documenti di testo ottenuti dal database.
     * </p>
     *
     * @return un {@link Task} che, al completamento, fornisce un {@link ArrayList} di
     *         {@link it.unisa.diem.wordageddong17.model.DocumentoDiTesto}
     */
    @Override
    protected Task<ArrayList<DocumentoDiTesto>> createTask() {
        return new Task<ArrayList<DocumentoDiTesto>>() {
            @Override
            protected ArrayList<DocumentoDiTesto> call() throws Exception {
                return dbDT.prendiTuttiIDocumenti();
            }
        };
    }
}
