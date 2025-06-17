package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseStopWords;
import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Questo servizio si occupa del recupero in background di un file contenente le stop words.
 * <p>
 * La classe estende {@link javafx.concurrent.Service} e utilizza l'interfaccia
 * {@link it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords} per ottenere il file
 * delle stop words, rappresentato come un array di byte. L'istanza del data access
 * object (DAO) viene ottenuta tramite il singleton di {@link it.unisa.diem.wordageddong17.database.DatabaseStopWords}.
 *
 * @see javafx.concurrent.Service
 * @see it.unisa.diem.wordageddong17.database.DatabaseStopWords
 * @see it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords
 */
public class PrendiStopWordsService extends Service<byte[]> {
    
    /**
     * DAO per il recupero dei file delle stop words.
     */
    private final DAOListaStopWords dbSW;
    
    /**
     * Nome del file contenente le stop words da recuperare.
     */
    private String nomeFile;
    
    /**
     * Costruisce una nuova istanza del servizio per il recupero del file delle stop words.
     *
     * @param nomeFile il nome del file delle stop words da recuperare
     */
    public PrendiStopWordsService(String nomeFile) {
        this.dbSW = DatabaseStopWords.getInstance();
        this.nomeFile = nomeFile;
    }
    
    /**
     * Imposta il nome del file contenente le stop words da recuperare.
     *
     * @param nomeFile il nome del file
     */
    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }
    
    /**
     * Crea e restituisce un task che viene eseguito in background per il recupero del file
     * delle stop words.
     * <p>
     * All'interno del task viene invocato il metodo {@code PrendiStopwords} del DAO, il cui
     * risultato (un array di byte) viene stampato in console come stringa e poi restituito.
     * </p>
     *
     * @return un {@link Task} che, al completamento, restituisce il contenuto del file come array di byte
     */
    @Override
    protected Task<byte[]> createTask() {
        return new Task<byte[]>() {
            @Override
            protected byte[] call() throws Exception {
                byte[] file = dbSW.prendiStopwords(nomeFile);
                System.out.println(new String(file));
                return file;
            }
        };
    }
}
