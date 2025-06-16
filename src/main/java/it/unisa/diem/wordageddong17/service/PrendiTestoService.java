package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Questo servizio esegue il recupero in background di un documento di testo.
 * <p>
 * La classe estende {@link javafx.concurrent.Service} e fornisce un {@link Task} che, 
 * una volta eseguito, restituisce il contenuto di un file di testo come array di byte.
 * Tale operazione viene eseguita tramite il metodo {@code prendiTesto} dell'interfaccia
 * {@link it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto}, la cui istanza è ottenuta 
 * tramite il singleton di {@link it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto}.
 * </p>
 *
 * @see javafx.concurrent.Service
 * @see it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto
 * @see it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto
 */
public class PrendiTestoService extends Service<byte[]> {

    /**
     * Nome del file che contiene il documento di testo da recuperare.
     */
    private String nomeFile;
    
    /**
     * Istanza del data access object per il documento di testo.
     */
    private final DAODocumentoDiTesto dbDT;

    /**
     * Costruisce una nuova istanza del servizio impostando il nome del file.
     *
     * @param nomeFile il nome del file da cui recuperare il documento di testo
     */
    public PrendiTestoService(String nomeFile) {
        this.nomeFile = nomeFile;
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
    }

    /**
     * Costruisce una nuova istanza del servizio senza impostare inizialmente il nome del file.
     * È necessario imposto successivamente il nome del file tramite il metodo {@link #setNomeFile(String)}
     * prima dell'esecuzione del servizio.
     */
    public PrendiTestoService() {
        this.dbDT = DatabaseDocumentoDiTesto.getInstance();
    }

    /**
     * Imposta il nome del file da cui recuperare il documento di testo.
     *
     * @param nomeFile il nome del file da utilizzare per il recupero
     */
    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    /**
     * Crea ed esegue un task in background per il recupero del documento di testo.
     * <p>
     * Il task invoca il metodo {@code prendiTesto} del DAO associato, passando il nome del file,
     * e restituisce il contenuto del file come array di byte.
     * </p>
     *
     * @return un {@link Task} che, una volta completato, restituisce il contenuto del documento come array di byte
     */
    @Override
    protected Task<byte[]> createTask() {
        return new Task<byte[]>() {
            @Override
            protected byte[] call() throws Exception {
                return dbDT.prendiTesto(nomeFile);
            }
        };
    }
}
